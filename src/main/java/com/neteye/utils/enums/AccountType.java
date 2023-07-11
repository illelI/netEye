package com.neteye.utils.enums;

public enum AccountType {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;
    AccountType(final String newValue) {
        value = newValue;
    }
    public String getValue() {
        return value;
    }
}
