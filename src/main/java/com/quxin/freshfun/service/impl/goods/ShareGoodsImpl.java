package com.quxin.freshfun.service.impl.goods;


import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.Share;
import com.quxin.freshfun.mongodb.MongoCRUD;
import com.quxin.freshfun.mongodb.MongoShare;
import com.quxin.freshfun.service.goods.ShareGoods;
@Service
public class ShareGoodsImpl implements ShareGoods {
	@Autowired
	private MongoShare mongoShare;
	/**
	 * 记录用户分享信息
	 * @param user_id 分享用户编号
	 * @param goods_id	商品编号
	 * @param code 编码后的信息
	 * @return
	 */
	@Override
	public Integer recordShareInfo(String user_id, String code) {
		Share share = new Share(user_id,code);
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