package com.myc.service.impl;

import com.myc.comm.base.BaseServiceImpl;
import com.myc.comm.shiro.MyRealm;
import com.myc.entity.UserRole;
import com.myc.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {
    @Autowired
    private MyRealm myRealm;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void addUserRole(Integer userId,String roleIds) {
        //删除
        Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userid", userId);
        mapper.deleteByExample(example);
        //添加
        String[] rds = roleIds.split(",");
        for (String roleId : rds) {
            UserRole u = new UserRole();
            u.setUserid(userId);
            u.setRoleid(Integer.valueOf(roleId));
            mapper.insert(u);
        }
        //更新当前登录的用户的权限缓存
        List<Integer> userIds = new ArrayList<Integer>();
        userIds.add(userId);
        myRealm.clearUserAuthByUserId(userIds);
    }
}
