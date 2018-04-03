package com.myc.comm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/12 17:51
 */
@Configuration
public class MVCConfig  implements WebMvcConfigurer {

    /**
     * 消息转义
     * @param converters
     */
    public void configureMessageConverters( List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        converters.add(converter);
    }

    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setCharacterEncoding("UTF-8");
        registry.viewResolver(viewResolver);
    }


}
