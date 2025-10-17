package com.assignment.fuelorder.web;

import com.assignment.fuelorder.dto.CreateOrderRequest;
import com.assignment.fuelorder.dto.OrderResponse;
import com.assignment.fuelorder.dto.PageResponse;
import com.assignment.fuelorder.dto.SearchOrderRequest;
import com.assignment.fuelorder.dto.UpdateStatusRequest;
import com.assignment.fuelorder.service.FuelOrderService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class FuelOrderController {

    private final FuelOrderService fuelOrderService;

    @PreAuthorize("hasRole('AIRCRAFT_OPERATOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse submitRequest(@Valid @RequestBody CreateOrderRequest request) {
        log.debug("Received fuel order request: {}", request);
        return fuelOrderService.submitRequest(request);
    }

    @PreAuthorize("hasRole('OPERATIONS_MANAGER')")
    @PostMapping("list")
    public PageResponse<OrderResponse> listOrder(@RequestBody SearchOrderRequest searchOrderRequest, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Listing orders according to request: {}", searchOrderRequest);

        var orderResponses = fuelOrderService.listOrder(searchOrderRequest, page, size);

        return new PageResponse<>(orderResponses.getContent(), orderResponses.getNumber(), orderResponses.getSize(),
                orderResponses.getTotalElements(),
                orderResponses.getTotalPages());
    }

    @PreAuthorize("hasRole('OPERATIONS_MANAGER')")
    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateStatusRequest req) {
        log.debug("Received order update request: {}", req);
        return fuelOrderService.updateOrderStatus(id, req.status());
    }

}
