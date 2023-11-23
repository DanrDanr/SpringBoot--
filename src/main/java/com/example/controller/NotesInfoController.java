package com.example.controller;

import com.example.common.Result;
import com.example.entity.NotesInfo;
import com.example.service.NotesInfoService;
import com.example.vo.NotesInfoVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/notesInfo")
public class NotesInfoController {
    @Resource
    private NotesInfoService notesInfoService;

    /**
     * 添加笔记信息
     * @param info
     * @return
     */
    @PostMapping
    public Result<NotesInfo> add(@RequestBody NotesInfoVo info) {
        notesInfoService.add(info);
        return Result.success(info);
    }

    /**
     * 删除笔记信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        notesInfoService.delete(id);
        return Result.success();
    }

    /**
     * 更新笔记信息
     * @param info
     * @return
     */
    @PutMapping
    public Result update(@RequestBody NotesInfoVo info) {
        notesInfoService.update(info);
        return Result.success();
    }

    /**
     * 获取指定id的笔记信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<NotesInfo> detail(@PathVariable Long id) {
        NotesInfo info = notesInfoService.findById(id);
        return Result.success(info);
    }

    /**
     * 获取所有笔记信息列表
     * @return
     */
    @GetMapping
    public Result<List<NotesInfoVo>> all() {
        return Result.success(notesInfoService.findAll());
    }

    /**
     * 分页获取笔记信息列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<NotesInfoVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(notesInfoService.findPage(name, pageNum, pageSize, request));
    }
}
