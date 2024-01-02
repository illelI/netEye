package com.neteye.utils.enums.commonServers;

public enum FtpServers {
    VSFTPD("vsftpd"),
    PROFTPD("ProFTPD"),
    PURE_FTPD("Pure-FTPd"),
    FILEZILLA_SERVER("FileZilla Server"),
    MICROSOFT_IIS("Microsoft FTP Server"),
    CERBERUS("Cerberus FTP Server"),
    PROVIDE("ProVide"),
    CRUSHFTP("CrushFTP");

    private final String appName;

    FtpServers(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

}
