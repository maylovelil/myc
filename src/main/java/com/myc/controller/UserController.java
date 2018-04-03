package com.myc.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myc.comm.Result;
import com.myc.comm.constans.RoleCons;
import com.myc.comm.utils.ResultUtils;
import com.myc.entity.User;
import com.myc.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 19:10
 */
@Controller
@Transactional
@RequestMapping("user")
@Api(value = "用户操作控制器")
public class UserController {
    @Resource
    UserService userService;

    @ApiOperation(value = "获取所有的用户信息", notes = "参数描述", code = 200, produces = "application/json")
    @GetMapping(value = "list")
    @Transactional(readOnly = true)
    @RequiresRoles(value = {RoleCons.ADMIN, RoleCons.ADMINISTRATOR}, logical = Logical.OR)
    @RequiresPermissions(value = "user:list", logical = Logical.AND)
    public String getUserList(Model model) {
        User user = new User();
        user.setId(1);
        PageHelper.startPage(1, 1);
        List<User> userList = userService.queryAll();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", new PageInfo(userList));
        model.addAttribute("list", new PageInfo(userList));
        model.addAttribute("mayong","mayong123");
        return "user/index";
    }

    @PostMapping("add")
    public @ResponseBody
    Result saveUser() {

/*        User user = new User();
        user.setName("test");
        user.setSex("Fmale");
        int flag = userService.save(user);*//*        User user = new User();
        user.setName("test");
        user.setSex("Fmale");
        int flag = userService.save(user);*/
        Result result = new Result();
        return ResultUtils.SUCCESS;
    }
}
