package com.neteye.utils.enums.commonServers;

public enum MailServers {
    POSTFIX("Postfix"),
    SENDMAIL("Sendmail"),
    MICROSOFT("Microsoft"),
    EXIM("Exim"),
    QMAIL("Qmail"),
    DOVECOT("Dovecot");

    private final String appName;

    MailServers(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }
}
