package com.assignment.fuelorder.service.impl;

import com.assignment.fuelorder.dto.CreateOrderRequest;
import com.assignment.fuelorder.dto.OrderResponse;
import com.assignment.fuelorder.dto.SearchOrderRequest;
import com.assignment.fuelorder.dto.constants.OrderStatus;
import com.assignment.fuelorder.entity.FuelOrder;
import com.assignment.fuelorder.mapper.FuelOrderMapper;
import com.assignment.fuelorder.repo.FuelOrderRepository;
import com.assignment.fuelorder.repo.specification.OrderSpecification;
import com.assignment.fuelorder.service.FuelOrderService;
import java.util.EnumSet;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FuelOrderServiceImpl implements FuelOrderService {

    private final FuelOrderRepository fuelOrderRepository;

    @Override
    @Transactional
    public OrderResponse submitRequest(CreateOrderRequest request) {

        validateRequest(request);

        FuelOrder fuelOrder = fuelOrderRepository.save(FuelOrderMapper.toEntity(request));

        return FuelOrderMapper.toResponse(fuelOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> listOrder(SearchOrderRequest searchRequest, int page, int size) {

        var pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));

        Page<FuelOrder> items = fuelOrderRepository.findAll(OrderSpecification.getOrderSpecification(searchRequest), pageable);

        return items.map(FuelOrderMapper::toResponse);
    }

    @Override
    public OrderResponse updateOrderStatus(UUID id, OrderStatus orderStatus) {

        log.info("Fetching order for id {}", id);
        FuelOrder fuelOrder = fuelOrderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!isAllowedTransition(fuelOrder.getStatus(), orderStatus)) {
            throw new IllegalStateException("Illegal status transition: " + fuelOrder.getStatus() + " -> " + orderStatus);
        }

        fuelOrder.setStatus(orderStatus);

        fuelOrder = fuelOrderRepository.save(fuelOrder);

        return FuelOrderMapper.toResponse(fuelOrder);
    }

    private boolean isAllowedTransition(OrderStatus from, OrderStatus to) {

        log.debug("Validating transition from {} to {}", from, to);

        return switch (from) {
            case PENDING -> to == OrderStatus.CONFIRMED || to == OrderStatus.CANCELLED;
            case CONFIRMED -> to == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }

    private void validateRequest(CreateOrderRequest request) {

        log.debug("Validating request: {}", request);

        if (!request.deliveryWindowStart().isBefore(request.deliveryWindowEnd())) {
            log.error("Invalid delivery window");
            throw new IllegalArgumentException("Delivery window start must be before delivery window end");
        }

        //Validate an overlap time window
        String tail = request.tailNumber().trim();
        var active = EnumSet.of(OrderStatus.PENDING, OrderStatus.CONFIRMED);
        boolean overlaps = fuelOrderRepository.existsOverlapping(tail, request.deliveryWindowStart(), request.deliveryWindowEnd(), active);

        if (overlaps) {
            log.error("Delivery window overlaps");
            throw new IllegalStateException("An order for this tailNumber already exists in the requested delivery window");
        }

    }


}
