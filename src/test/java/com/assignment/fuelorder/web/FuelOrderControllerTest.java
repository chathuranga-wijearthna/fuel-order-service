package com.assignment.fuelorder.web;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.assignment.fuelorder.dto.CreateOrderRequest;
import com.assignment.fuelorder.dto.OrderResponse;
import com.assignment.fuelorder.dto.SearchOrderRequest;
import com.assignment.fuelorder.dto.UpdateStatusRequest;
import com.assignment.fuelorder.dto.constants.OrderStatus;
import com.assignment.fuelorder.service.FuelOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class FuelOrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FuelOrderService fuelOrderService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        FuelOrderController controller = new FuelOrderController(fuelOrderService);
        JavaTimeModule module = new JavaTimeModule();

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(module);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void submitRequest_shouldReturn201_andBody() throws Exception {
        
        UUID id = UUID.randomUUID();

        OrderResponse resp = new OrderResponse(id, "N1", "KLAX", new BigDecimal("1.000"),
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), OrderStatus.PENDING, LocalDateTime.now());

        when(fuelOrderService.submitRequest(any(CreateOrderRequest.class))).thenReturn(resp);

        CreateOrderRequest req = new CreateOrderRequest("N1", "KLAX", new BigDecimal("1000.00"),
                LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        mockMvc.perform(post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(id.toString())));
    }

    @Test
    void submitRequest_shouldReturn400_onValidationError() throws Exception {

        CreateOrderRequest req = new CreateOrderRequest("N1", "KLA", new BigDecimal("1.000"),
                LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        mockMvc.perform(post("/api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed {airportIcao=must match \"[A-Za-z]{4}\", requestedFuelVolume=must be greater than or equal to 1000.0}")));
    }

    @Test
    void listOrder_shouldReturnPageResponse() throws Exception {
        
        OrderResponse item = new OrderResponse(UUID.randomUUID(), "N1", "KLAX", new BigDecimal("1"),
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), OrderStatus.PENDING, LocalDateTime.now());

        Page<OrderResponse> page = new PageImpl<>(List.of(item), PageRequest.of(0, 10), 1);

        when(fuelOrderService.listOrder(any(SearchOrderRequest.class), eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(post("/api/v1/order/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tailNumber", is("N1")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    void updateStatus_shouldReturnUpdatedResponse() throws Exception {
        
        UUID id = UUID.randomUUID();

        OrderResponse resp = new OrderResponse(id, "N1", "KLAX", new BigDecimal("1"),
                LocalDateTime.now(), LocalDateTime.now().plusHours(1), OrderStatus.CONFIRMED, LocalDateTime.now());

        when(fuelOrderService.updateOrderStatus(eq(id), eq(OrderStatus.CONFIRMED))).thenReturn(resp);

        mockMvc.perform(patch("/api/v1/order/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateStatusRequest(OrderStatus.CONFIRMED))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CONFIRMED")));
    }
}
