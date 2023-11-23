package com.example.dao;

import com.example.entity.UserInfo;
import com.example.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserInfoDao extends Mapper<UserInfo> {
    /**
     * 根据用户名（name）查询用户信息
     * @param name
     * @return
     */
    List<UserInfoVo> findByName(@Param("name") String name);

    /**
     * 检查给定列和值是否存在重复的记录，同时排除指定的ID
     * @param column
     * @param value
     * @param id
     * @return
     */
    int checkRepeat(String column, String value, Long id);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    UserInfoVo findByUsername(String username);

    /**
     * 查询用户数量
     * @return
     */
    Integer count();
}
