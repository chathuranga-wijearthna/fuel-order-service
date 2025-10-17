package com.assignment.fuelorder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.assignment.fuelorder.dto.CreateOrderRequest;
import com.assignment.fuelorder.dto.OrderResponse;
import com.assignment.fuelorder.dto.SearchOrderRequest;
import com.assignment.fuelorder.dto.constants.OrderStatus;
import com.assignment.fuelorder.entity.FuelOrder;
import com.assignment.fuelorder.exception.CustomGlobalException;
import com.assignment.fuelorder.mapper.FuelOrderMapper;
import com.assignment.fuelorder.repo.FuelOrderRepository;
import com.assignment.fuelorder.service.impl.FuelOrderServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class FuelOrderServiceImplTest {

    @Mock
    private FuelOrderRepository fuelOrderRepository;

    @InjectMocks
    private FuelOrderServiceImpl service;

    @Test
    void submitRequest_shouldValidateAndSave_andReturnResponse() {
        
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);
        CreateOrderRequest req = new CreateOrderRequest(" N123 ", "KLAX", new BigDecimal("1.000"), start, end);

        FuelOrder saved = FuelOrderMapper.toEntity(req);
        saved.setId(UUID.randomUUID());
        when(fuelOrderRepository.existsOverlapping(eq("N123"), eq(start), eq(end), any(EnumSet.class))).thenReturn(false);
        when(fuelOrderRepository.save(any(FuelOrder.class))).thenReturn(saved);

        OrderResponse resp = service.submitRequest(req);

        assertThat(resp.id()).isEqualTo(saved.getId());
        assertThat(resp.tailNumber()).isEqualTo("N123");
        verify(fuelOrderRepository).existsOverlapping(eq("N123"), eq(start), eq(end), any());
        ArgumentCaptor<FuelOrder> captor = ArgumentCaptor.forClass(FuelOrder.class);
        verify(fuelOrderRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void submitRequest_shouldRejectInvalidWindow() {
        
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusMinutes(1);
        CreateOrderRequest req = new CreateOrderRequest("N1", "ABCD", new BigDecimal("1"), start, end);

        assertThatThrownBy(() -> service.submitRequest(req))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessageContaining("start must be before");
        verifyNoInteractions(fuelOrderRepository);
    }

    @Test
    void submitRequest_shouldRejectOverlap() {
        
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(1);
        CreateOrderRequest req = new CreateOrderRequest("N1", "ABCD", new BigDecimal("1"), start, end);
        when(fuelOrderRepository.existsOverlapping(eq("N1"), eq(start), eq(end), any())).thenReturn(true);

        assertThatThrownBy(() -> service.submitRequest(req))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessageContaining("already exists");
        verify(fuelOrderRepository, never()).save(any());
    }

    @Test
    void listOrder_shouldUsePageBounds_andMapResponses() {
        
        FuelOrder fo = new FuelOrder();
        fo.setId(UUID.randomUUID());
        fo.setTailNumber("N123");
        fo.setAirportIcao("KLAX");
        fo.setRequestedFuelVolume(new BigDecimal("5"));
        fo.setDeliveryWindowStart(LocalDateTime.now());
        fo.setDeliveryWindowEnd(LocalDateTime.now().plusHours(1));
        fo.setStatus(OrderStatus.PENDING);
        Page<FuelOrder> page = new PageImpl<>(List.of(fo), PageRequest.of(0, 1), 1);

        when(fuelOrderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<OrderResponse> resp = service.listOrder(new SearchOrderRequest("", null, null), -10, 0);

        assertThat(resp.getContent()).hasSize(1);
        assertThat(resp.getContent().get(0).tailNumber()).isEqualTo("N123");
        ArgumentCaptor<PageRequest> pageableCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(fuelOrderRepository).findAll(any(Specification.class), pageableCaptor.capture());
        PageRequest used = pageableCaptor.getValue();
        assertThat(used.getPageNumber()).isEqualTo(0);
        assertThat(used.getPageSize()).isEqualTo(1); // size corrected to min 1
    }

    @ParameterizedTest
    @MethodSource("allowedTransitions")
    void updateOrderStatus_shouldUpdate_whenTransitionAllowed(OrderStatus from, OrderStatus to) {
        
        UUID id = UUID.randomUUID();
        FuelOrder fo = new FuelOrder();
        fo.setId(id);
        fo.setStatus(from);
        when(fuelOrderRepository.findById(id)).thenReturn(Optional.of(fo));
        when(fuelOrderRepository.save(fo)).thenReturn(fo);

        OrderResponse resp = service.updateOrderStatus(id, to);

        assertThat(resp.status()).isEqualTo(to);
        assertThat(fo.getStatus()).isEqualTo(to);
    }

    static Stream<Arguments> allowedTransitions() {
        return Stream.of(Arguments.of(OrderStatus.PENDING, OrderStatus.CONFIRMED),
                Arguments.of(OrderStatus.PENDING, OrderStatus.CANCELLED),
                Arguments.of(OrderStatus.CONFIRMED, OrderStatus.COMPLETED)
        );
    }

    @ParameterizedTest
    @MethodSource("illegalTransitions")
    void updateOrderStatus_shouldThrow_whenTransitionIllegal(OrderStatus from, OrderStatus to) {
        
        UUID id = UUID.randomUUID();
        FuelOrder fo = new FuelOrder();
        fo.setId(id);
        fo.setStatus(from);
        when(fuelOrderRepository.findById(id)).thenReturn(Optional.of(fo));

        assertThatThrownBy(() -> service.updateOrderStatus(id, to))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessageContaining("Illegal status transition");
    }

    static Stream<Arguments> illegalTransitions() {
        return Stream.of(
                Arguments.of(OrderStatus.PENDING, OrderStatus.COMPLETED),
                Arguments.of(OrderStatus.CONFIRMED, OrderStatus.PENDING),
                Arguments.of(OrderStatus.COMPLETED, OrderStatus.PENDING),
                Arguments.of(OrderStatus.COMPLETED, OrderStatus.CONFIRMED),
                Arguments.of(OrderStatus.CANCELLED, OrderStatus.PENDING),
                Arguments.of(OrderStatus.CANCELLED, OrderStatus.CONFIRMED)
        );
    }

    @Test
    void updateOrderStatus_shouldThrow_whenNotFound() {
        
        UUID id = UUID.randomUUID();
        when(fuelOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateOrderStatus(id, OrderStatus.CONFIRMED))
                .isInstanceOf(CustomGlobalException.class)
                .hasMessageContaining("Order not found");
    }
}
