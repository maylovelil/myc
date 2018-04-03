package com.myc.comm.shiro;

import com.myc.comm.constans.CommCons;
import com.myc.comm.jwt.JWTToken;
import com.myc.comm.utils.JWTUtils;
import com.myc.entity.Resources;
import com.myc.entity.Role;
import com.myc.entity.User;
import com.myc.service.ResourcesService;
import com.myc.service.UserService;
import com.myc.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/9 13:47
 */
@Service
public class MyRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyRealm.class);
    @Resource
    private UserService userService;

    @Resource
    private ResourcesService resourcesService;

    @Autowired
    private RedisSessionDAO redisSessionDAO;


    /**
     * JWTToken 类型的Token，
     * 那么如果在StatelessAuthcFilter类中返回的是UsernamePasswordToken，那么将会报如下错误信息：
     * Please ensure that the appropriate Realm implementation is configured correctly or
     * that the realm accepts AuthenticationTokens of this type.StatelessAuthcFilter.isAccessAllowed()
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = JWTUtils.getUser(principalCollection.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("userid", user.getId());
        UserVo userVo  = userService.selectUserVoByUserId(user.getId());
        List<Resources> resourcesList = resourcesService.loadUserResources(map);
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加用户的资源权限
        info.addStringPermissions(resourcesList.stream().map(Resources :: getResurl).collect(Collectors.toSet()));
        //添加用户的角色权限
        info.addRoles(userVo.getRoleList().stream().map(Role :: getRoleName).collect(Collectors.toSet()));
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getCredentials() == null) {
            throw new AuthenticationException("token已失效");
        }
        String jwtToken = String.valueOf(token.getCredentials());
        //获取用户的输入的账号.
        String username = JWTUtils.getUsername((String) token.getPrincipal());
        User user = userService.selectByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("用户名不存在");
        }
        if (CommCons.TWO == user.getEnable()) {
            throw new LockedAccountException(); // 帐号锁定
        }
        if(CommCons.FIVE.compareTo(user.getVerifyCount()) <= 0){
            throw new ExcessiveAttemptsException("验证未通过错误次数过多");
        }
        if (!JWTUtils.verify(jwtToken, user)) {//校验密码是否正确
            saveVerifyCount(user.getId(),user.getVerifyCount()+CommCons.ONE);
            throw new AuthenticationException("用户名或者密码错误");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(jwtToken, jwtToken, username);
        // 当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(SessionEnum.SESSION_USER_ID.getValue() + "_" + user.getId(), user.getId());
        session.setAttribute(SessionEnum.SESSION_USER.getValue(), user);
        session.setAttribute(SessionEnum.SESSION_USER_ID.getValue(), user.getId());

        //验证通过以后把验证次数清零
        saveVerifyCount(user.getId(),CommCons.ZERO);
        return authenticationInfo;
    }

    private void saveVerifyCount(Integer userId,Integer verifyCount){
        User userVerifyCount = new User();
        userVerifyCount.setId(userId);
        userVerifyCount.setVerifyCount(verifyCount);
        userService.updateVerifyCount(userVerifyCount);
    }


    /**
     * 根据userId 清除当前session存在的用户的权限缓存
     *
     * @param userIds 已经修改了权限的userId
     */
    public void clearUserAuthByUserId(List<Integer> userIds) {
        if (null == userIds || userIds.size() == 0) return;
        //获取所有session
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        //定义返回
        List<SimplePrincipalCollection> list = new ArrayList<>();
        for (Session session : sessions) {
            //获取session登录信息。
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (null != obj && obj instanceof SimplePrincipalCollection) {
                //强转
                SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
                //判断用户，匹配用户ID。
                obj = spc.getPrimaryPrincipal();
                if (null != obj && obj instanceof User) {
                    User user = (User) obj;
                    System.out.println("user:" + user);
                    //比较用户ID，符合即加入集合
                    if (null != user && userIds.contains(user.getId())) {
                        list.add(spc);
                    }
                }
            }
        }
        RealmSecurityManager securityManager =
                (RealmSecurityManager) SecurityUtils.getSecurityManager();
        MyRealm realm = (MyRealm) securityManager.getRealms().iterator().next();
        for (SimplePrincipalCollection simplePrincipalCollection : list) {
            realm.clearCachedAuthorizationInfo(simplePrincipalCollection);
        }
    }

    /**
     * 清空当前用户权限信息
     */
    public void clearCachedAuthorizationInfo() {
        PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection,JWTUtils.getUsername(principalCollection.toString()));
        super.clearCachedAuthorizationInfo(principals);
    }
    /**
     * 指定principalCollection 清除
     */
    public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
        //该位置初始化的SimplePrincipalCollection必须和登录认证初始化的参数一致，否则权限清除不了
        SimplePrincipalCollection principals = new SimplePrincipalCollection(
                principalCollection, JWTUtils.getUsername(principalCollection.toString()));
        if(principals != null){
            super.clearCachedAuthorizationInfo(principals);
        }
    }
}