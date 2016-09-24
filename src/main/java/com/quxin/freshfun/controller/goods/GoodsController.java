package com.quxin.freshfun.controller.goods;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.quxin.freshfun.service.goods.ShareGoodsService;

@Controller
@RequestMapping("/")
public class GoodsController {
	@Autowired
	private ShareGoodsService shareGoodsService;
	/**
	 * 记录分享信息
	 * @param userId 用户编号
	 * @param code 编码后的字符串
	 * @return
	 */
	@RequestMapping(value="/recordShareInfo")
	@ResponseBody
	public Map<String, Integer> recordShareInfo(String userId,String code){
		Integer shareStatus = shareGoodsService.recordShareInfo(userId,code);
		Map<String, Integer> map = Maps.newHashMap();
		map.put("status", shareStatus);
		return map;
	}
}