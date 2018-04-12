package com.myc.service.impl;


import com.myc.comm.base.BaseServiceImpl;
import com.myc.comm.shiro.MyRealm;
import com.myc.comm.utils.StringUtils;
import com.myc.entity.RoleResources;
import com.myc.mapper.UserRoleMapper;
import com.myc.service.RoleResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */

@Service("roleResourcesService")
public class RoleResourcesServiceImpl extends BaseServiceImpl<RoleResources> implements RoleResourcesService {
    @Resource
    private UserRoleMapper userRoleMapper;
    /*@Resource
    private ShiroService shiroService;*/
    @Autowired
    private MyRealm myRealm;

    @Override
    //更新权限
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    //@CacheEvict(cacheNames="resources", allEntries=true)
    public void addRoleResources(RoleResources roleResources) {
        //删除
        Example example = new Example(RoleResources.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleResources.getRoleId());
        mapper.deleteByExample(example);
        //添加
        if (!StringUtils.isEmpty(roleResources.getResourcesId())) {
            String[] resourcesArr = roleResources.getResourcesId().split(",");
            for (String resourcesId : resourcesArr) {
                RoleResources r = new RoleResources();
                r.setRoleId(roleResources.getRoleId());
                r.setResourcesId(resourcesId);
                mapper.insert(r);
            }
        }

        List<Integer> userIds = userRoleMapper.findUserIdByRoleId(roleResources.getRoleId());
        //更新当前登录的用户的权限缓存
        myRealm.clearUserAuthByUserId(userIds);


    }
}
