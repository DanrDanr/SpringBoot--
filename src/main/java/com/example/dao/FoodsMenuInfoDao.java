package com.example.dao;

import com.example.entity.FoodsMenuInfo;
import com.example.vo.FoodsMenuInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface FoodsMenuInfoDao extends Mapper<FoodsMenuInfo> {
    /**
     * 根据名称、ID和分类ID查询食品菜单信息
     * @param name
     * @param id
     * @param classifyId
     * @return
     */
    List<FoodsMenuInfoVo> findByNameAndId(@Param("name") String name, @Param("id") Long id, @Param("classifyId") Long classifyId);

    /**
     * 根据名称、用户名和级别查询食品菜单信息
     * @param name
     * @param username
     * @param level
     * @return
     */
    List<FoodsMenuInfoVo> findByNameAndUser(@Param("name") String name, @Param("username") String username, @Param("level") Integer level);
}
