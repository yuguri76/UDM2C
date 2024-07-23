package com.example.livealone.payment.controller;

import com.example.livealone.payment.dto.PaymentRequestDto;
import com.example.livealone.payment.dto.PaymentResponseDto;
import com.example.livealone.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	/**
	 * 카카오페이 결제 준비
	 *
	 * @param requestDto 결제 요청 DTO
	 * @return 결제 응답 DTO
	 */
	@PostMapping("/kakao/process")
	public ResponseEntity<PaymentResponseDto> createKakaoPayReady(@RequestBody PaymentRequestDto requestDto) {
		PaymentResponseDto response = paymentService.createKakaoPayReady(requestDto);
		if (response.getStatus().equals("FAILED")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 토스페이 결제 준비
	 *
	 * @param requestDto 결제 요청 DTO
	 * @return 결제 응답 DTO
	 */
	@PostMapping("/toss/process")
	public ResponseEntity<PaymentResponseDto> createTossPayReady(@RequestBody PaymentRequestDto requestDto) {
		PaymentResponseDto response = paymentService.createTossPayReady(requestDto);
		if (response.getStatus().equals("FAILED")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 카카오페이 결제 승인
	 *
	 * @param pgToken 결제 승인 토큰
	 * @param orderId 주문 ID
	 * @param userId 사용자 ID
	 * @return 결제 응답 DTO
	 */
	@PostMapping("/approve")
	public ResponseEntity<PaymentResponseDto> approveKakaoPayPayment(@RequestParam String pgToken, @RequestParam Long orderId, @RequestParam Long userId) {
		PaymentResponseDto response = paymentService.approveKakaoPayPayment(pgToken, orderId, userId);
		if (response.getStatus().equals("FAILED")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * 결제 상태 조회
	 *
	 * @param paymentId 결제 ID
	 * @return 결제 응답 DTO
	 */
	@GetMapping("/{paymentId}/status")
	public ResponseEntity<PaymentResponseDto> getPaymentStatus(@PathVariable Long paymentId) {
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	/**
	 * 결제 취소
	 *
	 * @param paymentId 결제 ID
	 * @return 결제 응답 DTO
	 */
	@PostMapping("/{paymentId}/cancel")
	public ResponseEntity<PaymentResponseDto> cancelPayment(@PathVariable Long paymentId) {
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	/**
	 * 사용자 결제 내역 조회
	 *
	 * @param userId 사용자 ID
	 * @return 결제 내역 리스트
	 */
	@GetMapping("/user/{userId}/payment")
	public ResponseEntity<?> getUserPaymentHistory(@PathVariable Long userId) {
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
}
