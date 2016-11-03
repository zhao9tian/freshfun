package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.pojo.goods.GoodsPropertyPOJO;

/**
 * Created by gsix on 2016/10/27.
 */
public interface GoodsPropertyMapper {
    /**
     * 根据key查询banner数据
     * @return
     */
    GoodsPropertyPOJO selectValueByKey(String key);
}
