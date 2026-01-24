package org.arispay.enums;

import java.util.HashMap;
import java.util.Map;

public enum RecordStatus {
    ACTIVE("active"), INACTIVE("inactive"), PENDING("pending");
    private final String value;

    RecordStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static RecordStatus fromString(String text) {
        for (RecordStatus plan : RecordStatus.values()) {
            if (plan.value.equalsIgnoreCase(text)) {
                return plan;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
