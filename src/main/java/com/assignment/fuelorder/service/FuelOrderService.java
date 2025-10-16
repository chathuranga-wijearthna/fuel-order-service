package com.assignment.fuelorder.service;

import com.assignment.fuelorder.dto.CreateOrderRequest;
import com.assignment.fuelorder.dto.OrderResponse;
import com.assignment.fuelorder.dto.SearchOrderRequest;
import com.assignment.fuelorder.dto.constants.OrderStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;


public interface FuelOrderService {

    OrderResponse submitRequest(CreateOrderRequest req);

    Page<OrderResponse> listOrder(SearchOrderRequest searchRequest, int page, int size);

    OrderResponse updateOrderStatus(UUID id, OrderStatus orderStatus);
}
