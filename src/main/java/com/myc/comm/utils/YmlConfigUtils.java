package com.myc.comm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myc.comm.config.YmlConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/4/3 16:44
 */
public class YmlConfigUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    private static Map<String, String> propertiesMap = null;

    public YmlConfigUtils() {
    }

    public static String getConfigByKey(String key) throws JsonProcessingException {
        if (null == propertiesMap || propertiesMap.isEmpty()) {
            YmlConfig ymlConfig = applicationContext.getBean(YmlConfig.class);
            propertiesMap = ymlConfig.getProps();
        }
        return propertiesMap.get(key);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (YmlConfigUtils.applicationContext == null) {
            YmlConfigUtils.applicationContext = applicationContext;
        }
    }
}
