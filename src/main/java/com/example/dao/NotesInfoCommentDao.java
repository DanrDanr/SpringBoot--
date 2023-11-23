package com.example.dao;

import com.example.entity.NotesInfoComment;
import com.example.vo.NotesInfoCommentVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NotesInfoCommentDao extends Mapper<NotesInfoComment> {
    /**
     * 根据名称查询评论笔记信息
     * @param name
     * @return
     */
    List<NotesInfoCommentVo> findAllVo(@Param("name") String name);

    /**
     * 根据外键 ID 查询评论笔记信息 这个外键对应的是笔记的id
     * @param id
     * @return
     */
    List<NotesInfoComment> findByForeignId (Long id);
}
