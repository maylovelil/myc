package com.myc.comm.shiro;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.myc.comm.jwt.JWTToken;
import com.myc.comm.utils.CommonUtils;
import com.myc.comm.utils.CookieUtils;
import com.myc.comm.utils.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Description: 定制了JWT加密方式 所以要扩展 BasicHttpAuthenticationFilter
 * @author: :MaYong
 * @Date: 2018/3/9 13:50
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        logger.info("url:{}", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getServletContext().getContextPath());
        HttpServletRequest req = WebUtils.toHttp(request);
        String authorization = req.getHeader(JWTToken.AUTH_TOKEN);
        String cookie_authorization = CookieUtils.getCookieValue(req, JWTToken.AUTH_TOKEN);
        Boolean isLogin = true;
        if (StringUtils.isNotBlank(authorization) || StringUtils.isNotBlank(cookie_authorization)) {
            isLogin = false;
        }
        logger.info("是否需要登录：{}", isLogin);
        return isLogin;
    }

    /**
     * 登录扩展判断
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String authorization = httpServletRequest.getHeader(JWTToken.AUTH_TOKEN);
        String cookie_authorization = CookieUtils.getCookieValue(httpServletRequest, JWTToken.AUTH_TOKEN);
        if (authorization == null) {
            authorization = cookie_authorization;
        }
        JWTToken token = new JWTToken(authorization);
        //提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        //如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                this.executeLogin(request, response);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * isAccessAllowed 返回false之后调用
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        if(CommonUtils.isAjax(request)){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            Map<String,Integer> stringMap = Maps.newHashMap();
            stringMap.put("status",HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(JSONObject.toJSONString(stringMap));
        }else{
            WebUtils.issueRedirect(request, response,this.getLoginUrl());
        }
        return false;
    }


    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;

        }
        return super.preHandle(request, response);
    }

    private void redirectUrl(ServletRequest request, ServletResponse response, String url) {
        try {
            //WebUtils.issueRedirect(request,response,url);
            WebUtils.toHttp(response).sendRedirect(request.getServletContext().getContextPath() + this.getLoginUrl());
        } catch (IOException e) {
            logger.info("URL:{},跳转失败！");
        }
    }

}