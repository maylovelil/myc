package com.myc.service;


import com.myc.comm.base.BaseService;
import com.myc.entity.UserRole;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
public interface UserRoleService extends BaseService<UserRole> {

    public void addUserRole(UserRole userRole);
}
