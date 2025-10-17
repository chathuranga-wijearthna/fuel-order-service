package com.assignment.fuelorder.entity;

import com.assignment.fuelorder.dto.constants.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_fuel_orders")
@Getter
@Setter
public class FuelOrder extends AuditEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String tailNumber;

    @Column(nullable = false, length = 4)
    private String airportIcao;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal requestedFuelVolume;

    @Column(nullable = false)
    private LocalDateTime deliveryWindowStart;

    @Column(nullable = false)
    private LocalDateTime deliveryWindowEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private OrderStatus status = OrderStatus.PENDING;

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (status == null) {
            status = OrderStatus.PENDING;
        }
        if (airportIcao != null) {
            airportIcao = airportIcao.toUpperCase();
        }
    }

    @PreUpdate
    public void onUpdate() {
        if (airportIcao != null) {
            airportIcao = airportIcao.toUpperCase();
        }
    }
}
