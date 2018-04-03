package com.myc.comm.filter;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
        initParams = {@WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")  //忽略资源
        }
)
/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/22 12:21
 */
public class DruidStatFilter extends WebStatFilter {
}
