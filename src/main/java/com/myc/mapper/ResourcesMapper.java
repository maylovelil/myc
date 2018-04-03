package com.myc.mapper;

import com.myc.comm.base.MycBaseMapper;
import com.myc.entity.Resources;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/2/26 18:52
 */
public interface ResourcesMapper extends MycBaseMapper<Resources> {

    public List<Resources> queryAll();

    public List<Resources> loadUserResources(Map<String, Object> map);

    public List<Resources> queryResourcesListWithSelected(Integer rid);
}