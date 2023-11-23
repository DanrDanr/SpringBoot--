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
import com.example.entity.AdvertiserInfo;
import com.example.service.*;
import com.example.vo.AdvertiserInfoVo;

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
@RequestMapping(value = "/advertiserInfo")
public class AdvertiserInfoController {
    @Resource
    private AdvertiserInfoService advertiserInfoService;

    /**
     * 用于添加广告主信息
     * @param advertiserInfo
     * @return
     */
    @PostMapping
    public Result<AdvertiserInfo> add(@RequestBody AdvertiserInfoVo advertiserInfo) {
        //接收一个AdvetiserInfoVo对象
        //调用advertiserInfoService的add方法将该对象添加到数据库中，并返回添加成功的结果
        advertiserInfoService.add(advertiserInfo);
        return Result.success(advertiserInfo);
    }

    /**
     * 用于删除指定ID的广告主信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        //接收一个ID参数，调用advertiserInfoService的delete方法删除指定ID的广告主信息，并返回删除成功的结果
        advertiserInfoService.delete(id);
        return Result.success();
    }

    /**
     * 用于更新广告主信息
     * @param advertiserInfo
     * @return
     */
    @PutMapping
    public Result update(@RequestBody AdvertiserInfoVo advertiserInfo) {
        //接收一个AdvetiserInfoVo对象，调用advertiserInfoService的update方法更新该对象对应的数据库记录，并返回更新成功的结果
        advertiserInfoService.update(advertiserInfo);
        return Result.success();
    }

    /**
     * 用于获取指定ID的广告主信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AdvertiserInfo> detail(@PathVariable Long id) {
        //接收一个ID参数，调用advertiserInfoService的findById方法获取指定ID的广告主信息，并返回查询结果
        AdvertiserInfo advertiserInfo = advertiserInfoService.findById(id);
        return Result.success(advertiserInfo);
    }

    /**
     * 用于获取所有广告主信息
     * @return
     */
    @GetMapping
    public Result<List<AdvertiserInfoVo>> all() {
        //调用advertiserInfoService的findAll方法获取所有广告主信息，并返回查询结果
        return Result.success(advertiserInfoService.findAll());
    }

    /**
     * 分页获取广告主信息
     * @param name
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<AdvertiserInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        //接收一个name参数和分页参数，调用advertiserInfoService的findPage方法根据name和分页参数进行分页查询，并返回分页查询结果
        return Result.success(advertiserInfoService.findPage(name, pageNum, pageSize, request));
    }

    /**
    * 批量通过excel添加信息
    * @param file excel文件
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        //接收一个Excel文件，使用ExcelUtil读取文件中的广告主信息
        //过滤空数据后调用advertiserInfoService的add方法批量添加到数据库中，并返回添加成功的结果
        List<AdvertiserInfo> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(AdvertiserInfo.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            // 处理一下空数据
            List<AdvertiserInfo> resultList = infoList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getName())).collect(Collectors.toList());
            for (AdvertiserInfo info : resultList) {
                advertiserInfoService.add(info);
            }
        }
        return Result.success();
    }

    /**
     * 获取Excel模板
     * @param response
     * @throws IOException
     */
    @GetMapping("/getExcelModel")
    public void getExcelModel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
		row.put("name", "系统公告");
		row.put("content", "这是系统公告，管理员可以在后台修改");
		row.put("time", "TIME");

        List<Map<String, Object>> list = CollUtil.newArrayList(row);

        // 2. 写excel 通过hutool工具类ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=advertiserInfoModel.xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
}
