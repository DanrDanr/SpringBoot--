package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.CollectInfo;
import com.example.service.*;
import com.example.vo.CollectInfoVo;
import com.example.vo.CollectInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/collectInfo")
public class CollectInfoController {
    @Resource
    private CollectInfoService collectInfoService;

    /**
     * 用于添加采集信息
     * @param collectInfo
     * @param request
     * @return
     */
    @PostMapping
    public Result<CollectInfo> add(@RequestBody CollectInfoVo collectInfo, HttpServletRequest request) {
        //接收一个CollectInfoVo对象和HttpServletRequest对象
        //调用collectInfoService的add方法将该对象添加到数据库中，并返回添加成功的结果
        collectInfoService.add(collectInfo, request);
        return Result.success(collectInfo);
    }

    /**
     * 用于删除指定ID的采集信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        //接收一个ID参数，调用collectInfoService的delete方法删除指定ID的采集信息，并返回删除成功的结果
        collectInfoService.delete(id);
        return Result.success();
    }

    /**
     * 用于更新采集信息
     * @param collectInfo
     * @return
     */
    @PutMapping
    public Result update(@RequestBody CollectInfoVo collectInfo) {
        //接收一个CollectInfoVo对象，调用collectInfoService的update方法更新该对象对应的数据库记录，并返回更新成功的结果
        collectInfoService.update(collectInfo);
        return Result.success();
    }

    /**
     * 用于获取指定ID的采集信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<CollectInfo> detail(@PathVariable Long id) {
        // 接收一个ID参数，调用collectInfoService的findById方法获取指定ID的采集信息，并返回查询结果
        CollectInfo collectInfo = collectInfoService.findById(id);
        return Result.success(collectInfo);
    }

    /**
     * 用于获取所有采集信息
     * @return
     */
    @GetMapping
    public Result<List<CollectInfoVo>> all() {
        //调用collectInfoService的findAll方法获取所有采集信息，并返回查询结果
        return Result.success(collectInfoService.findAll());
    }

    /**
     * 分页获取采集信息
     * @param name
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<CollectInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        //接收一个name参数和分页参数，调用collectInfoService的findPage方法根据name和分页参数进行分页查询，并返回分页查询结果
        return Result.success(collectInfoService.findPage(name, pageNum, pageSize, request));
    }

}
