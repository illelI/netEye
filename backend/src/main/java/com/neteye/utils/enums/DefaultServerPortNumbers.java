package com.neteye.utils.enums;

public enum DefaultServerPortNumbers {
    FTP(21),
    TELNET(23),
    SMTP(25),
    HTTP(80),
    HTTP8080(8080),
    HTTPS(443),
    POP3(110),
    IMAP(143);

    private final int portNumber;
    DefaultServerPortNumbers(final int portNumber) {
        this.portNumber = portNumber;
    }
    public int getPortNumber() {
        return portNumber;
    }
}
