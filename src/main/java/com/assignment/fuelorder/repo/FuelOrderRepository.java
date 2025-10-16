package com.assignment.fuelorder.repo;

import com.assignment.fuelorder.dto.constants.OrderStatus;
import com.assignment.fuelorder.entity.FuelOrder;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelOrderRepository extends JpaRepository<FuelOrder, UUID>, JpaSpecificationExecutor<FuelOrder> {

    @Query("""
              select (count(o) > 0) from FuelOrder o
              where upper(o.tailNumber) = upper(:tail)
                and (:start < o.deliveryWindowEnd and :end > o.deliveryWindowStart)
                and o.status in :statuses
            """)
    boolean existsOverlapping(@Param("tail") String tail, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
            @Param("statuses") Set<OrderStatus> statuses);

}
