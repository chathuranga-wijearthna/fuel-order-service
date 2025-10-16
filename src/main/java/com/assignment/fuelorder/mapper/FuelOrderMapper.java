package com.assignment.fuelorder.mapper;

import com.assignment.fuelorder.dto.CreateOrderRequest;
import com.assignment.fuelorder.dto.OrderResponse;
import com.assignment.fuelorder.dto.constants.OrderStatus;
import com.assignment.fuelorder.entity.FuelOrder;

public class FuelOrderMapper {


    public static FuelOrder toEntity(CreateOrderRequest request) {

        FuelOrder fuelOrder = new FuelOrder();

        fuelOrder.setTailNumber(request.tailNumber().trim());
        fuelOrder.setAirportIcao(request.airportIcao());
        fuelOrder.setRequestedFuelVolume(request.requestedFuelVolume());
        fuelOrder.setDeliveryWindowStart(request.deliveryWindowStart());
        fuelOrder.setDeliveryWindowEnd(request.deliveryWindowEnd());
        fuelOrder.setStatus(OrderStatus.PENDING);

        return fuelOrder;
    }

    public static OrderResponse toResponse(FuelOrder entity) {
        return new OrderResponse(
                entity.getId(), entity.getTailNumber(), entity.getAirportIcao(), entity.getRequestedFuelVolume(),
                entity.getDeliveryWindowStart(), entity.getDeliveryWindowEnd(), entity.getStatus(), entity.getCreatedAt());
    }
}
