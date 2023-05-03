package com.neteye.utils.enums;

public enum PORTNUMBERS {
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
    SNTP(161),
    MYSQL(3306),
    POSTGRESQL(5432);
    private final int value;
    PORTNUMBERS(final int newValue) {
        value = newValue;
    }
    public int getValue() {
        return value;
    }
}
