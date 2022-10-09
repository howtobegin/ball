package com.ball.boss.service.assist;

import com.alibaba.fastjson.JSON;
import com.ball.boss.dao.entity.IpAddress;
import com.ball.boss.service.IIpAddressService;
import com.ball.boss.util.BossThreadPool;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


/**
 * @author littlehow
 */
@Component
@Slf4j
public class IpAssist {

    @Value("${ip.address.url:http://whois.pconline.com.cn/ipJson.jsp?ip={ip}&json=true}")
    private String addressUrl;

    @Value("${ip.address.enabled:true}")
    private boolean ipCheckEnabled;

    @Autowired
    private IIpAddressService ipAddressService;

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 检测ip或
     * @param ip
     */
    public void checkOrSaveIpAddress(String ip) {
        if (!ipCheckEnabled) {
            return;
        }
        if (StringUtils.isEmpty(ip) || isLocal(ip)) {
            return;
        }
        if (StringUtils.hasText(addressUrl) && addressUrl.startsWith("http")) {
            BossThreadPool.execute(() -> {
                // 查询ip是否存在
                IpAddress address = ipAddressService.lambdaQuery().eq(IpAddress::getIp, ip).one();
                if (address != null) return;
                String result;
                try {
                    String value = restTemplate.getForObject(addressUrl.replace("{ip}", ip), String.class);
                    IpAddressLocal ipAddress = JSON.parseObject(value, IpAddressLocal.class);
                    if (ipAddress != null) {
                        result = ipAddress.addr;
                        save(ip, result);
                    }
                } catch (Exception e) {
                    // 暂时不处理
                    log.warn("getIp-info-error", e);
                }

            });
        }
    }

    public void save(String ip, String address) {
        ipAddressService.save(new IpAddress().setAddress(address).setIp(ip));
    }

    /**
     * 获取ip地址信息
     * @param ip -
     * @return -
     */
    public String getIpAddressInfo(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return ip;
        }
        if (isLocal(ip)) {
            return "LAN Address(local area network)";
        } else {
            // 查询ip是否存在
            IpAddress address = ipAddressService.lambdaQuery().eq(IpAddress::getIp, ip).one();
            if (address == null) {
                return "UNKNOWN";
            }
            return address.getAddress();
        }
    }

    @Getter
    @Setter
    private static class IpAddressLocal {
        private String addr;
    }

    /**
     * 简单判定是否为局域网ip
     * @param ip
     * @return
     */
    private boolean isLocal(String ip) {
        if (StringUtils.hasText(ip)) {
            if ("localhost".equalsIgnoreCase(ip) || ip.startsWith("0:0:0:0")) {
                return true;
            }
            String[] ipNo = ip.split("\\.");
            // 10开头的ip全是局域网ip, 127开头的为回环ip, 192.168也是局域网
            return "10".equals(ipNo[0])
                    || "127".equals(ipNo[0])
                    || ("192".equals(ipNo[0]) && "168".equals(ipNo[2]));
        }
        return false;
    }
}
