package com.example.livealone.payment.service;

import com.example.livealone.payment.dto.PaymentRequestDto;
import com.example.livealone.payment.dto.PaymentResponseDto;
import com.example.livealone.payment.entity.Payment;
import com.example.livealone.payment.entity.PaymentMethod;
import com.example.livealone.payment.entity.PaymentStatus;
import com.example.livealone.payment.repository.PaymentRepository;
import com.example.livealone.user.repository.UserRepository;
import com.example.livealone.order.repository.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

	@Value("dev")
	private String secretKey;

	@Value("https://yourapp.com/payment/success")
	private String approvalUrl;

	@Value("https://yourapp.com/payment/cancel")
	private String cancelUrl;

	@Value("https://yourapp.com/payment/fail")
	private String failUrl;

	@Value("test_ck_kYG57Eba3GPyQ4zAdxQkVpWDOxmA")
	private String tossClientKey;

	@Value("test_sk_jExPeJWYVQ1ekabzNRlxV49R5gvN")
	private String tossSecretKey;

	@Value("https://yourapp.com/payment/success")
	private String tossRetUrl;

	@Value("https://yourapp.com/payment/cancel")
	private String tossRetCancelUrl;

	@Value("https://yourapp.com/payment/callback")
	private String tossResultCallback;

	/**
	 * 카카오페이 결제 준비
	 *
	 * @param requestDto 결제 요청 DTO
	 * @return 결제 응답 DTO
	 */
	public PaymentResponseDto createKakaoPayReady(PaymentRequestDto requestDto) {
		String url = "https://kapi.kakao.com/v1/payment/ready";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "KakaoAK " + secretKey);

		Map<String, String> params = new HashMap<>();
		params.put("cid", cid);
		params.put("partner_order_id", requestDto.getOrderId().toString());
		params.put("partner_user_id", requestDto.getUserId().toString());
		params.put("item_name", "Order " + requestDto.getOrderId());
		params.put("quantity", "1");
		params.put("total_amount", String.valueOf(requestDto.getAmount()));
		params.put("vat_amount", String.valueOf(requestDto.getAmount() / 11));
		params.put("tax_free_amount", "0");
		params.put("approval_url", approvalUrl);
		params.put("cancel_url", cancelUrl);
		params.put("fail_url", failUrl);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());

			Payment payment = Payment.builder()
				.user(userRepository.findById(requestDto.getUserId()).orElseThrow())
				.order(orderRepository.findById(requestDto.getOrderId()).orElseThrow())
				.amount(requestDto.getAmount())
				.paymentMethod(PaymentMethod.KAKAO_PAY)
				.status(PaymentStatus.REQUESTED)
				.tid(jsonNode.get("tid").asText())
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
	 * 카카오페이 결제 승인
	 *
	 * @param pgToken 결제 승인 토큰
	 * @param orderId 주문 ID
	 * @param userId 사용자 ID
	 * @return 결제 응답 DTO
	 */
	public PaymentResponseDto approveKakaoPayPayment(String pgToken, Long orderId, Long userId) {
		String url = "https://kapi.kakao.com/v1/payment/approve";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "KakaoAK " + secretKey);

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

			Payment payment = Payment.builder()
				.user(userRepository.findById(requestDto.getUserId()).orElseThrow())
				.order(orderRepository.findById(requestDto.getOrderId()).orElseThrow())
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
