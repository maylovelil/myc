package com.myc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.myc.comm.base.BaseServiceImpl;
import com.myc.comm.constans.CommCons;
import com.myc.comm.utils.BeanUtils;
import com.myc.comm.utils.YmlConfigUtils;
import com.myc.dto.UserDto;
import com.myc.entity.Role;
import com.myc.entity.User;
import com.myc.entity.UserRole;
import com.myc.mapper.RoleMapper;
import com.myc.mapper.UserMapper;
import com.myc.service.UserService;
import com.myc.vo.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    RoleMapper roleMapper;

    @Override
    public PageInfo<User> selectByPage(User user, int start, int length) {
        int page = start / length + 1;
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(user.getUserName())) {
            criteria.andLike("username", "%" + user.getUserName() + "%");
        }
        if (user.getId() != null) {
            criteria.andEqualTo("id", user.getId());
        }
        if (user.getEnable() != null) {
            criteria.andEqualTo("enable", user.getEnable());
        }
        //分页查询
        PageHelper.startPage(page, length);
        List<User> userList = selectByExample(example);
        return new PageInfo<>(userList);
    }

    @Override
    public User selectByUsername(String userName) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userName", userName);
        List<User> userList = selectByExample(example);
        if (userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void delUser(Integer userId) {
        //删除用户表
        mapper.deleteByPrimaryKey(userId);
        //删除用户角色表
        Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        mapper.deleteByExample(example);
    }

    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public UserVo selectUserVoByUserId(Integer userId) {
        return userMapper.selectUserVoByUserId(userId);
    }

    @Override
    public Integer updateVerifyCount(User record) {
        try {
            if (null == record) return CommCons.ZERO;
            if (null == record.getVerifyCount()) record.setVerifyCount(CommCons.ZERO);
            if (Integer.valueOf(YmlConfigUtils.getConfigByKey(CommCons.VERIFY_COUNT)).compareTo(record.getVerifyCount()) == 0) {
                record.setVerifyCount(CommCons.ZERO);
                record.setEnable(CommCons.TWO);
            }
            return mapper.updateByPrimaryKeySelective(record);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public PageInfo selectAllUserVoForPage(UserDto userDto) {
        PageHelper.startPage(userDto.getPageNumber(), userDto.getPageSize());
        List<User> userList = userMapper.selectAllUser(userDto);
        return setRoleToUserVo(new PageInfo(userList));
    }

    private PageInfo setRoleToUserVo(PageInfo pageInfo) {
        List<User> userList = (List<User>)pageInfo.getList();
        List<Integer> userIds = userList.stream().map(User::getId).collect(Collectors.toList());
        List<Role> roles = roleMapper.queryRoleListByUserIds(userIds);
        List<UserVo> userVoList = transferToVo(userList);
        userVoList.forEach(userVo -> {
            roles.forEach(role -> {
                if(userVo.getId().equals(role.getUserId())){
                    userVo.getRoleList().add(role);
                }
            });
        });
        pageInfo.setList(userVoList);
        return pageInfo;
    }

    public List<UserVo> transferToVo(List<User> userList) {
        List<UserVo> userVos = Lists.newArrayList();
        userVos.addAll(userList.stream().map(user -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userVo,user);
            return userVo;
        }).collect(Collectors.toList()));
        return userVos;
    }
}
