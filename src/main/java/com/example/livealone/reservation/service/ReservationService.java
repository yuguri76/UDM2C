package com.example.livealone.reservation.service;

import com.example.livealone.reservation.dto.ReservationRequestDto;
import com.example.livealone.reservation.dto.ReservationResponseDto;
import com.example.livealone.broadcast.dto.ReservationStateResponseDto;
import com.example.livealone.reservation.entity.Reservations;
import com.example.livealone.reservation.mapper.ReservationMapper;
import com.example.livealone.reservation.repository.ReservationRepository;
import com.example.livealone.global.aop.DistributedLock;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final MessageSource messageSource;

  private static final int BROADCAST_AFTER_STARTING = 20;

  @DistributedLock(key = "'createReservation-' + #user.getId()")
  public ReservationResponseDto createReservation(ReservationRequestDto requestDto, User user) {
    if(!reservationRepository.findByAirTimeGreaterThanEqualAndStreamer(LocalDateTime.now().minusMinutes(BROADCAST_AFTER_STARTING), user).isEmpty()) {
      throw new CustomException(messageSource.getMessage(
          "limit.reservation.term",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.FORBIDDEN);
    }

    if (reservationRepository.findByAirTime(requestDto.getAirtime()).isPresent()) {
      throw new CustomException(messageSource.getMessage(
          "already.occupied.reservation",
          null,
          CustomException.DEFAULT_ERROR_MESSAGE,
          Locale.getDefault()
      ), HttpStatus.FORBIDDEN);
    }

    return ReservationMapper.toReservationResponseCodeDto(reservationRepository
        .save(ReservationMapper.toReservation(requestDto, user)));
  }

  public List<ReservationStateResponseDto> getReservations(LocalDate date) {
    LocalDateTime currentBroadcastTime = LocalDateTime.now().minusMinutes(BROADCAST_AFTER_STARTING);

    List<LocalDateTime> reservedTimes = reservationRepository
        .findByAirTimeBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));

    List<ReservationStateResponseDto> responseDtoList = new ArrayList<>();

    for(int hour = 0; hour < 24; hour++) {
      for(int minute : new int[] {0, 20, 40}) {
        LocalDateTime timeSlot = LocalDateTime.of(date, LocalTime.of(hour, minute));
        if(timeSlot.isBefore(currentBroadcastTime)) {
          continue;
        }

        ReservationStateResponseDto dto = ReservationStateResponseDto.builder()
            .time(timeSlot.toLocalTime())
            .isReserved(reservedTimes.contains(timeSlot))
            .build();

        responseDtoList.add(dto);
      }
    }

    return responseDtoList;
  }

  public Reservations findReservation(User user) {
    LocalDateTime now = ZonedDateTime.now().toLocalDateTime();

    return reservationRepository
        .findByAirTimeBetweenAndStreamer(now.minusMinutes(BROADCAST_AFTER_STARTING), now, user)
        .orElseThrow(
            () -> new CustomException(messageSource.getMessage(
                "reservation.not.found",
                null,
                CustomException.DEFAULT_ERROR_MESSAGE,
                Locale.getDefault()
            ), HttpStatus.NOT_FOUND)
        );
  }
}
