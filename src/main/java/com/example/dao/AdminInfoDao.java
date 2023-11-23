package com.example.dao;

import com.example.entity.AdminInfo;
import com.example.vo.AdminInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 扩展`Mapper`接口是为了使用现有的映射器配置（如`mapper.xml`文件）
 */
@Repository
public interface AdminInfoDao extends Mapper<AdminInfo> {
    /**
     * 执行这个查询后，会返回一个包含匹配管理员信息的`AdminInfoVo`列表
     * 添加了条件判断查询包含指定用户名的管理员信息和排序功能根据id排序
     * @param name
     * @return
     */

    List<AdminInfoVo> findByName(@Param("name") String name);

    /**
     * 检查给定的列（column）和值（value）是否在数据库中已经存在，如果存在，返回该值的ID，否则返回null
     * @param column
     * @param value
     * @param id
     * @return
     */
    int checkRepeat(String column, String value, Long id);

    /**
     * 根据姓名查找管理员
     * @param username
     * @return
     */
    AdminInfoVo findByUsername(String username);

    /**
     * 查询数量
     * @return
     */
    Integer count();
}
