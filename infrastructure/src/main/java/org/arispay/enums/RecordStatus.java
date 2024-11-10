package org.arispay.enums;

import java.util.HashMap;
import java.util.Map;

public enum RecordStatus {
    NEW(0), ACTIVE(1), INACTIVE(2), DELETED(3);
    private final int statusIntValue;
    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<Integer, RecordStatus> lookup = new HashMap<Integer, RecordStatus>();

    static {
        for (RecordStatus d : RecordStatus.values()) {
            lookup.put(d.getIntValue(), d);
        }
    }
    RecordStatus(int i) {
        this.statusIntValue = i;
    }

    private int getIntValue() {
        return statusIntValue;
    }
    public static RecordStatus getByIntValue(int intValue) {
        return lookup.get(intValue);
    }
}
