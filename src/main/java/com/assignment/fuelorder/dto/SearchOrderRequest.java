package com.assignment.fuelorder.dto;

import com.assignment.fuelorder.dto.constants.OrderStatus;
import jakarta.validation.constraints.Pattern;

public record SearchOrderRequest(
        String tailNumber,
        @Pattern(regexp = "[A-Za-z]{4}") String airportIcao,
        OrderStatus status
) {


}
