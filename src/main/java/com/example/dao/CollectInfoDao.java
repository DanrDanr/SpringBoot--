package com.example.dao;

import com.example.entity.Account;
import com.example.entity.CollectInfo;
import com.example.vo.CollectInfoVo;
import com.example.vo.PraiseInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CollectInfoDao extends Mapper<CollectInfo> {
    /**
     * 根据名称、用户ID和级别查询收集信息列表
     * @param name
     * @param userId
     * @param level
     * @return
     */
    List<CollectInfoVo> findByName(@Param("name") String name, @Param("userId") String userId, @Param("level") String level);

    /**
     * 根据食物id和笔记id返回相对应的收集信息id
     * @param notesId
     * @param foodsId
     * @return
     */
    Integer count(@Param("notesId") Long notesId, @Param("foodsId") Long foodsId);

    /**
     * 根据`userId`和可选的`notesId`、`foodsId`查询数据
     * 将查询结果中的`foods_name`列映射到`CollectInfoVo`类的`foodsName`属性
     * @param user
     * @param collectInfo
     * @return
     */
    CollectInfoVo findByUser(Account user, CollectInfo collectInfo);
}
