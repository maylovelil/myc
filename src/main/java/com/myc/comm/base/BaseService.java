package com.myc.comm.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/1 14:39
 */
@Service
public interface BaseService<T> {

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    T queryById(Long id);

    /**
     * 查询所有数据
     *
     * @return
     */
    List<T> queryAll();
    /**
     * 根据条件查询一条数据，如果有多条数据会抛出异常
     * @param record
     * @return
     */
    T queryOne(T record);

    /**
     * 根据条件查询数据列表
     * @param record
     * @return
     */
    List<T> queryListByWhere(T record);

    /**
     * 分页查询
     * @param page
     * @param rows
     * @param record
     * @return
     */
    PageInfo<T> queryPageListByWhere(Integer page, Integer rows, T record);

    /**
     * 新增数据，返回成功的条数
     * @param record
     * @return
     */
    Integer save(T record);

    /**
     * 新增数据，使用不为null的字段，返回成功的条数
     * @param record
     * @return
     */
    Integer saveSelective(T record);

    /**
     * 修改数据，返回成功的条数
     * @param record
     * @return
     */
    Integer update(T record);

    /**
     * 修改数据，使用不为null的字段，返回成功的条数
     * @param record
     * @return
     */
    Integer updateSelective(T record);

    /**
     * 根据id删除数据
     * @param id
     * @return
     */
    Integer deleteById(Long id);

    /**
     * 批量删除
     * @param clazz
     * @param property
     * @param values
     * @return
     */
    Integer deleteByIds(Class<T> clazz, String property, List<Object> values);

    /**
     * 根据条件做删除
     * @param record
     * @return
     */
    Integer deleteByWhere(T record);

    public List<T> selectByExample(Object example);
}
