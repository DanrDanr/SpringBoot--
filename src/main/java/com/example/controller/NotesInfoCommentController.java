package com.example.controller;

import com.example.common.Result;
import com.example.entity.NotesInfoComment;
import com.example.vo.NotesInfoCommentVo;
import com.example.service.NotesInfoCommentService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/notesInfoComment")
public class NotesInfoCommentController {
    @Resource
    private NotesInfoCommentService notesInfoCommentService;

    /**
     * 添加评论信息
     * @param commentInfo
     * @param request
     * @return
     */
    @PostMapping
    public Result<NotesInfoComment> add(@RequestBody NotesInfoComment commentInfo, HttpServletRequest request) {
        notesInfoCommentService.add(commentInfo, request);
        return Result.success(commentInfo);
    }

    /**
     * 删除评论信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        notesInfoCommentService.delete(id);
        return Result.success();
    }

    /**
     * 更新评论信息
     * @param commentInfo
     * @return
     */
    @PutMapping
    public Result update(@RequestBody NotesInfoComment commentInfo) {
        notesInfoCommentService.update(commentInfo);
        return Result.success();
    }

    /**
     * 获取指定id的评论信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<NotesInfoComment> detail(@PathVariable Long id) {
        NotesInfoComment commentInfo = notesInfoCommentService.findById(id);
        return Result.success(commentInfo);
    }

    /**
     * 获取所有评论信息列表
     * @return
     */
    @GetMapping
    public Result<List<NotesInfoCommentVo>> all() {
        return Result.success(notesInfoCommentService.findAll());
    }

    /**
     * 分页获取评论信息列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<NotesInfoCommentVo>> page(@PathVariable String name,
                                                @RequestParam(defaultValue = "1") Integer pageNum,
                                                @RequestParam(defaultValue = "5") Integer pageSize,
                                                HttpServletRequest request) {
        return Result.success(notesInfoCommentService.findPage(name, pageNum, pageSize));
    }

    /**
     * 根据外键id获取评论信息列表
     * @param id
     * @return
     */
    @GetMapping("/findByForeignId/{id}")
    public Result<List<NotesInfoComment>> findByForeignId (@PathVariable Long id) {
        return Result.success(notesInfoCommentService.findByForeignId(id));
    }
}
