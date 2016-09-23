package com.quxin.freshfun.service.goods;

import org.springframework.stereotype.Service;

@Service
public interface ShareGoods {
	/**
	 * 记录用户分享信息
	 * @param userId 分享用户编号
	 * @param code 编码后的标记
	 * @return
	 */
	public Integer recordShareInfo(String userId,String code);
}