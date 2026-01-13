package org.arispay.data;

public enum ActivityEventType {
    TRANSACTION_CREATED("Transaction Created"),
    TRANSACTION_UPDATED("Transaction Updated"),
    BULK_DISBURSEMENT("Bulk Disbursement Processed"),
    CLIENT_CREATED("Client Created"),
    CLIENT_UPDATED("Client Updated"),
    USER_UPDATED("User Profile Updated"),
    ACCOUNT_LINKED("Bank Account Linked");

    private final String displayName;

    ActivityEventType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
