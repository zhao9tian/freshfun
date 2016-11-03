package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.pojo.goods.ThemePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by qingtian on 2016/10/26.
 */
public interface GoodsThemeMapper {
    /**
     * 查询所有专题列表
     * @return
     */
    List<ThemePOJO> selectAll(@Param("page") Integer page,@Param("pageSize") Integer pageSize);

    /**
     * 根据专题编号查询专题信息
     * @return
     */
    ThemePOJO selectThemeById(Long themeId);
}
