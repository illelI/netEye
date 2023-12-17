package com.neteye.utils;

import com.neteye.utils.enums.PortNumbersEnum;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.smtp.SMTPClient;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@Log4j2
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
            pop3Client.setDefaultTimeout(700);
            pop3Client.setSoTimeout(700);
            pop3Client.connect(ip.toString());
            return pop3Client.getReplyString();
        } catch (Exception e) {
        }
        return "";
    }

    private static String getImapBanner(IpAddress ip) {
        try {
            IMAPClient imapClient = new IMAPClient();
            imapClient.setDefaultTimeout(700);
            imapClient.setSoTimeout(700);
            imapClient.connect(ip.toString());
            return imapClient.getReplyString();
        } catch (Exception e) {
        }
        return "";
    }

    private static String getTelnetBanner(IpAddress ip) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.setDefaultTimeout(700);
            ftpClient.setSoTimeout(700);
            ftpClient.connect(ip.toString());
            return ftpClient.getReplyString();
        } catch (Exception e) {
        }
        return "";
    }

    private static String getSmtpBanner(IpAddress ip) {
        try {
            SMTPClient smtpClient = new SMTPClient();
            smtpClient.setDefaultTimeout(700);
            smtpClient.setSoTimeout(700);
            smtpClient.connect(ip.toString(), 25);
            return smtpClient.getReplyString();
        } catch (Exception e) {
        }
        return "";
    }
    private static String getFtpBanner(IpAddress ip) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.setDefaultTimeout(700);
            ftpClient.setSoTimeout(700);
            ftpClient.connect(ip.toString(), 21);
            return ftpClient.getReplyString();
        } catch (Exception e) {
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
            conn.setReadTimeout(700);
            Map<String, List<String>> map = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if(entry.getKey() == null) {
                    message.append(entry.getValue().getFirst()).append("\n");
                }
                else {
                    message.append(entry.getKey()).append(": ").append(entry.getValue().getFirst()).append("\n");
                }
            }
            return message.toString();
        } catch (Exception e) {
        }
        return "";
    }
}
