package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.FoodsMaterialInfo;
import com.example.service.FoodsMaterialInfoService;
import com.example.vo.FoodsMaterialInfoVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/foodsMaterialInfo")
public class FoodsMaterialInfoController {
    @Resource
    private NxSystemFileController nxSystemFileController;
    @Resource
    private FoodsMaterialInfoService foodsMaterialInfoService;

    /**
     * 添加食材信息
     * @param info
     * @param request
     * @return
     */
    @PostMapping
    public Result<FoodsMaterialInfo> add(@RequestBody FoodsMaterialInfo info, HttpServletRequest request) {
        //从请求体中获取食材信息，然后从HttpServletRequest中获取当前用户的信息
        //设置到食材信息对象中，最后将信息添加到数据库中，并返回添加的食材信息
        Account account = (Account) request.getSession().getAttribute("user");
        info.setUserName(account.getName());
        info.setLevel(account.getLevel());
        info.setUploadUserId(account.getId());
        foodsMaterialInfoService.add(info);
        return Result.success(info);
    }

    /**
     * 删除指定id的食材信息
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id, HttpServletRequest request) {
        //从路径参数中获取要删除的食材信息的id，然后从HttpServletRequest中获取当前用户的信息
        //根据id获取对应的食材信息，检查当前用户是否有权限删除该记录
        //如果有权限则删除记录
        //并根据记录中的文件id删除对应的文件记录
        Account account = (Account) request.getSession().getAttribute("user");
        FoodsMaterialInfo info = foodsMaterialInfoService.findById(id);
        if (!account.getLevel().equals(info.getLevel()) || !account.getId().equals(info.getUploadUserId())) {
            return Result.error("1001", "不能删除他人的记录");
        }
        foodsMaterialInfoService.delete(id);
        // 删除对应文件记录
        if (info.getFileId() != null) {
            nxSystemFileController.deleteFile(info.getFileId().toString());
        }
        return Result.success();
    }

    /**
     * 更新食材信息
     * @param info
     * @param request
     * @return
     */
    @PutMapping
    public Result update(@RequestBody FoodsMaterialInfo info, HttpServletRequest request) {
        //从请求体中获取更新后的食材信息，然后从HttpServletRequest中获取当前用户的信息
        //检查当前用户是否有权限修改该记录，如果有权限则调用更新记录
        Account account = (Account) request.getSession().getAttribute("user");
        if (!account.getLevel().equals(info.getLevel()) || !account.getId().equals(info.getUploadUserId())) {
            return Result.error("1001", "不能修改他人的记录");
        }
        foodsMaterialInfoService.update(info);
        return Result.success();
    }

    /**
     * 获取指定id的食材信息详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<FoodsMaterialInfoVo> detail(@PathVariable Long id) {
        //获取对应id的食材信息详情
        FoodsMaterialInfoVo info = foodsMaterialInfoService.findById(id);
        return Result.success(info);
    }

    /**
     * 获取所有食材信息列表
     * @return
     */
    @GetMapping
    public Result<List<FoodsMaterialInfoVo>> all() {
        //
        return Result.success(foodsMaterialInfoService.findAll());
    }

    /**
     * 分页获取符合条件的食材信息列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<FoodsMaterialInfoVo>> page(@PathVariable String name,
                                             @RequestParam(defaultValue = "1") Integer pageNum,
                                             @RequestParam(defaultValue = "5") Integer pageSize,
                                             HttpServletRequest request) {
        //从路径参数中获取条件name，从请求参数中获取pageNum和pageSize
        //然后调用foodsMaterialInfoService的findPage方法进行分页查询，并返回分页结果
        return Result.success(foodsMaterialInfoService.findPage(name, pageNum, pageSize));
    }

}
