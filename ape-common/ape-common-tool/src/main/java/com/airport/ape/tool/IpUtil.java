package com.airport.ape.tool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * Ip工具类
 *
 * @author: lee
 * @date: 2023/1/15
 */
@Slf4j
public class IpUtil {

    private static final String DEFAULT_IP = "127.0.0.1";

    private static final String UN_KNOWN = "unknown";

    private static final int IP_MAX_LENGTH = 15;

    private static final String SPLIT = ",";


    private static String getLocalIp() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String localIp = localHost.getHostAddress();
            log.info("IpUtils.getLocalIp:{}", localIp);
            return localIp;
        } catch (Exception e) {
            log.error("IpUtils.getLocalIp.error:{}", e.getMessage(), e);
            return DEFAULT_IP;
        }
    }

    public static String getIp(HttpServletRequest request) {
        String ip = null;
        try {
            String[] headers = {"x-forwarded-for","Proxy-Client-IP","WL-Proxy-Client-IP","HTTP_CLIENT_IP","HTTP_X_FORWARDED_FOR"};
            for(String header:headers){
                ip = request.getHeader(header);
                if(!StringUtils.isEmpty(ip)&&UN_KNOWN.equalsIgnoreCase(ip)){
                    break;
                }
            }
            if (StringUtils.isEmpty(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (!StringUtils.isEmpty(ip) && ip.length() > IP_MAX_LENGTH) {
                if (ip.indexOf(SPLIT) > 0) {
                    ip = ip.substring(0, ip.indexOf(SPLIT));
                }
            }
        } catch (Exception e) {
            log.error("IpUtils.getLocalIp.error:{}", e.getMessage(), e);
        }
        return ip;
    }

}
