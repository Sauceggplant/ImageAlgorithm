package org.sauceggplant.ia.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 配置文件实用类
 */
public class PropertiesUtil {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 图像算法配置
     */
    public static Properties iaProperties = new Properties();

    /**
     * 图像算法配置文件路径
     */
    private static final String IA_PROPERTIES = "ia.properties";

    /**
     * 配置文件加载中日志
     */
    private static final String IA_PROPERTIES_LOADING = "[ia.properties]Loading...";

    /**
     * 配置文件加载失败日志
     */
    private static final String IA_PROPERTIES_LOAD_FAIL = "[ia.properties]Load Fail";

    //加载
    static {
        try {
            logger.info(IA_PROPERTIES_LOADING);
            iaProperties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(IA_PROPERTIES));
        } catch (Exception e) {
            logger.error(IA_PROPERTIES_LOAD_FAIL, e);
        }
    }

    /**
     * 获取配置
     *
     * @param key 属性
     * @return 值
     */
    public static String getProperty(String key) {
        return iaProperties.getProperty(key);
    }
}
