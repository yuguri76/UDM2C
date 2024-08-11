package com.example.livealone.alert.service;

import com.example.livealone.broadcast.dto.BroadcastTitleResponseDto;
import com.example.livealone.global.dto.SocketMessageDto;
import com.example.livealone.global.entity.SocketMessageType;
import com.example.livealone.order.dto.OrderQuantityResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
  private final SimpMessagingTemplate messagingTemplate;
  private final ObjectMapper objectMapper;
  public void sendBroadcastStartAlert(BroadcastTitleResponseDto broadcastTitleResponseDto) throws JsonProcessingException {
    String messageJSON = objectMapper.writeValueAsString(broadcastTitleResponseDto);
    SocketMessageDto socketMessageDto = new SocketMessageDto(SocketMessageType.ALERT_BROADCAST_START, "server", messageJSON);

    messagingTemplate.convertAndSend("/queue/alert",socketMessageDto);
  }


  public void sendStockQuantity(OrderQuantityResponseDto orderQuantityResponseDto)
      throws JsonProcessingException {
    String messageJSON = objectMapper.writeValueAsString(orderQuantityResponseDto);
    SocketMessageDto socketMessageDto = new SocketMessageDto(SocketMessageType.ALERT_ALMOST_SOLD_OUT, "server", messageJSON);

    messagingTemplate.convertAndSend("/queue/alert",socketMessageDto);
  }

  public void sendSoldOutAlert() {
    SocketMessageDto socketMessageDto = new SocketMessageDto(SocketMessageType.ALERT_SOLD_OUT, "server", null);

    messagingTemplate.convertAndSend("/queue/alert",socketMessageDto);
  }

}
