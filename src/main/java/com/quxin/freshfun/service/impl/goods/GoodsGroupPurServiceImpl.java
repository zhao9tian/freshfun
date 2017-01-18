package com.quxin.freshfun.service.impl.goods;

import com.quxin.freshfun.dao.GoodsBaseMapper;
import com.quxin.freshfun.dao.GoodsGroupPurMapper;
import com.quxin.freshfun.model.goods.GoodsGroupPurPOJO;
import com.quxin.freshfun.model.pojo.goods.GoodsBasePOJO;
import com.quxin.freshfun.service.goods.GoodsGroupPurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 团购商品实现类
 * Created by qucheng on 17/1/18.
 */
@Service("goodsGroupPurService")
public class GoodsGroupPurServiceImpl implements GoodsGroupPurService {

    @Autowired
    private GoodsGroupPurMapper goodsGroupPurMapper;

    @Autowired
    private GoodsBaseMapper goodsBaseMapper;

    Logger logger = LoggerFactory.getLogger(GoodsGroupPurServiceImpl.class);

    @Override
    public GoodsGroupPurPOJO queryGoodsGroupPurByGoodsId(Long goodsId) {
        //1.商品下架了应该没有信息
        GoodsBasePOJO goodsBasePOJO  = goodsBaseMapper.findGoodsById(goodsId);
        if(0 == goodsBasePOJO.getIsOnSale()){
            logger.error("商品已下架");
            return null;
        }else{
            return goodsGroupPurMapper.selectGoodsGPByGoodsId(goodsId);
        }
    }
}
