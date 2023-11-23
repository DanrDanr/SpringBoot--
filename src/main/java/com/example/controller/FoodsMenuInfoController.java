package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.FoodsMenuInfo;
import com.example.service.FoodsMenuInfoService;
import com.example.vo.FoodsMenuInfoVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/foodsMenuInfo")
public class FoodsMenuInfoController {

    @Resource
    private NxSystemFileController nxSystemFileController;
    @Resource
    private FoodsMenuInfoService foodsMenuInfoService;

    /**
     * 添加食谱信息
     * @param info
     * @param request
     * @return
     */
    @PostMapping
    public Result<FoodsMenuInfo> add(@RequestBody FoodsMenuInfo info, HttpServletRequest request) {
        //从请求体中获取食谱信息，然后从会话中获取当前用户的信息，设置到食谱信息对象中
        //将信息添加到数据库中，并返回添加的食谱信息
        Account account = (Account) request.getSession().getAttribute("user");
        info.setUserName(account.getName());
        info.setLevel(account.getLevel());
        info.setUploadUserId(account.getId());
        foodsMenuInfoService.add(info);
        return Result.success(info);
    }

    /**
     * 删除指定id的食谱信息
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id, HttpServletRequest request) {
        //从路径参数中获取要删除的食谱信息的id，然后从会话中获取当前用户的信息
        //根据id获取对应的食谱信息，检查当前用户是否有权限删除该记录如
        //果有权限则删除记录，并根据记录中的文件id删除对应的文件记录
        Account account = (Account) request.getSession().getAttribute("user");
        FoodsMenuInfo info = foodsMenuInfoService.findById(id);
        if (!account.getLevel().equals(info.getLevel()) || !account.getId().equals(info.getUploadUserId())) {
            return Result.error("1001", "不能删除他人的记录");
        }
        foodsMenuInfoService.delete(id);
        // 删除对应文件记录
        if (info.getFileId() != null) {
            nxSystemFileController.deleteFile(info.getFileId().toString());
        }
        return Result.success();
    }

    /**
     * 更新食谱信息
     * @param info
     * @param request
     * @return
     */
    @PutMapping
    public Result update(@RequestBody FoodsMenuInfo info, HttpServletRequest request) {
        //从请求体中获取更新后的食谱信息，然后从会话中获取当前用户的信息
        //检查当前用户是否有权限修改该记录，如果有权限则更新记录
        Account account = (Account) request.getSession().getAttribute("user");
        if (!account.getLevel().equals(info.getLevel()) || !account.getId().equals(info.getUploadUserId())) {
            return Result.error("1001", "不能修改他人的记录");
        }
        foodsMenuInfoService.update(info);
        return Result.success();
    }

    /**
     * 获取指定id的食谱信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<FoodsMenuInfoVo> detail(@PathVariable Long id) {
        FoodsMenuInfoVo info = foodsMenuInfoService.findById(id);
        return Result.success(info);
    }

    /**
     * 获取所有食谱信息列表
     * @return
     */
    @GetMapping
    public Result<List<FoodsMenuInfoVo>> all() {
        return Result.success(foodsMenuInfoService.findAll());
    }

    /**
     * 分页获取符合条件的食谱信息列表
     * @param name
     * @param classifyId
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<FoodsMenuInfoVo>> page(@PathVariable String name,
                                                  @RequestParam(required = false) Long classifyId,
                                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                                  @RequestParam(defaultValue = "5") Integer pageSize,
                                                  HttpServletRequest request) {
        //从路径参数中获取条件name和classifyId，从请求参数中获取pageNum和pageSize
        //然后调用foodsMenuInfoService的findPage方法进行分页查询，并返回分页结果
        return Result.success(foodsMenuInfoService.findPage(name, classifyId, pageNum, pageSize));
    }

    /**
     * 分页获取当前用户上传的符合条件的食谱信息列表
     * @param name
     * @param username
     * @param level
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/user/{name}")
    public Result<PageInfo<FoodsMenuInfoVo>> userPage(@PathVariable String name,
                                                      @RequestParam(required = false) String username,
                                                      @RequestParam(required = false) Integer level,
                                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "5") Integer pageSize,
                                                      HttpServletRequest request) {
        // 从路径参数中获取条件name，从请求参数中获取username、level、pageNum和pageSize
        // 然后调用foodsMenuInfoService的findPageByUser方法进行分页查询，并返回分页结果
        return Result.success(foodsMenuInfoService.findPageByUser(name, username, level, pageNum, pageSize));
    }
}
