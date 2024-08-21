package com.bytepound.thstandapp.application.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author zelex
 * @Date 2023/1/4 22:19
 * @Version 1.0
 *
 * 文档参考：https://github.com/lionsoul2014/ip2region/tree/master/binding/java
 *
 * 获取的数据格式如下：
 * 国外：
 * 美国|0|德克萨斯|达拉斯|0
 * 美国|0|纽约|纽约|0
 *
 * 国内：
 * 中国|0|北京|北京市|电信
 * 中国|0|广东省|广州市|电信
 *
 */
@Slf4j
public class Ip2RegionUtil {

    //离线库数据
    private final static String DATA_PATH = "ip2region/ip2region.xdb";
    private static byte[] cBuff = null;
    private static Searcher searcher;
    private static final String[] FAKE_LOCATION = {"未知","0","未知","电信"};


    static {
        ClassPathResource classPathResource = new ClassPathResource(DATA_PATH);
        InputStream inputStream = null;
        try {
            inputStream = classPathResource.getInputStream();
            cBuff = IOUtils.toByteArray(inputStream);
            searcher = Searcher.newWithBuffer(cBuff);
            log.info("Searcher has been initialized successfully....");
        } catch (IOException e) {
            log.error("failed to load data with config path:{}, reason:{}", DATA_PATH, e.toString());
        }
    }

    /**
     * 从文件流中读取，效率比较低
     * @param ip
     * @return
     * @throws IOException
     */
    @Deprecated
    private static String getLocationFromDB(String ip) throws IOException {
        String path = ResourceUtils.getURL("classpath:" + DATA_PATH).getPath();
        Searcher searcher = null;
        try {
            searcher = Searcher.newWithFileOnly(path);
        } catch (IOException e) {
            System.out.printf("failed to create searcher with `%s`: %s\n", DATA_PATH, e);
            log.error("failed to create searcher with path:{}, reason:{}", path, e);
            return "";
        }
        // 2、查询
        try {
            String region = searcher.search(ip);
            return region;
        } catch (Exception e) {
            log.error("failed to load content from ip:{}, reason:{}", ip, e);
            return "";
        }
    }

    /**
     * 单缓存查询方式，比上面效率高
     * @param ip
     * @return
     * @throws FileNotFoundException
     */
    @Deprecated
    private static String getLocationFromSingleCache(String ip) throws FileNotFoundException {
        String path = ResourceUtils.getURL("classpath:" + DATA_PATH).getPath();

        // 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
        byte[] vIndex;
        try {
            vIndex = Searcher.loadVectorIndexFromFile(path);
        } catch (Exception e) {
            log.error("failed to load vector index from path:{}, reason:{}", path, e);
            return "";
        }

        // 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
        Searcher searcher;
        try {
            searcher = Searcher.newWithVectorIndex(path, vIndex);
        } catch (Exception e) {
            log.error("failed to create vectorIndex cached searcher with path:{}, reason:{}", path, e);
            return "";
        }

        // 3、查询
        try {
            String region = searcher.search(ip);
            return region;
        } catch (Exception e) {
            log.error("failed to load content from ip:{}, reason:{}", ip, e);
            return "";
        }

        // 备注：每个线程需要单独创建一个独立的 Searcher 对象，但是都共享全局的制度 vIndex 缓存。
    }

    public static String getLocationFromGlobalCache(String ip) {
        try {
            String region = searcher.search(ip);
            log.info("search IP result:{}", region);
            return region;
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
            log.error("failed to search the ip:{}, reason:{}",ip, e.toString());
            return "";
        }
        // 4、关闭资源 - 该 searcher 对象可以安全用于并发，等整个服务关闭的时候再关闭 searcher
        // searcher.close();
        // 备注：并发使用，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也就是你可以把这个 searcher 对象做成全局对象去跨线程访问。
    }

    /**
     * 按照地理信息层级获取信息,level 取值参照下面枚举值
     * @see IPRegionLevelEnum
     * @return
     */
    public static String getLocationByLevel(int level, String fullRegionInfo) {
        if (StringUtils.isEmpty(fullRegionInfo)) {
            log.error("empty regionInfo! using fake location info now~");
            return FAKE_LOCATION[level];
        }
        String[] split = fullRegionInfo.split("|");
        return split[level];
    }
}
