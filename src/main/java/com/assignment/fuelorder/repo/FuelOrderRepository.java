package com.assignment.fuelorder.repo;

import com.assignment.fuelorder.entity.FuelOrder;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelOrderRepository extends JpaRepository<FuelOrder, UUID>, JpaSpecificationExecutor<FuelOrder> {

}
