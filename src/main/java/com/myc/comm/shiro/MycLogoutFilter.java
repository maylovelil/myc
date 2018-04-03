package com.myc.comm.shiro;

import com.myc.comm.jwt.JWTToken;
import com.myc.comm.utils.CookieUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Locale;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/23 14:58
 */
public class MycLogoutFilter  extends LogoutFilter{

    private static final Logger logger = LoggerFactory.getLogger(MyRealm.class);



    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response){
        Subject subject = this.getSubject(request, response);
        String redirectUrl = this.getRedirectUrl(request, response, subject);
        if(this.isPostOnlyLogout() && !WebUtils.toHttp(request).getMethod().toUpperCase(Locale.ENGLISH).equals("POST")) {
            return this.onLogoutRequestNotAPost(request, response);
        } else {
            try {
                PrincipalCollection principals = subject.getPrincipals();
                RealmSecurityManager securityManager =
                        (RealmSecurityManager) SecurityUtils.getSecurityManager();
                MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
                //擦除redis中的存的权限信息
                myRealm.clearCachedAuthorizationInfo(principals);
                //擦除Cookie中的存的登录信息
                CookieUtils.removeCookie(WebUtils.toHttp(request),WebUtils.toHttp(response), JWTToken.AUTH_TOKEN);
                //擦除Redis中存的用户信息

                subject.logout();
            } catch (Exception var6) {
                logger.debug("登出操作异常", var6);
            }finally {
                try {
                    this.issueRedirect(request, response, redirectUrl);
                } catch (Exception e) {
                    logger.debug("登出跳转异常",e);
                }
                logger.info("登录信息擦除完毕！");
            }
            return false;
        }
    }
}
