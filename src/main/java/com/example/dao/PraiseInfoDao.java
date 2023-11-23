package com.example.dao;

import com.example.entity.Account;
import com.example.entity.PraiseInfo;
import com.example.vo.PraiseInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface PraiseInfoDao extends Mapper<PraiseInfo> {
    /**
     * 根据提供的名字（name参数），它会返回匹配的名字点赞信息
     * @param name
     * @return
     */
    List<PraiseInfoVo> findByName(@Param("name") String name);

    /**
     * 统计满足以下条件的记录数量
     * 1. 如果`notesId`不为空，那么查询条件中包含`notesId`；
     * 2. 如果`foodsId`不为空，那么查询条件中包含`foodsId`。
     * @param foodsId
     * @return
     */
    Integer count(@Param("notesId") Long notesId, @Param("foodsId") Long foodsId);

    /**
     * 据给定的用户（Account对象ID）和点赞信息的美食id和笔记id（PraiseInfo对象）查询数据库中的点赞记录
     *
     * @param user
     * @param praiseInfo
     * @return
     */
    PraiseInfoVo findByUser(Account user, PraiseInfo praiseInfo);
}
