package com.myc.comm.base;


import javax.persistence.*;
import java.util.Date;
/**
 * @Description:
 * @author: :MaYong
 * @Date: 2018/3/16 12:31
 */
@MappedSuperclass
public class BaseEntity<T>{

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;

    private String remark;

}