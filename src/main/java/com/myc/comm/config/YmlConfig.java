package com.myc.comm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/4/3 16:42
 */
@Component
@ConfigurationProperties(prefix = "myc")
public class YmlConfig {
    private Map<String,String> props = new HashMap<>();

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }
}
