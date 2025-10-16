package com.assignment.fuelorder.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateOrderRequest(
        @NotBlank String tailNumber,
        @NotBlank @Pattern(regexp = "[A-Za-z]{4}") String airportIcao,
        @NotNull @DecimalMin(value = "1000.0") BigDecimal requestedFuelVolume,
        @NotNull LocalDateTime deliveryWindowStart,
        @NotNull LocalDateTime deliveryWindowEnd
) {

}
