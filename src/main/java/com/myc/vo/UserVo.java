package com.myc.vo;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.myc.comm.constans.CommCons;
import com.myc.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/29 15:31
 */
public class UserVo {
    private Integer id;
    private String username;
    private String password;
    private Integer enable;
    private Integer verifyCount;
    private String roleDesc;

    private List<Role> roleList = Lists.newArrayList();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public Integer getVerifyCount() {
        return verifyCount;
    }

    public void setVerifyCount(Integer verifyCount) {
        this.verifyCount = verifyCount;
    }

    public String getRoleDesc() {
        return Joiner.on(CommCons.COMMA).join(roleList.stream()
                .map(Role :: getRoleDesc)
                .collect(Collectors.toSet()));
    }

    public void setRoleDesc(String roleDesc) {
        roleDesc = roleDesc;
    }
}
