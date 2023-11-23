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
import com.example.entity.ClassifyInfo;
import com.example.service.*;
import com.example.vo.ClassifyInfoVo;

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
@RequestMapping(value = "/classifyInfo")
public class ClassifyInfoController {
    @Resource
    private ClassifyInfoService classifyInfoService;

    /**
     * 用于添加分类信息
     * @param classifyInfo
     * @return
     */
    @PostMapping
    public Result<ClassifyInfo> add(@RequestBody ClassifyInfoVo classifyInfo) {
        //接收一个ClassifyInfoVo对象，调用classifyInfoService的add方法将该对象添加到数据库中，并返回添加成功的结果
        classifyInfoService.add(classifyInfo);
        return Result.success(classifyInfo);
    }

    /**
     * 用于删除指定ID的分类信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        //接收一个ID参数，调用classifyInfoService的delete方法删除指定ID的分类信息，并返回删除成功的结果
        classifyInfoService.delete(id);
        return Result.success();
    }

    /**
     * 用于更新分类信息
     * @param classifyInfo
     * @return
     */
    @PutMapping
    public Result update(@RequestBody ClassifyInfoVo classifyInfo) {
        //接收一个ClassifyInfoVo对象，调用classifyInfoService的update方法更新该对象对应的数据库记录，并返回更新成功的结果
        classifyInfoService.update(classifyInfo);
        return Result.success();
    }

    /**
     * 用于获取指定ID的分类信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ClassifyInfo> detail(@PathVariable Long id) {
        //接收一个ID参数，调用classifyInfoService的findById方法获取指定ID的分类信息，并返回查询结果
        ClassifyInfo classifyInfo = classifyInfoService.findById(id);
        return Result.success(classifyInfo);
    }

    /**
     * 用于获取所有分类信息
     * @return
     */
    @GetMapping
    public Result<List<ClassifyInfoVo>> all() {
        //调用classifyInfoService的findAll方法获取所有分类信息，并返回查询结果
        return Result.success(classifyInfoService.findAll());
    }

    /**
     * 分页获取分类信息
     * @param name
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<ClassifyInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        //接收一个name参数和分页参数，调用classifyInfoService的findPage方法根据name和分页参数进行分页查询，并返回分页查询结果
        return Result.success(classifyInfoService.findPage(name, pageNum, pageSize, request));
    }

    /**
    * 批量通过excel添加信息
    * @param file excel文件
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        //
        List<ClassifyInfo> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(ClassifyInfo.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            // 处理一下空数据
            List<ClassifyInfo> resultList = infoList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getName())).collect(Collectors.toList());
            for (ClassifyInfo info : resultList) {
                classifyInfoService.add(info);
            }
        }
        return Result.success();
    }

    /**
     * 生成一个包含分类信息的Excel模板，并通过response返回给前端
     * @param response
     * @throws IOException
     */
    @GetMapping("/getExcelModel")
    public void getExcelModel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
		row.put("name", "");
		row.put("descroiption", "");

        List<Map<String, Object>> list = CollUtil.newArrayList(row);

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=classifyInfoModel.xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
}
