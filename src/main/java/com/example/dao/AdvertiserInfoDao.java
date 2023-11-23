package com.example.dao;

import com.example.entity.AdvertiserInfo;
import com.example.vo.AdvertiserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 通过扩展Mapper<AdvertiserInfo>
 * 可以在AdvertiserInfoDao接口中实现各种数据库操作，从而方便地查询和管理广告信息
 */
@Repository
public interface AdvertiserInfoDao extends Mapper<AdvertiserInfo> {
    /**
     * 根据广告主的名字查询广告信息
     * @param name
     * @return
     */
    List<AdvertiserInfoVo> findByName(@Param("name") String name);
    // AdvertiserInfoVo 继承自 AdvertiserInfo，说明它继承了 AdvertiserInfo 的所有属性和方法
    // 并且在基础上可能添加了新的属性和方法。这样，在进行查询时，返回的结果会更加详细和符合特定需求
}
