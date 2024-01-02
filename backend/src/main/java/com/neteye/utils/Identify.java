package com.neteye.utils;

import com.neteye.utils.enums.commonServers.FtpServers;
import com.neteye.utils.enums.commonServers.SmtpServers;
import com.neteye.utils.misc.ServiceInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

@Log4j2
public class Identify {
    private static final int CONNECTION_TIMEOUT = 60000;
    private static final int OPENED_CONNECTION_TIMEOUT = 60000;

    private Identify() {}
    public static ServiceInfo fetchPortInfo(ServiceInfo info) {
        return switch (info.getPort()) {
            case FTP -> getFtpBanner(info);
            case SSH -> getSshBanner(info);
            case TELNET -> getTelnetBanner(info);
            case SMTP -> getSmtpBanner(info);
            case DNS -> getDnsBanner(info);
            case HTTP, HTTP8080, HTTPS -> getHttpBanner(info);
            case POP3 -> getPop3Banner(info);
            case IMAP -> getImapBanner(info);
            case SNMP -> getSnmpBanner(info);
        };
    }

    private static ServiceInfo getSshBanner(ServiceInfo info) {

        return info;
    }

    private static ServiceInfo getDnsBanner(ServiceInfo info) {
        return info;
    }

    private static ServiceInfo getSnmpBanner(ServiceInfo info) {
        return info;
    }

    private static ServiceInfo getPop3Banner(ServiceInfo info) {
        try {
            POP3Client pop3Client = new POP3Client();
            pop3Client.setDefaultTimeout(CONNECTION_TIMEOUT);
            pop3Client.connect(info.getIp());
            info.setInfo(pop3Client.getReplyString());
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static ServiceInfo getImapBanner(ServiceInfo info) {
        try {
            IMAPClient imapClient = new IMAPClient();
            imapClient.setDefaultTimeout(CONNECTION_TIMEOUT);
            imapClient.connect(info.getIp());
            info.setInfo(imapClient.getReplyString());
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static ServiceInfo getTelnetBanner(ServiceInfo info) {
        try {
            TelnetClient telnetClient = new TelnetClient();
            telnetClient.setDefaultTimeout(CONNECTION_TIMEOUT);
            telnetClient.connect(info.getIp());

            InputStream is = telnetClient.getInputStream();

            byte[] buffer = new byte[1024];
            int bytesRead = is.read(buffer);
            info.setInfo(new String(buffer, 0, bytesRead));
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static ServiceInfo getSmtpBanner(ServiceInfo info) {
        try {
            SMTPClient smtpClient = new SMTPClient();
            smtpClient.setDefaultTimeout(CONNECTION_TIMEOUT);

            smtpClient.connect(info.getIp());
            info.setInfo(smtpClient.getReplyString());
            checkMostPopularSmtpServers(info);
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static void checkMostPopularSmtpServers(ServiceInfo info) {
        for (SmtpServers server : SmtpServers.values()) {
            if (info.getInfo().contains(server.getAppName())) {
                info.setAppName(server.getAppName());
                getAppVersion(info);
                break;
            }
        }
    }

    private static ServiceInfo getFtpBanner(ServiceInfo info) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.setDefaultTimeout(CONNECTION_TIMEOUT);

            ftpClient.connect(info.getIp());
            info.setInfo(ftpClient.getReplyString());
            checkMostPopularFtpServers(info);
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static void checkMostPopularFtpServers(ServiceInfo info) {
        for (FtpServers server : FtpServers.values()) {
            if (info.getInfo().contains(server.getAppName())) {
                info.setAppName(server.getAppName());
                break;
            }
        }
    }

    private static ServiceInfo getHttpBanner(ServiceInfo info) {
        try {
            String urlString = "http://" + info.getIp();
            StringBuilder message = new StringBuilder();
            if(info.getPort().getValue() == 443) {
                urlString = "https://" + info.getIp();
            }
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(OPENED_CONNECTION_TIMEOUT);
            Map<String, List<String>> map = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if(entry.getKey() == null) {
                    message.append(entry.getValue().getFirst()).append("\n");
                }
                else {
                    message.append(entry.getKey()).append(": ").append(entry.getValue().getFirst()).append("\n");
                }
            }
            info.setInfo(message.toString());
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static void getAppVersion(ServiceInfo serviceInfo) {
        String[] split = serviceInfo.getInfo().split(" ");
        String regex = "^[0-9./]+$";
        for (String s : split) {
            if (s.equals("220")) continue;
            if (s.matches(regex)) {
                serviceInfo.setVersion(s);
                break;
            }
        }
    }

}
