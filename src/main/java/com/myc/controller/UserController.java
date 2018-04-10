package com.myc.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.myc.comm.Result;
import com.myc.comm.ResultPage;
import com.myc.comm.constans.RoleCons;
import com.myc.comm.utils.ResultUtils;
import com.myc.dto.UserDto;
import com.myc.service.UserService;
import com.myc.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getUserList(Model model,UserDto userDto) {
        PageHelper.startPage(userDto.getPageNumber(), userDto.getPageSize());
        List<UserVo> userList = userService.selectAllUserVo(userDto);
        PageInfo pageInfo = new PageInfo(userList);
        model.addAttribute("userList",pageInfo);
        PageHelper.clearPage();
        if(pageInfo.getTotal()>2){
            return "user/contacts_20";
        }else{
            return "user/contacts";
        }
    }

    @ApiOperation(value = "获取所有的用户信息", notes = "参数描述", code = 200, produces = "application/json")
    @PostMapping(value = "ajaxList")
    @Transactional(readOnly = true)
    public @ResponseBody ResultPage getAjaxList(Model model,@RequestBody UserDto userDto) {
        List<UserVo> userList = userService.selectAllUserVo(userDto);
        PageInfo pageInfo = new PageInfo(userList);
        model.addAttribute("userList",pageInfo);
        ResultPage resultPage = new ResultPage();
        resultPage.setTotal(pageInfo.getTotal());
        resultPage.setRows(userList);
        return resultPage;
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
