package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.common.Result;
import com.example.common.ResultCode;
import com.example.entity.Account;
import com.example.entity.AuthorityInfo;
import com.example.exception.CustomException;
import com.example.entity.AdminInfo;
import com.example.entity.UserInfo;

import com.example.service.AdminInfoService;
import com.example.service.UserInfoService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import cn.hutool.json.JSONUtil;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    //注释表示其值应从指定的属性源()中获取
    @Value("${authority.info}")
    private String authorityStr;

	@Resource
	private AdminInfoService adminInfoService;
	@Resource
	private UserInfoService userInfoService;

    /**
     * 根据帐户级别（管理员或用户）处理用户登录和注册
     * @param account
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result<Account> login(@RequestBody Account account, HttpServletRequest request) {
        //它检查对象的账号.密码.级别 字段是空白还是 null。如果满足这些条件中的任何一个，它将引发带有错误代码的自定义异常类型
        if (StrUtil.isBlank(account.getName()) || StrUtil.isBlank(account.getPassword()) || account.getLevel() == null) {
            throw new CustomException(ResultCode.PARAM_LOST_ERROR);
        }
        Integer level = account.getLevel();
        Account login = new Account();
		if (1 == level) {
			login = adminInfoService.login(account.getName(), account.getPassword());
		}
		if (2 == level) {
			login = userInfoService.login(account.getName(), account.getPassword());
		}
        //本地会话存用户信息
        request.getSession().setAttribute("user", login);
        return Result.success(login);
    }

    /**
     * 注册 根据等级来调用不同service处理
     * @param account
     * @return
     */
    @PostMapping("/register")
    public Result<Account> register(@RequestBody Account account) {
        Integer level = account.getLevel();
        Account login = new Account();
		if (1 == level) {
			AdminInfo info = new AdminInfo();
            //该方法用于将属性从对象复制到对象。此过程将具有匹配名称的属性从一个对象映射到另一个对象
			BeanUtils.copyProperties(account, info);
			login = adminInfoService.add(info);
		}
		if (2 == level) {
			UserInfo info = new UserInfo();
			BeanUtils.copyProperties(account, info);
			login = userInfoService.add(info);
		}
        return Result.success(login);
    }

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        //退出登录 删除会话储存用户信息
        request.getSession().setAttribute("user", null);
        return Result.success();
    }

    @GetMapping("/auth")
    public Result getAuth(HttpServletRequest request) {
        //检索身份验证信息
        //如果会话信息为空 返回状态代码为“401”的错误结果和指示“未登录”的消息
        Object user = request.getSession().getAttribute("user");
        if(user == null) {
            return Result.error("401", "未登录");
        }
        // 如果用户对象不为 null，则返回包含从会话中检索到的用户信息的成功结果
        return Result.success(user);
    }

    @GetMapping("/getAccountInfo")
    public Result<Object> getAccountInfo(HttpServletRequest request) {
        // 从会话中检索“user”属性。这假定“user”属性应包含活动会话期间用户的帐户信息
        Account account = (Account) request.getSession().getAttribute("user");
        //然后检查从会话中检索到的帐户对象是否为 null。如果是，则返回带有空对象的成功结果
        if (account == null) {
            return Result.success(new Object());
        }
        Integer level = account.getLevel();
        // 如果级别为 1，则它使用检索管理员信息，并返回包含管理员信息的成功结果
		if (1 == level) {
			return Result.success(adminInfoService.findById(account.getId()));
		}
        //如果级别为 2，则它使用检索用户信息，并返回包含用户信息的成功结果
		if (2 == level) {
			return Result.success(userInfoService.findById(account.getId()));
		}
        //如果级别与 1 或 2 不匹配，则返回包含空对象的成功结果
        return Result.success(new Object());
    }

    /**
     * 它确保只有具有有效会话的经过身份验证的用户才能访问他们自己的会话相关数据
     * @param request
     * @return
     */
    @GetMapping("/getSession")
    public Result<Map<String, String>> getSession(HttpServletRequest request) {
        Account account = (Account) request.getSession().getAttribute("user");
        // 检查从会话中检索到的帐户对象是否为 null。如果为 null，则返回带有空映射的成功结果
        if (account == null) {
            return Result.success(new HashMap<>(1));
        }
        //果 account 对象不为 null，则创建一个新对象并从该帐户添加用户名。然后，它返回一个成功结果，其中包含此映射和用户名
        Map<String, String> map = new HashMap<>(1);
        map.put("username", account.getName());
        return Result.success(map);
    }

    @GetMapping("/getAuthority")
    public Result<List<AuthorityInfo>> getAuthorityInfo() {
        // JSONUtil 将 解析为 JSON 数组，然后使用 将 JSON 数组转换为对象列表
        List<AuthorityInfo> authorityInfoList = JSONUtil.toList(JSONUtil.parseArray(authorityStr), AuthorityInfo.class);
        return Result.success(authorityInfoList);
    }

    /**
    * 获取当前用户所能看到的模块信息
    * @param request
    * @return
    */
    @GetMapping("/authority")
    public Result<List<Integer>> getAuthorityInfo(HttpServletRequest request) {
        Account user = (Account) request.getSession().getAttribute("user");
        if (user == null) {
            return Result.success(new ArrayList<>());
        }
        JSONArray objects = JSONUtil.parseArray(authorityStr);
        for (Object object : objects) {
            JSONObject jsonObject = (JSONObject) object;
            if (user.getLevel().equals(jsonObject.getInt("level"))) {
                JSONArray array = JSONUtil.parseArray(jsonObject.getStr("models"));
                List<Integer> modelIdList = array.stream().map((o -> {
                    JSONObject obj = (JSONObject) o;
                    return obj.getInt("modelId");
                    })).collect(Collectors.toList());
                return Result.success(modelIdList);
            }
        }
        return Result.success(new ArrayList<>());
    }

    /**
     * 根据模块ID获取用户的权限信息
     * @param modelId
     * @param request
     * @return
     */
    @GetMapping("/permission/{modelId}")
    public Result<List<Integer>> getPermission(@PathVariable Integer modelId, HttpServletRequest request) {
        List<AuthorityInfo> authorityInfoList = JSONUtil.toList(JSONUtil.parseArray(authorityStr), AuthorityInfo.class);
        Account user = (Account) request.getSession().getAttribute("user");
        if (user == null) {
            return Result.success(new ArrayList<>());
        }
        Optional<AuthorityInfo> optional = authorityInfoList.stream().filter(x -> x.getLevel().equals(user.getLevel())).findFirst();
        if (optional.isPresent()) {
            Optional<AuthorityInfo.Model> firstOption = optional.get().getModels().stream().filter(x -> x.getModelId().equals(modelId)).findFirst();
            if (firstOption.isPresent()) {
                List<Integer> info = firstOption.get().getOperation();
                return Result.success(info);
            }
        }
        return Result.success(new ArrayList<>());
    }

    /**
     * 更新用户密码
     * @param info
     * @param request
     * @return
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account info, HttpServletRequest request) {
        Account account = (Account) request.getSession().getAttribute("user");
        if (account == null) {
            return Result.error(ResultCode.USER_NOT_EXIST_ERROR.code, ResultCode.USER_NOT_EXIST_ERROR.msg);
        }
        String oldPassword = SecureUtil.md5(info.getPassword());
        if (!oldPassword.equals(account.getPassword())) {
            return Result.error(ResultCode.PARAM_PASSWORD_ERROR.code, ResultCode.PARAM_PASSWORD_ERROR.msg);
        }
        info.setPassword(SecureUtil.md5(info.getNewPassword()));
        Integer level = account.getLevel();
		if (1 == level) {
			AdminInfo adminInfo = new AdminInfo();
			BeanUtils.copyProperties(info, adminInfo);
			adminInfoService.update(adminInfo);
		}
		if (2 == level) {
			UserInfo userInfo = new UserInfo();
			BeanUtils.copyProperties(info, userInfo);
			userInfoService.update(userInfo);
		}

        info.setLevel(level);
        info.setName(account.getName());
        // 清空session，让用户重新登录
        request.getSession().setAttribute("user", null);
        return Result.success();
    }

    /**
     * 重置用户密码
     * @param account
     * @return
     */
    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody Account account) {
        Integer level = account.getLevel();
		if (1 == level) {
			AdminInfo info = adminInfoService.findByUserName(account.getName());
			if (info == null) {
				return Result.error(ResultCode.USER_NOT_EXIST_ERROR.code, ResultCode.USER_NOT_EXIST_ERROR.msg);
			}
			info.setPassword(SecureUtil.md5("123456"));
			adminInfoService.update(info);
		}
		if (2 == level) {
			UserInfo info = userInfoService.findByUserName(account.getName());
			if (info == null) {
				return Result.error(ResultCode.USER_NOT_EXIST_ERROR.code, ResultCode.USER_NOT_EXIST_ERROR.msg);
			}
			info.setPassword(SecureUtil.md5("123456"));
			userInfoService.update(info);
		}

        return Result.success();
    }
}
