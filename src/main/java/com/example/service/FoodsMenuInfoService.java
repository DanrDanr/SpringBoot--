package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import com.example.dao.CollectInfoDao;
import com.example.dao.FoodsMenuInfoDao;
import com.example.dao.PraiseInfoDao;
import com.example.entity.FoodsMenuInfo;
import com.example.vo.FoodsMenuInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodsMenuInfoService {

    /**
     * @AutoWried按by type自动注入
     * @Resource默认按byName
     */
    @Resource
    private FoodsMenuInfoDao foodsMenuInfoDao;
    @Resource
    private PraiseInfoDao praiseInfoDao;
    @Resource
    private CollectInfoDao collectInfoDao;

    // 添加食物菜单信息
    public FoodsMenuInfo add(FoodsMenuInfo info) {
        foodsMenuInfoDao.insertSelective(info);
        return info;
    }

    public void delete(Long id) {
        foodsMenuInfoDao.deleteByPrimaryKey(id);
    }

    public void update(FoodsMenuInfo info) {
        foodsMenuInfoDao.updateByPrimaryKeySelective(info);
    }

    public FoodsMenuInfoVo findById(Long id) {
        List<FoodsMenuInfoVo> list = foodsMenuInfoDao.findByNameAndId(null, id, null);
        if (!CollectionUtil.isEmpty(list)) {
            //如果生成的列表不为空，则从列表中检索第一项
            FoodsMenuInfoVo foodsMenuInfoVo = list.get(0);
            //检索到的食物菜单项的表扬计数和收集计数
            Integer count = praiseInfoDao.count(null, id);
            foodsMenuInfoVo.setPraiseCount(count);

            Integer collectCount = collectInfoDao.count(null, id);
            foodsMenuInfoVo.setCollectCount(collectCount);
            //返回修改后
            return foodsMenuInfoVo;
        }
        return new FoodsMenuInfoVo();
    }

    public List<FoodsMenuInfoVo> findAll() {
        return foodsMenuInfoDao.findByNameAndId("all", null, null);
    }

    public PageInfo<FoodsMenuInfoVo> findPage(String name, Long classifyId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FoodsMenuInfoVo> info = foodsMenuInfoDao.findByNameAndId(name, null, classifyId);
        // 对于每个食物菜单进行检索，查询并设置赞美计数和收集计数
        for (FoodsMenuInfoVo foodsMenuInfoVo : info) {
            Long id = foodsMenuInfoVo.getId();
            Integer count = praiseInfoDao.count(null, id);
            foodsMenuInfoVo.setPraiseCount(count);
            Integer collectCount = collectInfoDao.count(null, id);
            foodsMenuInfoVo.setCollectCount(collectCount);
        }
        // 按点赞数按降序对列表进行排序
        info = info.stream().sorted(Comparator.comparing(FoodsMenuInfoVo::getPraiseCount).reversed()).collect(Collectors.toList());
        return PageInfo.of(info);
    }

    public PageInfo<FoodsMenuInfoVo> findPageByUser(String name, String username, Integer level, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FoodsMenuInfoVo> info = foodsMenuInfoDao.findByNameAndUser(name, username, level);
        return PageInfo.of(info);
    }
}
