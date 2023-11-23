package com.example.service;

import com.example.dao.AdminInfoDao;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.entity.AdminInfo;
import com.example.exception.CustomException;
import com.example.common.ResultCode;
import com.example.vo.AdminInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.hutool.crypto.SecureUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class AdminInfoService {
    @Resource
    private AdminInfoDao adminInfoDao;

    //添加管理员
    public AdminInfo add(AdminInfo adminInfo) {
        // 唯一校验
        int count = adminInfoDao.checkRepeat("name", adminInfo.getName(), null);
        if (count > 0) {
            throw new CustomException("1001", "用户名\"" + adminInfo.getName() + "\"已存在");
        }
        if (StringUtils.isEmpty(adminInfo.getPassword())) {
            // 默认密码123456
            adminInfo.setPassword(SecureUtil.md5("123456"));
        } else {
            adminInfo.setPassword(SecureUtil.md5(adminInfo.getPassword()));
        }
        adminInfoDao.insertSelective(adminInfo);
        return adminInfo;
    }

    public void delete(Long id) {
        // 实现了`DeleteByPrimaryKeyMapper<MyEntity>`接口。
        // `deleteByPrimaryKey`方法可以根据你的需求进行数据库操作
        adminInfoDao.deleteByPrimaryKey(id);
    }

    public void update(AdminInfo adminInfo) {
        // 实现UpdateByPrimaryKeySelectiveMapper<T>接口
        // 可以根据你的需求进行数据库操作
        adminInfoDao.updateByPrimaryKeySelective(adminInfo);
    }

    public AdminInfo findById(Long id) {
        return adminInfoDao.selectByPrimaryKey(id);
    }

    public List<AdminInfoVo> findAll() {
        return adminInfoDao.findByName("all");
    }

    public PageInfo<AdminInfoVo> findPage(String name, Integer pageNum, Integer pageSize, HttpServletRequest request) {
        //就是开启分页，设置【pageNum：当前页】和【pageSize：每页记录数】；
        PageHelper.startPage(pageNum, pageSize);
        // 分页场景
        List<AdminInfoVo> all = adminInfoDao.findByName(name);
        // 返回分页数据
        return PageInfo.of(all);
    }

    public AdminInfoVo findByUserName(String name) {
        return adminInfoDao.findByUsername(name);
    }

    public AdminInfo login(String username, String password) {
        AdminInfo adminInfo = adminInfoDao.findByUsername(username);
        if (adminInfo == null) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        if (!SecureUtil.md5(password).equalsIgnoreCase(adminInfo.getPassword())) {
            throw new CustomException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return adminInfo;
    }

}
