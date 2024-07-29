package com.example.livealone.payment.service;

import com.example.livealone.order.entity.Order;
import com.example.livealone.payment.dto.PaymentRequestDto;
import com.example.livealone.payment.dto.PaymentResponseDto;
import com.example.livealone.payment.entity.Payment;
import com.example.livealone.payment.entity.PaymentMethod;
import com.example.livealone.payment.entity.PaymentStatus;
import com.example.livealone.payment.repository.PaymentRepository;
import com.example.livealone.user.entity.User;
import com.example.livealone.user.repository.UserRepository;
import com.example.livealone.order.repository.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	@Value("TC0ONETIME")
	private String cid;

	@Value("DEV1983009FCE70023372B535B4EB027DEB9824F")
	private String secretKey;

	@Value("http://localhost:8080/payment/kakao/completePayment")
	private String approvalUrl;

	@Value("http://localhost:3000/completepayment")
	private String frontendApprovalUrl;

	@Value("http://localhost:8080/payment")
	private String cancelUrl;

	@Value("http://localhost:8080/payment")
	private String failUrl;

	@Value("test_ck_kYG57Eba3GPyQ4zAdxQkVpWDOxmA")
	private String tossClientKey;

	@Value("test_sk_jExPeJWYVQ1ekabzNRlxV49R5gvN")
	private String tossSecretKey;

	@Value("http://localhost:8080/completePayment")
	private String tossRetUrl;

	@Value("http://localhost:8080/payment")
	private String tossRetCancelUrl;

	@Value("http://localhost:8080/payment")
	private String tossResultCallback;

	public PaymentResponseDto createKakaoPayReady(PaymentRequestDto requestDto) {
		String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
		System.out.println("createKakaoPayReady 진입 완료!!!");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "SECRET_KEY " + secretKey);
		headers.set("Content-type", "application/json"); // 500 에러 해결 지점

		HashMap<String, String> params = new HashMap<>();
		params.put("cid", "TC0ONETIME");
		params.put("partner_order_id", "1L");
		params.put("partner_user_id", "3L");
		params.put("item_name", "초코파이");
		params.put("quantity", "1");
		params.put("total_amount", "2200");
		params.put("vat_amount", "200");
		params.put("tax_free_amount", "0");
		params.put("approval_url", approvalUrl);
		params.put("cancel_url", cancelUrl);
		params.put("fail_url", failUrl);

		HttpEntity<HashMap<String, String>> request = new HttpEntity<>(params, headers);
		System.out.println("params");
		System.out.println(params);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			System.out.println("response");
			System.out.println(response);

			JsonNode jsonNode = objectMapper.readTree(response.getBody());

			User user = userRepository.findById(requestDto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + requestDto.getUserId()));

			Order order = orderRepository.findById(requestDto.getOrderId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + requestDto.getOrderId()));

			String tid = jsonNode.get("tid").asText();

			int retryCount = 0;
			//            while (paymentRepository.existsByTid(tid) && retryCount < 3) {
			//                response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			//                jsonNode = objectMapper.readTree(response.getBody());
			//                tid = jsonNode.get("tid").asText();
			//                retryCount++;
			//            }
			System.out.println("hello, world!");

			if (paymentRepository.existsByTid(tid)) {
				System.out.println("hello, world!");
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
				.build();

			paymentRepository.save(payment);

			System.out.println("hello world!");
			System.out.println(jsonNode.get("next_redirect_pc_url").asText());
			System.out.println(jsonNode.get("next_redirect_pc_url").asText());
			System.out.println(jsonNode.get("next_redirect_pc_url").asText());


			return PaymentResponseDto.builder()
				.status("READY")
				.message("결제 준비 완료")
				.paymentId(payment.getId())
				.userId(requestDto.getUserId())
				.orderId(requestDto.getOrderId())
				.amount(requestDto.getAmount())
				.paymentMethod(requestDto.getPaymentMethod())
				.createdAt(payment.getCreatedAt().toString())
				.nextRedirectUrl(jsonNode.get("next_redirect_pc_url").asText() + "?redirect_url=" + frontendApprovalUrl) // 리디렉션 URL 수정
				.build();

		} catch (Exception e) {
			e.printStackTrace();
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
	public PaymentResponseDto approveKakaoPayPayment(String pgToken, Long orderId, Long userId) {
		String url = "https://kapi.kakao.com/v1/payment/approve";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "SECRET_KEY " + secretKey);

		Map<String, String> params = new HashMap<>();
		params.put("cid", cid);
		params.put("tid", getTidByOrderId(orderId));
		params.put("partner_order_id", orderId.toString());
		params.put("partner_user_id", userId.toString());
		params.put("pg_token", pgToken);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());

			Payment payment = paymentRepository.findByOrderId(orderId);
			payment.updateStatus(PaymentStatus.COMPLETED);

			return PaymentResponseDto.builder()
				.status("COMPLETED")
				.message("결제 완료")
				.paymentId(orderId)
				.userId(userId)
				.orderId(orderId)
				.amount(payment.getAmount())
				.paymentMethod(payment.getPaymentMethod().name())
				.createdAt(jsonNode.get("created_at").asText())
				.updateAt(jsonNode.get("approved_at").asText())
				.build();

		} catch (Exception e) {
			e.printStackTrace();
			return PaymentResponseDto.builder()
				.status("FAILED")
				.message("결제 승인 실패")
				.build();
		}
	}

	/**
	 * 토스페이 결제 준비
	 *
	 * @param requestDto 결제 요청 DTO
	 * @return 결제 응답 DTO
	 */
	public PaymentResponseDto createTossPayReady(PaymentRequestDto requestDto) {
		String url = "https://pay.toss.im/api/v2/payments";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", requestDto.getOrderId().toString());
		params.put("amount", requestDto.getAmount());
		params.put("amountTaxFree", 0);
		params.put("productDesc", "Order " + requestDto.getOrderId());
		params.put("apiKey", tossClientKey);
		params.put("autoExecute", true);
		params.put("resultCallback", tossResultCallback);
		params.put("retUrl", tossRetUrl);
		params.put("retCancelUrl", tossRetCancelUrl);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());

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
				.build();

		} catch (Exception e) {
			e.printStackTrace();
			return PaymentResponseDto.builder()
				.status("FAILED")
				.message("결제 준비 실패")
				.build();
		}
	}

	/**
	 * 주문 ID로 tid 조회
	 *
	 * @param orderId 주문 ID
	 * @return tid
	 */
	private String getTidByOrderId(Long orderId) {
		Payment payment = paymentRepository.findByOrderId(orderId);
		return payment.getTid();
	}
}
