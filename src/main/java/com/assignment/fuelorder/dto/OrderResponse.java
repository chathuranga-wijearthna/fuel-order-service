package com.assignment.fuelorder.dto;

import com.assignment.fuelorder.dto.constants.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String tailNumber,
        String airportIcao,
        BigDecimal requestedFuelVolume,
        LocalDateTime deliveryWindowStart,
        LocalDateTime deliveryWindowEnd,
        OrderStatus status,
        LocalDateTime createdAt
) {

}
