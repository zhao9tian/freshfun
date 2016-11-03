package com.quxin.freshfun.service.promotion;

import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.param.PromotionParam;
import com.quxin.freshfun.utils.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface PromotionService {

	/**
	 * 获取商品的优惠价格
	 * @param goodsId
	 * @return
	 */
	PromotionGoodsPOJO queryDiscountPriceByGoodsId(Long goodsId) throws BusinessException;

	/**
	 * 添加优惠信息
	 * @param promotionGoodsPOJO
	 * @return
	 */
	Integer addDiscountForGoods(PromotionGoodsPOJO promotionGoodsPOJO) throws BusinessException;

}
