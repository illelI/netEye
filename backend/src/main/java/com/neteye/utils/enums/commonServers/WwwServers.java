package com.neteye.utils.enums.commonServers;

public enum WwwServers {
    APACHE("Apache"),
    NGINX("nginx"),
    MICROSOFT_ISS("Microsoft-IIS"),
    LITESPEED("LiteSpeed"),
    CADDY("Caddy"),
    CHEROKEE("Cherokee"),
    TOMCAT("Apache-Coyote");

    private final String appName;

    WwwServers(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }
}
