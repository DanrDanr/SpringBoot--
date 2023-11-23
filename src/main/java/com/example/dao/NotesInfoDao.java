package com.example.dao;

import com.example.entity.NotesInfo;
import com.example.vo.NotesInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NotesInfoDao extends Mapper<NotesInfo> {
    /**
     * 根据名称、用户ID和状态查询笔记信息
     * 照笔记信息表的ID降序排序
     * @param name
     * @param userId
     * @param status
     * @return
     */
    List<NotesInfoVo> findByName(@Param("name") String name, @Param("userId") String userId, @Param("status") String status);
}
