package com.quxin.freshfun.service.impl.goods;


import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.Share;
import com.quxin.freshfun.mongodb.MongoShare;
import com.quxin.freshfun.service.goods.ShareGoodsService;
@Service("shareGoods")
public class ShareGoodsServiceImpl implements ShareGoodsService {
	@Autowired
	private MongoShare mongoShare;
	/**
	 * 记录用户分享信息
	 * @param userId 分享用户编号
	 * @param code 编码后的信息
	 * @return
	 */
	@Override
	public Integer recordShareInfo(String userId, String code) {
		Share share = new Share(userId,code);
		Integer status = mongoShare.addShareLink(share);
		return status;
	}
	
	public static void main(String[] args) throws EncoderException {
		byte[] encode = Base64.encodeBase64Chunked("您好".getBytes());
		System.out.println(new String(encode));
		byte[] decode = Base64.decodeBase64(encode);
		System.out.println(new String(decode));
	}
}