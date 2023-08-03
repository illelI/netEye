package com.neteye.utils;

import com.neteye.utils.enums.PortNumbersEnum;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.smtp.SMTPClient;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


public class Identify {
    private Identify() {}
    public static String fetchInfo(PortNumbersEnum port, IpAddress ip) {
        return switch (port) {
            case FTP -> getFtpBanner(ip);
            case SSH -> getSshBanner(ip);
            case TELNET -> getTelnetBanner(ip);
            case SMTP -> getSmtpBanner(ip);
            case DNS -> getDnsBanner(ip);
            case HTTP, HTTP8080, HTTPS -> getHttpBanner(port, ip);
            case POP3 -> getPop3Banner(ip);
            case IMAP -> getImapBanner(ip);
            case SNMP -> getSnmpBanner(ip);
        };

    }

    private static String getSshBanner(IpAddress ip) {

        return "";
    }

    private static String getDnsBanner(IpAddress ip) {
        return "";
    }

    private static String getSnmpBanner(IpAddress ip) {
        return "";
    }

    private static String getPop3Banner(IpAddress ip) {
        try {
            POP3Client pop3Client = new POP3Client();
            pop3Client.connect(ip.toString());
            return pop3Client.getReplyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getImapBanner(IpAddress ip) {
        try {
            IMAPClient imapClient = new IMAPClient();
            imapClient.connect(ip.toString());
            return imapClient.getReplyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getTelnetBanner(IpAddress ip) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(ip.toString());
            return ftpClient.getReplyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getSmtpBanner(IpAddress ip) {
        try {
            SMTPClient smtpClient = new SMTPClient();
            smtpClient.connect(ip.toString(), 25);
            return smtpClient.getReplyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String getFtpBanner(IpAddress ip) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(ip.toString(), 21);
            return ftpClient.getReplyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getHttpBanner(PortNumbersEnum port, IpAddress ip) {
        try {
            String urlString = "http://" + ip;
            StringBuilder message = new StringBuilder();
            if(port.getValue() == 443) {
                urlString = "https://" + ip;
            }
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            Map<String, List<String>> map = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if(entry.getKey() == null) {
                    message.append(entry.getValue().get(0)).append("\n");
                }
                else {
                    message.append(entry.getKey()).append(": ").append(entry.getValue().get(0)).append("\n");
                }
            }
            return message.toString();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return "";
    }
}
