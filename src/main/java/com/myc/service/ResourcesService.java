package com.myc.service;

import com.github.pagehelper.PageInfo;
import com.myc.comm.base.BaseService;
import com.myc.entity.Resources;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
public interface ResourcesService extends BaseService<Resources> {
    PageInfo<Resources> selectByPage(Resources resources, int start, int length);

    public List<Resources> queryAll();

    public List<Resources> loadUserResources(Map<String, Object> map);

    public List<Resources> queryResourcesListWithSelected(Integer rid);
}
