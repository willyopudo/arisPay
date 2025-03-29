package org.arispay.enums;

public enum CurrentPlan {
    BASIC("basic"),
    ENTERPRISE("enterprise"),
    STANDARD("standard"),
    SPECIAL("special");

    private final String value;

    CurrentPlan(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CurrentPlan fromString(String text) {
        for (CurrentPlan plan : CurrentPlan.values()) {
            if (plan.value.equalsIgnoreCase(text)) {
                return plan;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
