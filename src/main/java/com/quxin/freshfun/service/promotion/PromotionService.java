package com.quxin.freshfun.service.promotion;

import com.quxin.freshfun.model.OrderPayInfo;
import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.param.PromotionParam;
import com.quxin.freshfun.model.pojo.PromotionPOJO;
import com.quxin.freshfun.utils.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface PromotionService {

	/**
	 * 获取商品的优惠价格
	 * @param goodsId
	 * @return
	 */
	PromotionGoodsPOJO queryDiscountPriceByGoodsId(Long goodsId) throws BusinessException;

	/**
	 * 获取限量商品价格
	 * @return
	 */
	List<PromotionGoodsPOJO> queryLimitedGoods(List<OrderPayInfo> goodsBaseList) throws BusinessException;

	/**
	 * 添加优惠信息
	 * @param promotionGoodsPOJO
	 * @return
	 */
	Integer addDiscountForGoods(PromotionGoodsPOJO promotionGoodsPOJO) throws BusinessException;

	/**
	 * 根据商品编号查询限量库存
	 * @param goodsBaseList
	 * @return
	 */
	List<PromotionPOJO> selectStockByGoodsList(List<OrderPayInfo> goodsBaseList) throws BusinessException;

	/**
	 * 修改限量商品库存
	 * @param map 库存信息
	 * @return
	 */
	int updateStockById(Map<String,Object> map) throws BusinessException;

}
