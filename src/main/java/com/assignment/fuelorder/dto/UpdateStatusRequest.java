package com.assignment.fuelorder.dto;

import com.assignment.fuelorder.dto.constants.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull OrderStatus status) {

}
