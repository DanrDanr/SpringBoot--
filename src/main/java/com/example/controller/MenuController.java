package com.example.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MenuController {

	@Resource
	private AdminInfoService adminInfoService;
	@Resource
	private UserInfoService userInfoService;
	@Resource
	private ClassifyInfoService classifyInfoService;
	@Resource
	private SubClassifyInfoService subClassifyInfoService;
	@Resource
	private CollectInfoService collectInfoService;
	@Resource
	private PraiseInfoService praiseInfoService;
	@Resource
	private NewsInfoService newsInfoService;
	@Resource
	private AdvertiserInfoService advertiserInfoService;
	@Resource
	private MessageInfoService messageInfoService;

	/**
	 * 获取菜单信息
	 * @param request
	 * @return
	 */
    @GetMapping(value = "/getMenu", produces="application/json;charset=UTF-8")
    public String getMenu(HttpServletRequest request) {
		//从会话中获取当前用户的信息，根据用户的权限级别(level)不同，返回不同的菜单信息。
		//如果是管理员级别(level=1)，则返回包含管理员信息、用户信息、菜谱大类信息等管理信息的菜单；
		//如果是普通用户级别(level=2)，则返回包含菜谱信息、笔记信息等普通用户信息的菜单
        Account account = (Account) request.getSession().getAttribute("user");
        Integer level;
        if (account == null) {
            level = 1;
        } else {
            level = account.getLevel();
        }
        JSONObject obj = new JSONObject();
        obj.putOpt("code", 0);
        obj.putOpt("msg", "");
        JSONArray dataArray = new JSONArray();

        dataArray.add(getJsonObject("/", "系统首页", "layui-icon-home", "/"));

        JSONObject tableObj = new JSONObject();
        tableObj.putOpt("title", "信息管理");
        tableObj.putOpt("icon", "layui-icon-table");
		if (1 == level) {
			JSONArray array = new JSONArray();
			array.add(getJsonObject("adminInfo", "管理员信息", "layui-icon-table", "adminInfo"));
			array.add(getJsonObject("userInfo", "用户信息", "layui-icon-table", "userInfo"));
			array.add(getJsonObject("classifyInfo", "菜谱大类信息", "layui-icon-table", "classifyInfo"));
			array.add(getJsonObject("subClassifyInfo", "菜谱小类信息", "layui-icon-table", "subClassifyInfo"));
			array.add(getJsonObject("foodsMenuInfo", "菜谱信息", "layui-icon-table", "foodsMenuInfo"));
			array.add(getJsonObject("foodsMaterialInfo", "食材信息", "layui-icon-table", "foodsMaterialInfo"));
			array.add(getJsonObject("collectInfo", "收藏信息", "layui-icon-table", "collectInfo"));
			array.add(getJsonObject("praiseInfo", "笔记点赞信息", "layui-icon-table", "praiseInfo"));
			array.add(getJsonObject("notesInfo", "笔记信息", "layui-icon-table", "notesInfo"));
			array.add(getJsonObject("newsInfo", "饮食资讯信息", "layui-icon-table", "newsInfo"));
			array.add(getJsonObject("advertiserInfo", "公告信息", "layui-icon-table", "advertiserInfo"));
			array.add(getJsonObject("messageInfo", "趣味答题信息", "layui-icon-table", "messageInfo"));
			array.add(getJsonObject("accountAdminInfo", "个人信息", "layui-icon-user", "accountAdminInfo"));
			tableObj.putOpt("list", array);
		}

		if (2 == level) {
			JSONArray array = new JSONArray();
			array.add(getJsonObject("foodsMenuInfo", "菜谱信息", "layui-icon-table", "foodsMenuInfo"));
			array.add(getJsonObject("notesInfo", "笔记信息", "layui-icon-table", "notesInfo"));
			array.add(getJsonObject("accountUserInfo", "个人信息", "layui-icon-user", "accountUserInfo"));
			tableObj.putOpt("list", array);
		}


        dataArray.add(tableObj);
		dataArray.add(getJsonObject("notesInfoComment", "笔记评论", "layui-icon-group", "notesInfoComment"));

        dataArray.add(getJsonObject("updatePassword", "修改密码", "layui-icon-password", "updatePassword"));
        dataArray.add(getJsonObject("login", "退出登录", "layui-icon-logout", "login"));

        obj.putOpt("data", dataArray);
        return obj.toString();
    }

	/**
	 * 构建菜单项的JSONObject对象
	 * @param name
	 * @param title
	 * @param icon
	 * @param jump
	 * @return
	 */
    private JSONObject getJsonObject(String name, String title, String icon, String jump) {
		// 根据传入的name、title、icon和jump参数构建对应的JSONObject对象
        JSONObject object = new JSONObject();
        object.putOpt("name", name);
        object.putOpt("title", title);
        object.putOpt("icon", icon);
        object.putOpt("jump", jump);
        return object;
    }

	/**
	 * 获取各项信息的总数统计
	 * @return
	 */
    @GetMapping(value = "/getTotal", produces="application/json;charset=UTF-8")
    public Result<Map<String, Integer>> getTotle() {
		//调用各个service的findAll方法获取各项信息的列表，然后获取列表的大小作为各项信息的总数，并将结果封装成Map返回
        Map<String, Integer> resultMap = new HashMap<>();
		resultMap.put("adminInfo", adminInfoService.findAll().size());
		resultMap.put("userInfo", userInfoService.findAll().size());
		resultMap.put("classifyInfo", classifyInfoService.findAll().size());
		resultMap.put("subClassifyInfo", subClassifyInfoService.findAll().size());
		resultMap.put("collectInfo", collectInfoService.findAll().size());
		resultMap.put("praiseInfo", praiseInfoService.findAll().size());
		resultMap.put("newsInfo", newsInfoService.findAll().size());
		resultMap.put("advertiserInfo", advertiserInfoService.findAll().size());
		resultMap.put("messageInfo", messageInfoService.findAll().size());

        return Result.success(resultMap);
    }
}
