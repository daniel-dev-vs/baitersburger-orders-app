package br.com.fiap.baitersburger_orders.domain.entities;

public enum OrderStatus {

    REQUESTED("REQUESTED"),
    RECEIVED("RECEIVED"),
    PREPARING("PREPARING"),
    READY("READY"),
    DELIVERED("DELIVERED");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatus fromValue(String value) throws IllegalArgumentException {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Order status does not exist: " + value);
    }
}
