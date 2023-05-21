package com.neteye.utils.enums;

public enum PortNumbersEnum {
    FTP(21),
    SSH(22),
    TELNET(23),
    SMTP(25),
    DNS(53),
    HTTP(80),
    HTTP8080(8080),
    HTTPS(443),
    POP3(110),
    IMAP(143),
    SNMP(161);
    private final int value;
    PortNumbersEnum(final int newValue) {
        value = newValue;
    }
    public int getValue() {
        return value;
    }
}
