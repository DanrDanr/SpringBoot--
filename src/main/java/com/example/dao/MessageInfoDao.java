package com.example.dao;

import com.example.entity.MessageInfo;
import com.example.vo.MessageInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface MessageInfoDao extends Mapper<MessageInfo> {
    /**
     * 根据名称（name）查询消息信息（MessageInfoVo）列表
     * @param name
     * @return
     */
    List<MessageInfoVo> findByName(@Param("name") String name);
    /**
     * 查询消息数量
     * @return
     */
    Integer count();
}
