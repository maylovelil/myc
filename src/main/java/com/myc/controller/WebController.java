package com.myc.controller;

import com.myc.comm.ResponseBean;
import com.myc.comm.Result;
import com.myc.comm.config.RedisConfig;
import com.myc.comm.constans.CommCons;
import com.myc.comm.jwt.JWTToken;
import com.myc.comm.utils.MD5Utils;
import com.myc.dto.UserDto;
import com.myc.entity.User;
import com.myc.service.UserService;
import com.myc.comm.utils.CookieUtils;
import com.myc.comm.utils.JWTUtils;
import com.myc.comm.utils.ResultUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/8 19:05
 */
@Controller
@RequestMapping
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    @Autowired
    private UserService userService;

    @GetMapping(value = {"login", ""})
    public String login() {
        Boolean isAuth = SecurityUtils.getSubject().isAuthenticated();
        if (isAuth) {
            return "redirect:/index";
        }
        return "user/login";
    }

    @PostMapping(value = "login")
    public String toLogin(@RequestParam("userName") String userName,
                          @RequestParam("password") String password,
                          Model model,
                          RedirectAttributes redirectAttributes,
                          HttpServletResponse response) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(MD5Utils.md5(MD5Utils.md5(password)));
        User userBean = userService.queryOne(user);
        if (userBean != null){
            if(null != userBean.getVerifyCount()){
                if(CommCons.FIVE.compareTo(userBean.getVerifyCount()) <= 0){
                    redirectAttributes.addFlashAttribute("message", "验证未通过错误次数过多,请联系管理员解锁");
                    return "redirect:/login";
                }
            }
        }else{
            User verifyUser = userService.selectByUsername(userName);
            User userVerifyCount = new User();
            if(verifyUser != null) {
                if(CommCons.TWO == verifyUser.getEnable()){
                    redirectAttributes.addFlashAttribute("message", "账户已经锁定，请联系管理员解锁");
                    return "redirect:/login";
                }
                userVerifyCount.setId(verifyUser.getId());
                if(null != verifyUser.getVerifyCount()){
                    if(CommCons.FIVE.compareTo(verifyUser.getVerifyCount()) <= 0){
                        redirectAttributes.addFlashAttribute("message", "验证未通过错误次数过多");
                        return "redirect:/login";
                    }else {
                        userVerifyCount.setVerifyCount(verifyUser.getVerifyCount()+CommCons.ONE);
                        userService.updateVerifyCount(userVerifyCount);
                    }
                }else{
                    userVerifyCount.setVerifyCount(CommCons.ONE);
                    userService.updateVerifyCount(userVerifyCount);
                }
            }
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
            return "redirect:/login";
        }
        String sign = JWTUtils.sign(userBean);
        model.addAttribute("sign", sign);
        CookieUtils.setCookie(response, "Authorization", sign);

        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        JWTToken jwtToken = new JWTToken(sign);
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            logger.info("对用户[" + userName + "]进行登录验证..验证开始");
            currentUser.login(jwtToken);
            logger.info("对用户[" + userName + "]进行登录验证..验证通过");
        } catch (UnknownAccountException uae) {
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,未知账户");
            redirectAttributes.addFlashAttribute("message", "未知账户");
        } catch (IncorrectCredentialsException ice) {
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,错误的凭证");
            redirectAttributes.addFlashAttribute("message", "密码不正确");
        } catch (LockedAccountException lae) {
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,账户已锁定");
            redirectAttributes.addFlashAttribute("message", "账户已经锁定，请联系管理员解锁");
        } catch (ExcessiveAttemptsException eae) {
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,错误次数过多");
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,堆栈轨迹如下");
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
        }catch (TooManyResultsException tm){
            logger.info("对用户[" + userName + "]进行登录验证..验证未通过,堆栈轨迹如下");
            redirectAttributes.addFlashAttribute("message", "系统存在多个相同用户名，暂时不允许您登录");
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
            Session session = SecurityUtils.getSubject().getSession();
            logger.info("用户[" + userName + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            return "redirect:/index";
        } else {
            jwtToken.clear();
            return "redirect:/login";
        }
    }

    @GetMapping("register")
    public String toRegister() {
        return "user/register";
    }


    @PostMapping(value = "register",produces = "application/json;charset=utf-8")
    public String register(UserDto userDto,
                           Model model,
                           RedirectAttributes redirectAttributes,
                           HttpServletResponse response) {
        if (!userDto.getFirstPassword().equals(userDto.getSecondPassword())) {
            redirectAttributes.addFlashAttribute("message", "两次输入的密码不一致，请重新输入");
            return "redirect:/register";
        }
        User verifyUser = userService.selectByUsername(userDto.getUsername());
        if(null != verifyUser){
            redirectAttributes.addFlashAttribute("message", "该用户已被占用");
            return "redirect:/register";
        }
        User user = new User();
        user.setPassword(MD5Utils.md5(MD5Utils.md5(userDto.getFirstPassword())));
        user.setUsername(userDto.getUsername());
        user.setEnable(CommCons.ONE);
        user.setVerifyCount(CommCons.ZERO);
        int flag = userService.saveSelective(user);
        if (flag <= 0) {
            redirectAttributes.addFlashAttribute("message", "注册失败，请重试");
            return "redirect:/register";
        }
        String sign = JWTUtils.sign(user);
        model.addAttribute("sign", sign);
        CookieUtils.setCookie(response, "Authorization", sign);

        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        JWTToken jwtToken = new JWTToken(sign);
        currentUser.login(jwtToken);
        if(currentUser.isAuthenticated()){
            return "redirect:/index";
        }else{
            redirectAttributes.addFlashAttribute("message", "请登录");
            jwtToken.clear();
            return "redirect:/login";
        }
    }


    @RequestMapping(value = {"index"})
    public String index() {
        return "user/index";
    }

    @GetMapping("/article")
    public Result article() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return ResultUtils.ERROR("无权限访问");
        } else {
            return ResultUtils.ERROR("无权限访问");
        }
    }

    @GetMapping("/require_auth")
    @RequiresAuthentication
    public ResponseBean requireAuth() {
        return new ResponseBean(200, "You are authenticated", null);
    }

    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public ResponseBean requireRole() {
        return new ResponseBean(200, "You are visiting require_role", null);
    }

    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public ResponseBean requirePermission() {
        return new ResponseBean(200, "You are visiting permission require edit,view", null);
    }

    @GetMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result unauthorized() {
        return ResultUtils.ERROR("sdf");
    }


    @GetMapping("main")
    public String toContent() {
        return "user/index_v3";
    }

    @GetMapping("leftNav")
    public String leftNav(String url) {
        return "user/" + url;
    }

}
