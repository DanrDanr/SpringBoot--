package com.example.dao;

import com.example.entity.NewsInfo;
import com.example.vo.NewsInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NewsInfoDao extends Mapper<NewsInfo> {
    /**
     * 根据名称（name）查询新闻信息（NewsInfoVo）列表
     * @param name
     * @return
     */
    List<NewsInfoVo> findByName(@Param("name") String name);

    /**
     * 查询新闻数量
     * @return
     */
    Integer count();
}
