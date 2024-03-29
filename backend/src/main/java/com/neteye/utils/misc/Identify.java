package com.neteye.utils.misc;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.neteye.persistence.entities.PortInfo.PortInfo;
import com.neteye.utils.enums.DefaultServerPortNumbers;
import com.neteye.utils.enums.commonServers.FtpServers;
import com.neteye.utils.enums.commonServers.MailServers;
import com.neteye.utils.enums.commonServers.WwwServers;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.imap.IMAPClient;
import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.telnet.TelnetClient;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
public class Identify {

    @Setter
    private static int connectionTimeout = 10000;

    private static final String PATH = System.getProperty("user.dir");
    private static final File DATABASE = new File(PATH.replace('\\', '/') + "/GeoLite2-City_20240202/GeoLite2-City.mmdb");
    private static final DatabaseReader DB_READER;

    static {
        try {
            DB_READER = new DatabaseReader.Builder(DATABASE).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Identify() {}
    public static ServiceInfo fetchPortInfo(ServiceInfo info) {
        return switch (info.getPort()) {
            case FTP -> getFtpBanner(info);
            case TELNET -> getTelnetBanner(info);
            case SMTP -> getSmtpBanner(info);
            case HTTP, HTTP8080, HTTPS -> getHttpBanner(info);
            case POP3 -> getPop3Banner(info);
            case IMAP -> getImapBanner(info);
            case RTSP -> getRtspBanner(info);
        };
    }

    private static ServiceInfo getPop3Banner(ServiceInfo info) {
        try {
            POP3Client pop3Client = new POP3Client();
            pop3Client.setDefaultTimeout(connectionTimeout);
            pop3Client.connect(info.getIp());
            info.setInfo(pop3Client.getReplyString());
            checkMostPopularMailServers(info);
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static ServiceInfo getImapBanner(ServiceInfo info) {
        try {
            IMAPClient imapClient = new IMAPClient();
            imapClient.setDefaultTimeout(connectionTimeout);
            imapClient.connect(info.getIp());
            info.setInfo(imapClient.getReplyString());
            checkMostPopularMailServers(info);
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static ServiceInfo getTelnetBanner(ServiceInfo info) {
        try {
            TelnetClient telnetClient = new TelnetClient();
            telnetClient.setDefaultTimeout(connectionTimeout);
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
            smtpClient.setDefaultTimeout(connectionTimeout);

            smtpClient.connect(info.getIp());
            info.setInfo(smtpClient.getReplyString());
            checkMostPopularMailServers(info);
        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static void checkMostPopularMailServers(ServiceInfo info) {
        for (MailServers server : MailServers.values()) {
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
            ftpClient.setDefaultTimeout(connectionTimeout);

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

    private static ServiceInfo getRtspBanner(ServiceInfo info) {
        String url = "rtsp://" + info.getIp().getHostName() + ":554";
        try (Socket socket = new Socket(info.getIp().getHostAddress(), info.getPort().getPortNumber());
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            socket.setSoTimeout(connectionTimeout);
            out.println("OPTIONS " + url + "/ RTSP/1.0");
            out.println("CSeq: 1");
            out.println("User-Agent: ");
            out.println();

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }

            info.setInfo(response.toString());
        } catch (IOException e) {
            //ignored
        }
        return info;
    }

    private static ServiceInfo getHttpBanner(ServiceInfo info) {
        try {
            String urlString = "http://" + info.getIp().getHostName();
            StringBuilder message = new StringBuilder();
            if(info.getPort() == DefaultServerPortNumbers.HTTPS) {
                urlString = "https://" + info.getIp().getHostName();
            }

            Map<String, List<String>> headerFields = getStringListMap(info, urlString);

            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                if (entry.getKey() != null) {
                    message.append(entry.getKey()).append(": ");
                }
                for (String s : entry.getValue())
                {
                    message.append(s).append(" ");
                }
                message.append("\n");
            }

            info.setInfo(message.toString());

            if (headerFields.containsKey("Server")) {
                checkMostPopularWwwServers(info, headerFields.get("Server").getFirst());
            }


        } catch (Exception ignored) {
            //insignificant exception
        }
        return info;
    }

    private static Map<String, List<String>> getStringListMap(ServiceInfo info, String urlString) throws URISyntaxException, IOException {
        URI uri = new URI(urlString);
        URL url = uri.toURL();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if (info.getPort() == DefaultServerPortNumbers.HTTPS) {
            conn = (HttpsURLConnection) url.openConnection();
            ((HttpsURLConnection) conn).setSSLSocketFactory(getTrustAllSSLSocketFactory());
            ((HttpsURLConnection) conn).setHostnameVerifier(getTrustAllHostnameVerifier());
        }

        conn.setConnectTimeout(connectionTimeout);
        conn.setReadTimeout(connectionTimeout);
        return conn.getHeaderFields();
    }

    private static SSLSocketFactory getTrustAllSSLSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    private static HostnameVerifier getTrustAllHostnameVerifier() {
        return (hostname, session) -> true;
    }

    private static void checkMostPopularWwwServers(ServiceInfo info, String server) {
        for (WwwServers servers : WwwServers.values()) {
            if (server.contains(servers.getAppName())) {
                info.setAppName(servers.getAppName());
                getWwwAppVersion(info, server);
                break;
            }
        }
    }

    private static void getWwwAppVersion(ServiceInfo info, String server) {
        String[] split = server.split("[ /]");
        String regexToCheckIfItIsVersion = "^[0-9./]+$";

        for (String s : split) {
            if (s.matches(regexToCheckIfItIsVersion)) {
                info.setVersion(s);
                break;
            }
        }
    }

    private static void getAppVersion(ServiceInfo serviceInfo) {
        String[] split = serviceInfo.getInfo().split(" ");
        String regexToCheckIfItIsVersion = "^[0-9./]+$";
        for (String s : split) {
            if (s.equals("220")) continue;
            if (s.matches(regexToCheckIfItIsVersion)) {
                serviceInfo.setVersion(s);
                break;
            }
        }
    }

    public static String getOperatingSystem(List<PortInfo> infos) {
        List<String> results = new ArrayList<>();
        for (PortInfo info : infos) {
            if (info.getPrimaryKey().getPort() == DefaultServerPortNumbers.HTTP.getPortNumber() ||
                info.getPrimaryKey().getPort() == DefaultServerPortNumbers.HTTPS.getPortNumber() ||
                info.getPrimaryKey().getPort() == DefaultServerPortNumbers.HTTP8080.getPortNumber()) {
                results.add(checkSystemProperty(info.getInfo()));
            }
        }
        List<String> filteredResults = results.stream().filter(x -> !x.isBlank()).toList();
        return !filteredResults.isEmpty() ? filteredResults.getFirst() : "";
    }

    private static String checkSystemProperty(String info) {
        String system = "";
        String[] split = info.split("[\s\r\n]+");
        int counter = 0;
        String systemRegex = "^[(][a-zA-Z]+[)]$";
        for (String s : split) {
            if (s.equals("Server:")) {
                counter++;
            }
            if (counter == 1) {
                counter++;
                continue;
            }
            if (counter == 2) {
                if (s.matches(systemRegex)) {
                    system = s.substring(1, s.length() - 1);
                    break;
                }
            }
        }
        return system;
    }

    public static String getLocation(IpAddress address) {
        String location = "";
        try {
            CityResponse response = DB_READER.city(address.getIP());
            location = response.getCity().getName() + ", " + response.getCountry().getName();
            if (location.contains("null")) {
                location = "";
            }
        } catch (Exception e) {
            //insignificant exception
        }
        return location;
    }


}
