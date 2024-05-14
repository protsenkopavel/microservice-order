package net.protsenko.microserviceorder.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {

    CREATED("CREATED"),
    DELIVERING("DELIVERING"),
    DELIVERED("DELIVERED"),
    CANCELLED("CANCELLED"),
    DELETED("DELETED"),
    RETURNING("RETURNING"),
    RETURNED("RETURNED");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
