package com.example.dao;

import com.example.entity.NxSystemFileInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface NxSystemFileInfoDao extends Mapper<NxSystemFileInfo> {
    /**
     * 根据名称查询系统文件信息 模糊查询
     * @param name
     * @return
     */
    List<NxSystemFileInfo> findByName(@Param("name") String name);

    /**
     * 根据系统文件名称查询系统文件信息
     * @param name
     * @return
     */
    NxSystemFileInfo findByFileName(@Param("name") String name);
}
