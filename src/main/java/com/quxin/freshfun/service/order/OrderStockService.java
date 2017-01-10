package com.quxin.freshfun.service.order;

import com.quxin.freshfun.utils.BusinessException;

import java.util.Map;

/**
 * Created by fanyanlin on 2017/1/10.
 */
public interface OrderStockService {
    /**
     * 增加销量
     * @param goodsId 商品编号
     * @return
     */
    int modifyGoodsSaleNum(Long goodsId);

    /**
     * 修改限量商品库存
     * @param map 库存信息
     * @return
     */
    int modifyStockById(Map<String,Object> map) throws BusinessException;

    /**
     * 修改商品库存
     * @param map 参数信息
     * @return
     */
    int modifyGoodsStock(Map<String,Object> map);
}
