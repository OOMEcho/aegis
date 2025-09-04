package com.aegis.common.ip2region;

import com.aegis.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @Author: xuesong.lei
 * @Date: 2025/08/21 15:24
 * @Description: 根据IP地址获取归属地
 */
@Slf4j
@Service
public class Ip2regionService {

    private Searcher searcher;

    @PostConstruct
    public void init() throws Exception {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("ip2region/ip2region.xdb")) {

            if (inputStream == null) {
                throw new FileNotFoundException("找不到ip2region.xdb文件");
            }

            byte[] cBuff = StreamUtils.copyToByteArray(inputStream);
            searcher = Searcher.newWithBuffer(cBuff);
            log.info("ip2region数据库加载成功");
        }
    }

    public String getRegion(String ip) {
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        try {
            String search = searcher.search(ip);
            String[] obj = search.split("\\|");
            return String.format("%s-%s-%s-%s", obj[0], obj[2], obj[3], obj[4]);
        } catch (Exception e) {
            log.error("IP查询失败: {}", ip, e);
            return "未知|0|0|0|0";
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        if (searcher != null) {
            searcher.close();
        }
    }
}
