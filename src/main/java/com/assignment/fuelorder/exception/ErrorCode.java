package com.assignment.fuelorder.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ErrorCode {

    // Order related errors
    ORDER_NOT_FOUND("ERR_FO_ORD_01", "Order not found"),
    ILLEGAL_STATUS_TRANSITION("ERR_FO_ORD_02", "Illegal status transition: %s -> %s"),
    DELIVERY_WINDOW_INVALID("ERR_FO_ORD_03", "Delivery window start must be before delivery window end"),
    DELIVERY_WINDOW_OVERLAP("ERR_FO_ORD_04", "An order for this tailNumber already exists in the requested delivery window"),

    // Auth/User related errors
    EMAIL_ALREADY_REGISTERED("ERR_FO_AUTH_01", "Email already registered"),
    BAD_CREDENTIALS("ERR_FO_AUTH_02", "Bad credentials"),

    ;
    private final String code;
    private final String description;
}
