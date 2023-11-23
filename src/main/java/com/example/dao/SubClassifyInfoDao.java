package com.example.dao;

import com.example.entity.SubClassifyInfo;
import com.example.vo.SubClassifyInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SubClassifyInfoDao extends Mapper<SubClassifyInfo> {
    /**
     * 根据名称查询子分类信息
     * @param name
     * @return
     */
    List<SubClassifyInfoVo> findByName(@Param("name") String name);

    /**
     * 根据主分类id来获取子分类信息
     * @param classifyId
     * @return
     */
    List<SubClassifyInfoVo> findByClassifyId(Long classifyId);
}
