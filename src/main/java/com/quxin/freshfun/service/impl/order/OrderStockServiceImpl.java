package com.quxin.freshfun.service.impl.order;

import com.quxin.freshfun.dao.GoodsBaseMapper;
import com.quxin.freshfun.service.order.OrderStockService;
import com.quxin.freshfun.service.promotion.PromotionService;
import com.quxin.freshfun.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by fanyanlin on 2017/1/10.
 */
@Service("orderStockService")
public class OrderStockServiceImpl implements OrderStockService {
    @Autowired
    private GoodsBaseMapper goodsBaseMapper;

    @Autowired
    private PromotionService promotionService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int modifyGoodsSaleNum(Long goodsId) {
        if (goodsId == null)
            logger.error("增加销量参数不能为空");
        return goodsBaseMapper.updateGoodsSaleNum(goodsId);
    }

    @Override
    public int modifyStockById(Map<String, Object> map) throws BusinessException {
        if(map == null)
            logger.error("修改限量库存参数不能为空");
        return promotionService.updateStockById(map);
    }

    @Override
    public int modifyGoodsStock(Map<String, Object> map) {
        if (map == null)
            logger.error("修改商品库存参数不能为空");
        return goodsBaseMapper.updateGoodsStock(map);
    }
}
