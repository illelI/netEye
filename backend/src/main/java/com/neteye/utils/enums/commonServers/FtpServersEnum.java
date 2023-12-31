package com.neteye.utils.enums.commonServers;

public enum FtpServersEnum {
    VSFTPD("vsftpd"),
    PROFTPD("ProFTPD"),
    PURE_FTPD("Pure-FTPd"),
    FILEZILLA_SERVER("FileZilla Server"),
    MICROSOFT_IIS("Microsoft FTP Server"),
    CERBERUS("Cerberus FTP Server"),
    PROVIDE("ProVide"),
    CRUSHFTP("CrushFTP");

    private final String serverName;

    FtpServersEnum(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

}
