package com.quxin.freshfun.service.impl.goods;

import com.quxin.freshfun.dao.GoodsDetailsMapper;
import com.quxin.freshfun.model.GoodsMongo;
import com.quxin.freshfun.service.goods.GoodsDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mongoGoods")
public class GoodsDetailsServiceImpl implements GoodsDetailsService {

    @Autowired
    private GoodsDetailsMapper goodsDetailsMapper;

    @Override
    public List<GoodsMongo> findGoodsMongo(Integer goodsId) {
        return goodsDetailsMapper.selectGoodsDetailByGoodsId(goodsId);
    }

}
