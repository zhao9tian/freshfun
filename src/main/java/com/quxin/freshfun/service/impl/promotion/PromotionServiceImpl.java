package com.quxin.freshfun.service.impl.promotion;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.common.Constant;
import com.quxin.freshfun.dao.FlowMapper;
import com.quxin.freshfun.dao.PromotionMapper;
import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.param.PromotionParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;
import com.quxin.freshfun.model.pojo.PromotionPOJO;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.promotion.PromotionService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.nimbus.NimbusStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("promotionService")
public class PromotionServiceImpl implements PromotionService {

	private static Logger logger = LoggerFactory.getLogger(PromotionServiceImpl.class);

	@Autowired
	private PromotionMapper promotionMapper;

	@Override
	public PromotionGoodsPOJO queryDiscountPriceByGoodsId(Long goodsId) throws BusinessException {

		if (goodsId == null) {
			throw new BusinessException("请输入商品ID");
		}

		Map<String, Object> paramMap = new HashMap();

		paramMap.put("goodsId",goodsId);
		paramMap.put("now",System.currentTimeMillis()/1000);
		paramMap.put("objectType", Constant.OBJECT_TYPE_TIME_LIMIT);

		PromotionGoodsPOJO promotionGoodsPOJO = new PromotionGoodsPOJO();
		// 是否打折
		boolean isDiscount = false;

		// 获取商品优惠信息
		PromotionPOJO promotionInfo = promotionMapper.selectPromotionInfo(paramMap);

		if (promotionInfo != null) {
			// 打折
			isDiscount = true;
			// 获取优惠价格
			String content = promotionInfo.getContent();
			Map<String,	String> contentMap = (Map) JSON.parse(content);
			if (contentMap == null) {
				throw new BusinessException("优惠价格解析失败");
			}
			Long discountPrice = Long.parseLong(contentMap.get("discountPrice"));
			// 优惠价格
			promotionGoodsPOJO.setDiscountPrice(discountPrice);
			// 优惠开始时间
			promotionGoodsPOJO.setStartTime(promotionInfo.getStartTime());
			// 优惠结束书剑
			promotionGoodsPOJO.setEndTime(promotionInfo.getEndTime());

		}
		// 商品ID
		promotionGoodsPOJO.setGoodsId(goodsId);
		// 是否有折扣
		promotionGoodsPOJO.setDiscount(isDiscount);

		return promotionGoodsPOJO;
	}

	@Override
	public Integer addDiscountForGoods(PromotionGoodsPOJO promotionGoodsPOJO) throws BusinessException {

		Long goodsId = promotionGoodsPOJO.getGoodsId();
		if (goodsId == null) {
			throw new BusinessException("入参商品ID不能为空");
		}
		Long discountPrice = promotionGoodsPOJO.getDiscountPrice();
		if (discountPrice == null) {
			throw new BusinessException("入参优惠价格不能为空");
		}
		Long startTime = promotionGoodsPOJO.getStartTime();
		if (startTime == null) {
			throw new BusinessException("入参优惠开始时间不能为空");
		}
		Long endTime = promotionGoodsPOJO.getEndTime();
		if (endTime == null) {
			throw new BusinessException("入参优惠结束时间不能为空");
		}

		PromotionParam insertParam = new PromotionParam();
		insertParam.setObjectId(goodsId);
		insertParam.setObjectType(Constant.OBJECT_TYPE_TIME_LIMIT);

		Map<String,String> map = new HashMap();
		map.put("discountPrice",discountPrice.toString());
		String discountContent = JSON.toJSONString(map);
		insertParam.setContent(discountContent);

		insertParam.setStartTime(startTime);
		insertParam.setEndTime(endTime);

		Integer res = promotionMapper.insert(insertParam);
		if (res == null || res == 0) {
			throw new BusinessException("添加优惠信息失败");
		}
		return res;
	}

}