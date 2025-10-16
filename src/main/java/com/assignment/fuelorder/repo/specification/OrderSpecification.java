package com.assignment.fuelorder.repo.specification;

import com.assignment.fuelorder.dto.SearchOrderRequest;
import com.assignment.fuelorder.dto.constants.OrderConstants;
import com.assignment.fuelorder.dto.constants.OrderStatus;
import com.assignment.fuelorder.entity.FuelOrder;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class OrderSpecification {

    private OrderSpecification() {
    }

    public static Specification<FuelOrder> getOrderSpecification(SearchOrderRequest searchDto) {
        return Specification.allOf(
                withIsDeletedFalse(),
                withTailNumber(searchDto.tailNumber()),
                withAirportIcao(searchDto.airportIcao()),
                withStatus(searchDto.status())
        );
    }

    private static Specification<FuelOrder> withIsDeletedFalse() {
        return (root, query, cb) -> cb.isFalse(root.get(OrderConstants.IS_DELETED));
    }

    private static Specification<FuelOrder> withTailNumber(String tailNumber) {
        if (!StringUtils.hasText(tailNumber)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(OrderConstants.TAIL_NUMBER), tailNumber);
    }

    private static Specification<FuelOrder> withAirportIcao(String airportIcao) {
        if (!StringUtils.hasText(airportIcao)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(OrderConstants.AIRPORT_ICAO), airportIcao.toUpperCase());
    }

    private static Specification<FuelOrder> withStatus(OrderStatus status) {
        if (Objects.isNull(status)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(OrderConstants.STATUS), status);
    }

}
