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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.Base64;
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

	@Value("http://localhost:8080/payment/kakao/complete")
	private String approvalUrl;

	@Value("http://localhost:8080/payment")
	private String cancelUrl;

	@Value("http://localhost:8080/payment")
	private String failUrl;

	@Value("sk_test_w5lNQylNqa5lNQe013Nq")
	private String tossClientKey;

	@Value("test_sk_jExPeJWYVQ1ekabzNRlxV49R5gvN")
	private String tossSecretKey;

	// @Value("test_sk_jExPeJWYVQ1ekabzNRlxV49R5gvN")
	// private String tossSecretKey;

	@Value("http://seoldarin.iptime.org:7956/ORDER-CHECK?orderno=1")
	private String tossRetUrl;

	@Value("http://seoldarin.iptime.org:7956/close")
	private String tossRetCancelUrl;

	@Value("http://seoldarin.iptime.org:7956/callback")
	private String tossResultCallback;

	public PaymentResponseDto createKakaoPayReady(PaymentRequestDto requestDto) {
		// Ready API -> 성공 시 next url 리턴 -> 프론트에서 결제 진행 -> 사용자가 결제 수단 선택 후 비밀번호 인증까지 마치면 결제 대기 화면은 결제 준비 API 요청시
		// 전달 받은 approval_url에 pg_token 파라미터를 붙여 대기화면을 approval_url로 redirect
		// 인증완료 시 응답받은 pg_token과 tid로 최종 승인요청 -> online/v1/payment/approve

		String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
		System.out.println("createKakaoPayReady 진입 완료!!!");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "SECRET_KEY " + secretKey);
		headers.set("Content-type", "application/json"); // 500 에러 해결 지점

		HashMap<String, String> params = new HashMap<>();
		params.put("cid", "TC0ONETIME");
		params.put("partner_order_id", "2");
		params.put("partner_user_id", "2");
		params.put("item_name", "초코파이");
		params.put("quantity", "1");
		params.put("total_amount", "2200");
		params.put("vat_amount", "200");
		params.put("tax_free_amount", "0");
		params.put("approval_url", String.format("http://localhost:8080/payment/kakao/complete?order_id=%d&user_id=%d", 2, 2));
		params.put("cancel_url", cancelUrl);
		params.put("fail_url", failUrl);

		HttpEntity<HashMap<String, String>> request = new HttpEntity<>(params, headers);
		System.out.println("params");
		System.out.println(params);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			// Debug the response
			System.out.println("Response Body: " + response.getBody());

			JsonNode jsonNode = objectMapper.readTree(response.getBody());

			// Debug the parsed JSON
			System.out.println("Parsed JSON: " + jsonNode);

			User user = userRepository.findById(requestDto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + requestDto.getUserId()));

			Order order = orderRepository.findById(requestDto.getOrderId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + requestDto.getOrderId()));

			String tid = jsonNode.get("tid").asText();

			if (paymentRepository.existsByTid(tid)) {
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

			return PaymentResponseDto.builder()
				.status("READY")
				.message("결제 준비 완료")
				.paymentId(payment.getId())
				.userId(requestDto.getUserId())
				.orderId(requestDto.getOrderId())
				.amount(requestDto.getAmount())
				.paymentMethod(requestDto.getPaymentMethod())
				.createdAt(payment.getCreatedAt().toString())
				.nextRedirectUrl(jsonNode.get("next_redirect_pc_url").asText()) // Ensure this value is correctly set
				.build();
			// return jsonNode.get("next_redirect_pc_url").asText();

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

	@Transactional
	public PaymentResponseDto approveKakaoPayPayment(String pgToken, Long orderId, Long userId) {
		String url = "https://open-api.kakaopay.com/online/v1/payment/approve";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "SECRET_KEY " + secretKey);
		headers.set("Content-type", "application/json"); // No HttpMessageConverter for java.util.HashMap and content type "application/x-www-form-urlencoded"

		System.out.println(getTidByOrderId(orderId));
		System.out.println(orderId.toString());
		System.out.println(userId.toString());

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
	 * 토스페이 결제 준비(생성)
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
		params.put("productDesc", "토스 티셔츠");
		params.put("apiKey", tossClientKey);
		params.put("autoExecute", true);
		params.put("callbackVersion", "V2");
		params.put("resultCallback", tossResultCallback);
		params.put("retUrl", tossRetUrl);
		params.put("retCancelUrl", tossRetCancelUrl);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);
		System.out.println("params");
		System.out.println(params);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			// JSON 응답 디버깅
			System.out.println("Response Body: " + response.getBody());

			JsonNode jsonNode = objectMapper.readTree(response.getBody());

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
				return PaymentResponseDto.builder()
					.status("FAILED")
					.message("결제 준비 실패: 필요한 필드가 응답에 없습니다.")
					.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return PaymentResponseDto.builder()
				.status("FAILED")
				.message("결제 준비 실패")
				.build();
		}
	}

	/**
	 * 토스페이 결제 승인
	 *
	 * @param payToken 결제 고유 토큰
	 * @return 결제 응답 DTO
	 */
	@Transactional
	public PaymentResponseDto approveTossPayPayment(String payToken) {
		String url = "https://pay.toss.im/api/v2/execute";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> params = new HashMap<>();
		params.put("apiKey", tossSecretKey);
		params.put("payToken", payToken);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());

			Payment payment = paymentRepository.findByTid(payToken);
			if (payment == null) {
				throw new IllegalArgumentException("Invalid payToken: " + payToken);
			}

			payment.updateStatus(PaymentStatus.COMPLETED);

			return PaymentResponseDto.builder()
				.status("COMPLETED")
				.message("결제 완료")
				.paymentId(payment.getId())
				.userId(payment.getUser().getId())
				.orderId(payment.getOrder().getId())
				.amount(payment.getAmount())
				.paymentMethod(payment.getPaymentMethod().name())
				.createdAt(payment.getCreatedAt().toString())
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