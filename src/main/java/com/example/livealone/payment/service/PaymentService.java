package com.example.livealone.payment.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.livealone.global.config.URIConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.livealone.order.entity.Order;
import com.example.livealone.order.repository.OrderRepository;
import com.example.livealone.order.service.OrderService;
import com.example.livealone.payment.dto.PaymentRequestDto;
import com.example.livealone.payment.dto.PaymentResponseDto;
import com.example.livealone.payment.entity.Payment;
import com.example.livealone.payment.entity.PaymentMethod;
import com.example.livealone.payment.entity.PaymentStatus;
import com.example.livealone.payment.repository.PaymentRepository;
import com.example.livealone.product.entity.Product;
import com.example.livealone.product.service.ProductService;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private final ProductService productService;
	private final OrderService orderService;

	private final URIConfig uriConfig;

	@Value("${payment.kakao.cid}")
	private String cid;

	@Value("${payment.kakao.secret-key}")
	private String secretKey;

	@Value("${payment.kakao.approval-url}")
	private String approvalUrl;

	@Value("${payment.kakao.cancel-url}")
	private String cancelUrl;

	@Value("${payment.kakao.fail-url}")
	private String failUrl;

	@Value("${payment.toss.client-key}")
	private String tossClientKey;

	@Value("${payment.toss.secret-key}")
	private String tossSecretKey;

	@Value("${payment.toss.ret-url}")
	private String tossRetUrl;

	@Value("${payment.toss.ret-cancel-url}")
	private String tossRetCancelUrl;

	@Value("${payment.toss.result-callback}")
	private String tossResultCallback;

	public PaymentResponseDto createKakaoPayReady(PaymentRequestDto requestDto) {
		// Ready API -> 성공 시 next url 리턴 -> 프론트에서 결제 진행 -> 사용자가 결제 수단 선택 후 비밀번호 인증까지 마치면 결제 대기 화면은 결제 준비 API 요청시
		// 전달 받은 approval_url에 pg_token 파라미터를 붙여 대기화면을 approval_url로 redirect
		// 인증완료 시 응답받은 pg_token과 tid로 최종 승인요청 -> online/v1/payment/approve

		String url = "https://open-api.kakaopay.com/online/v1/payment/ready";

		log.debug("Create Kakao pay ready 진입 URI :{} ", url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "SECRET_KEY " + secretKey);
		headers.set("Content-type", "application/json"); // 500 에러 해결 지점

		HashMap<String, String> params = new HashMap<>();
		params.put("cid", "TC0ONETIME");
		params.put("partner_order_id", String.valueOf(requestDto.getOrderId()));
		params.put("partner_user_id", String.valueOf(requestDto.getUserId()));
		params.put("item_name", requestDto.getItemName());
		params.put("quantity", String.valueOf(requestDto.getOrderQuantity()));
		int totalAmount = requestDto.getAmount() * requestDto.getOrderQuantity();
		params.put("total_amount", String.valueOf(totalAmount));
		params.put("vat_amount", "0");
		params.put("tax_free_amount", "0");
		params.put("approval_url", String.format("http://%s:8080/payment/kakao/complete?order_id=%d&user_id=%d",
			uriConfig.getServerHost(),
			requestDto.getOrderId(),
			requestDto.getUserId()));
		String failCancelUrl = String.format("http://%s:8080/payment/kakao/cancel?order_id=%d", uriConfig.getServerHost(), requestDto.getOrderId());
		params.put("cancel_url", failCancelUrl);
		params.put("fail_url", failCancelUrl);

		HttpEntity<HashMap<String, String>> request = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			JsonNode jsonNode = objectMapper.readTree(response.getBody());

			User user = userRepository.findById(requestDto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + requestDto.getUserId()));

			Order order = orderRepository.findById(requestDto.getOrderId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + requestDto.getOrderId()));

			String tid = jsonNode.get("tid").asText();

			if (paymentRepository.existsByTid(tid)) {
				rollbackAndDeleteOrder(requestDto.getOrderId());
				return PaymentResponseDto.builder()
					.status("FAILED")
					.message("결제 준비 실패: 중복된 TID")
					.build();
			}

			Payment payment = Payment.builder()
				.user(user)
				.order(order)
				.amount(requestDto.getAmount())
				.paymentMethod(PaymentMethod.KAKAO_PAY)
				.status(PaymentStatus.REQUESTED)
				.tid(tid)
				.orderQuantity(requestDto.getOrderQuantity())
				.shippingAddress(requestDto.getShippingAddress())
				.deliveryRequest(requestDto.getDeliveryRequest())
				.build();

			paymentRepository.save(payment);

			return PaymentResponseDto.builder()
				.status("READY")
				.message("결제 준비 완료")
				.paymentId(payment.getId())
				.userId(requestDto.getUserId())
				.orderId(requestDto.getOrderId())
				.amount(requestDto.getAmount())
				.paymentMethod(requestDto.getPaymentMethod())
				.createdAt(payment.getCreatedAt().toString())
				.nextRedirectUrl(jsonNode.get("next_redirect_pc_url").asText())
				.build();

		} catch (Exception e) {
			e.printStackTrace();
			rollbackAndDeleteOrder(requestDto.getOrderId());
			return PaymentResponseDto.builder()
				.status("FAILED")
				.message("결제 준비 실패")
				.build();
		}
	}

	/**
	 * 카카오페이 결제 승인
	 *
	 * @param pgToken 결제 승인 토큰
	 * @param orderId 주문 ID
	 * @param userId  사용자 ID
	 * @return 결제 응답 DTO
	 */
	@Transactional
	public PaymentResponseDto approveKakaoPayPayment(String pgToken, Long orderId, Long userId) {
		String url = "https://open-api.kakaopay.com/online/v1/payment/approve";

		log.debug("Approve Kakao payment");
		log.debug("pgToken : {}", pgToken);
		log.debug("orderId : {}", orderId);
		log.debug("userID : {}", userId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "SECRET_KEY " + secretKey);
		headers.set("Content-type", "application/json");

		Payment payment = paymentRepository.findByOrder_Id(orderId);
		if (payment == null) {
			rollbackAndDeleteOrder(orderId);
			return PaymentResponseDto.builder()
				.status("FAILED")
				.message("Invalid order ID: " + orderId)
				.build();
		}

		Map<String, String> params = new HashMap<>();
		params.put("cid", cid);
		params.put("tid", payment.getTid());
		params.put("partner_order_id", orderId.toString());
		params.put("partner_user_id", userId.toString());
		params.put("pg_token", pgToken);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

		try {
			log.debug("Send Request");
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());

			log.debug("jsonNode : {}", jsonNode);

			payment.updateStatus(PaymentStatus.COMPLETED);
			paymentRepository.save(payment);

			return PaymentResponseDto.builder()
				.status("COMPLETED")
				.message("결제 완료")
				.paymentId(orderId)
				.userId(userId)
				.orderId(orderId)
				.amount(payment.getAmount())
				.paymentMethod(payment.getPaymentMethod().name())
				.createdAt(jsonNode.get("created_at").asText())
				.updatedAt(jsonNode.get("approved_at").asText())
				.build();

		} catch (Exception e) {
			rollbackAndDeleteOrder(orderId);
			log.debug(e.getMessage());
			return PaymentResponseDto.builder()
				.status("FAILED")
				.message("결제 승인 실패")
				.build();
		}
	}

	/**
	 * 카카오페이 결제 중 취소
	 * @param orderId 주문 ID
	 */
	@Transactional
	public void cancelKakaoPayment(Long orderId) {
		rollbackAndDeleteOrder(orderId);
	}

	/**
	 * 토스페이 결제 준비(생성)
	 *
	 * @param requestDto 결제 요청 DTO
	 * @return 결제 응답 DTO
	 */
	public PaymentResponseDto createTossPayReady(PaymentRequestDto requestDto) {
		String url = "https://pay.toss.im/api/v2/payments";

		log.debug("Toss pay read : {}", url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> params = new HashMap<>();
		String createOrderNo = String.format("livealone:%d", requestDto.getOrderId()) + ":" + LocalDate.now();
		params.put("orderNo", createOrderNo);
		params.put("amount", requestDto.getAmount() * requestDto.getOrderQuantity());
		params.put("amountTaxFree", "0"); // requestDto에서 받아옴
		params.put("productDesc", requestDto.getItemName()); // requestDto에서 받아옴
		params.put("apiKey", tossClientKey);
		params.put("autoExecute", true);
		params.put("callbackVersion", "V2");
		params.put("resultCallback", tossResultCallback);

		String createRetUrl = String.format("http://%s:8080/ORDER-CHECK?orderno=%s", uriConfig.getServerHost(),
			createOrderNo);
		params.put("retUrl", createRetUrl);

		String cancelUrl = String.format("http://%s:8080/payment/toss/cancel?orderno=%s", uriConfig.getServerHost(), createOrderNo);
		params.put("retCancelUrl", cancelUrl);

		log.debug("request : {}", params);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

		try {
			log.debug("Send Request");
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			log.debug("jsonNode : {}", jsonNode);

			// 필드 존재 여부 체크
			if (jsonNode.has("payToken") && jsonNode.has("checkoutPage")) {
				User user = userRepository.findById(requestDto.getUserId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + requestDto.getUserId()));

				Order order = orderRepository.findById(requestDto.getOrderId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + requestDto.getOrderId()));

				Payment payment = Payment.builder()
					.user(user)
					.order(order)
					.amount(requestDto.getAmount())
					.paymentMethod(PaymentMethod.TOSS_PAY)
					.status(PaymentStatus.REQUESTED)
					.tid(jsonNode.get("payToken").asText())
					.orderQuantity(requestDto.getOrderQuantity())
					.shippingAddress(requestDto.getShippingAddress())
					.deliveryRequest(requestDto.getDeliveryRequest())
					.build();

				paymentRepository.save(payment);

				return PaymentResponseDto.builder()
					.status("READY")
					.message("결제 준비 완료")
					.paymentId(payment.getId())
					.userId(requestDto.getUserId())
					.orderId(requestDto.getOrderId())
					.amount(requestDto.getAmount())
					.paymentMethod(requestDto.getPaymentMethod())
					.createdAt(payment.getCreatedAt().toString())
					.nextRedirectUrl(jsonNode.get("checkoutPage").asText())
					.build();
			} else {
				rollbackAndDeleteOrder(requestDto.getOrderId());
				return PaymentResponseDto.builder()
					.status("FAILED")
					.message("결제 준비 실패: 필요한 필드가 응답에 없습니다.")
					.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			rollbackAndDeleteOrder(requestDto.getOrderId());
			return PaymentResponseDto.builder()
				.status("FAILED")
				.message("결제 준비 실패")
				.build();
		}
	}

	// /**
	//  * 토스페이 결제 승인
	//  *
	//  * @param payToken 결제 고유 토큰
	//  * @return 결제 응답 DTO
	//  */
	// @Transactional
	// public PaymentResponseDto approveTossPayPayment(String payToken) {
	// 	String url = "https://pay.toss.im/api/v2/execute";
	//
	// 	log.debug("payToken : {}", payToken);
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.setContentType(MediaType.APPLICATION_JSON);
	//
	// 	Map<String, String> params = new HashMap<>();
	// 	params.put("apiKey", tossSecretKey);
	// 	params.put("payToken", payToken);
	//
	// 	HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);
	//
	// 	try {
	// 		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
	// 		JsonNode jsonNode = objectMapper.readTree(response.getBody());
	//
	// 		Payment payment = paymentRepository.findByTid(payToken);
	// 		if (payment == null) {
	// 			throw new IllegalArgumentException("Invalid payToken: " + payToken);
	// 		}
	//
	// 		payment.updateStatus(PaymentStatus.COMPLETED);
	//
	// 		return PaymentResponseDto.builder()
	// 			.status("COMPLETED")
	// 			.message("결제 완료")
	// 			.paymentId(payment.getId())
	// 			.userId(payment.getUser().getId())
	// 			.orderId(payment.getOrder().getId())
	// 			.amount(payment.getAmount())
	// 			.paymentMethod(payment.getPaymentMethod().name())
	// 			.createdAt(payment.getCreatedAt().toString())
	// 			.updateAt(jsonNode.get("approved_at").asText())
	// 			.build();
	//
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return PaymentResponseDto.builder()
	// 			.status("FAILED")
	// 			.message("결제 승인 실패")
	// 			.build();
	// 	}
	// }

	@Transactional
	public void rollbackAndDeleteOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));
		Product product = order.getProduct();

		Payment payment = paymentRepository.findByOrder_Id(orderId);

		// 재고 롤백
		product.rollbackStock(order.getQuantity());
		productService.saveProduct(product);

		// 주문 삭제
		paymentRepository.delete(payment);
		orderRepository.delete(order);
	}


	@Transactional
	public String returnOrderCheckPage(String orderno, String status, String orderNo, String payMethod, String bankCode,
		String cardCompany) {
		log.debug("orderno : {}", orderno); // livealone:192024-08-08
		log.debug("status : {}", status);
		log.debug("paymeThod ; {}", payMethod);

		// livealone:192024-08-08
		// livealone:19:2024-08-08 (: 로 tokenize) -> [livealone, 19, 2024-08-08]
		// findByOrder_Id(19)

		// 결제 상태 업데이트 로직 추가
		String[] tokens = orderno.split(":");
		Long orderId = Long.parseLong(tokens[1]);
		Payment payment = paymentRepository.findByOrder_Id(orderId);
		if (payment == null) {
			throw new IllegalArgumentException("Invalid orderno: " + orderno);
		}

		if (status.equals("PAY_COMPLETE")) {
			payment.updateStatus(PaymentStatus.COMPLETED);
			paymentRepository.save(payment);
		} else {
			payment.updateStatus(PaymentStatus.FAILED);
			rollbackAndDeleteOrder(payment.getOrder().getId());
		}

		String url = String.format("http://%s:3000/completepayment", uriConfig.getFrontServerHost());
		return url;
	}

	@Transactional
	public String cancelOrderCheckPage(String orderno) {
		log.debug("orderno : {}", orderno); // livealone:192024-08-08

		// livealone:192024-08-08
		// livealone:19:2024-08-08 (: 로 tokenize) -> [livealone, 19, 2024-08-08]
		// findByOrder_Id(19)

		// 결제 상태 업데이트 로직 추가
		String[] tokens = orderno.split(":");
		Long orderId = Long.parseLong(tokens[1]);
		Payment payment = paymentRepository.findByOrder_Id(orderId);
		if (payment == null) {
			throw new IllegalArgumentException("Invalid orderno: " + orderno);
		}

		payment.updateStatus(PaymentStatus.FAILED);
		rollbackAndDeleteOrder(payment.getOrder().getId());

		String url = String.format("http://%s:3000/streaming", uriConfig.getFrontServerHost());
		return url;
	}

	/**
	 * 사용자별 결제 상태가 COMPLETED인 결제 내역 조회
	 *
	 * @param userId 사용자 ID
	 * @return 결제 내역 리스트
	 */
	public List<PaymentResponseDto> getCompletedPaymentsByUserId(Long userId) {
		List<Payment> payments = paymentRepository.findByUserIdAndStatus(userId, PaymentStatus.COMPLETED);
		return payments.stream()
			.map(payment -> PaymentResponseDto.builder()
				.status(payment.getStatus().name())
				.message("결제 완료")
				.paymentId(payment.getId())
				.userId(payment.getUser().getId())
				.orderId(payment.getOrder().getId())
				.productName(payment.getOrder().getProduct().getName())
				.quantity(payment.getOrder().getQuantity())
				.amount(payment.getAmount())
				.paymentMethod(payment.getPaymentMethod().name())
				.createdAt(payment.getCreatedAt().toString())
				.updatedAt(payment.getUpdatedAt().toString())
				.build())
			.collect(Collectors.toList());
	}
}