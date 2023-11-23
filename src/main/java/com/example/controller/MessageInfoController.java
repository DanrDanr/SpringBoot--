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
import com.example.entity.MessageInfo;
import com.example.service.*;
import com.example.vo.MessageInfoVo;

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
@RequestMapping(value = "/messageInfo")
public class MessageInfoController {
    @Resource
    private MessageInfoService messageInfoService;

    /**
     * 添加留言信息
     * @param messageInfo
     * @return
     */
    @PostMapping
    public Result<MessageInfo> add(@RequestBody MessageInfoVo messageInfo) {
        messageInfoService.add(messageInfo);
        return Result.success(messageInfo);
    }

    /**
     * 删除留言信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        messageInfoService.delete(id);
        return Result.success();
    }

    /**
     * 更新留言信息
     * @param messageInfo
     * @return
     */
    @PutMapping
    public Result update(@RequestBody MessageInfoVo messageInfo) {
        messageInfoService.update(messageInfo);
        return Result.success();
    }

    /**
     * 获取指定id的留言信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<MessageInfo> detail(@PathVariable Long id) {
        MessageInfo messageInfo = messageInfoService.findById(id);
        return Result.success(messageInfo);
    }

    /**
     * 获取所有留言信息列表
     * @return
     */
    @GetMapping
    public Result<List<MessageInfoVo>> all() {
        return Result.success(messageInfoService.findAll());
    }

    /**
     * 分页获取留言信息列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<MessageInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        //根据传入的name、pageNum和pageSize参数调用messageInfoService的findPage方法进行分页查询，并返回分页信息
        return Result.success(messageInfoService.findPage(name, pageNum, pageSize, request));
    }

    /**
    * 批量通过excel添加信息
    * @param file excel文件
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        //接收前端传入的excel文件，使用ExcelUtil读取文件内容并转换为MessageInfo对象列表
        //然后逐个调用messageInfoService的add方法将留言信息添加到数据库中
        List<MessageInfo> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(MessageInfo.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            // 处理一下空数据
            List<MessageInfo> resultList = infoList.stream().filter(x -> ObjectUtil.isNotEmpty(x.getName())).collect(Collectors.toList());
            for (MessageInfo info : resultList) {
                messageInfoService.add(info);
            }
        }
        return Result.success();
    }

    /**
     * 获取导出Excel的模板
     * @param response
     * @throws IOException
     */
    @GetMapping("/getExcelModel")
    public void getExcelModel(HttpServletResponse response) throws IOException {
        // 1. 生成excel
        Map<String, Object> row = new LinkedHashMap<>();
		row.put("name", "小李");
		row.put("content", "来了");
		row.put("time", "TIME");

        List<Map<String, Object>> list = CollUtil.newArrayList(row);

        // 2. 写excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(list, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=messageInfoModel.xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(System.out);
    }
}
