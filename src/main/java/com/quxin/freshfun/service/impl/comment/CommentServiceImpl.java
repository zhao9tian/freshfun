package com.quxin.freshfun.service.impl.comment;

import com.quxin.freshfun.dao.CommentMapper;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.model.pojo.CommentPOJO;
import com.quxin.freshfun.service.comment.CommentService;
import com.quxin.freshfun.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

	private static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

	@Autowired
	private CommentMapper commentMapper;

	@Override
	public Integer addComment(CommentPOJO commentPOJO) {

		// 参数校验
		ValidateUtil validateResult = validate(commentPOJO);
		if (validateResult.isSuc() == false) {
			logger.error(validateResult.getMsg());
			return null;
		}

		// 初始化参数
		initial(commentPOJO);

		Integer id = commentMapper.insert(commentPOJO);
		return id;
	}

	@Override
	public CommentPOJO queryCommentDetailByOrderId(Long orderId) {

		if (orderId == null || orderId == 0) {
			logger.error("入参不能为空");
			return null;
		}

		CommentPOJO commentPOJO = commentMapper.selectCommentDetailByOrderId(orderId);

		return commentPOJO;
	}

	/**
	 * 初始化参数
	 * @param commentPOJO
	 */
	private void initial(CommentPOJO commentPOJO) {
		if (commentPOJO.getContent() == null) {
			commentPOJO.setContent("");
		}
		if (commentPOJO.getGeneralLevel() == null) {
			commentPOJO.setGeneralLevel(0);
		}
		if (commentPOJO.getLogisticsLevel() == null) {
			commentPOJO.setLogisticsLevel(0);
		}
		if (commentPOJO.getPackLevel() == null) {
			commentPOJO.setPackLevel(0);
		}
		if (commentPOJO.getTasteLevel() == null) {
			commentPOJO.setTasteLevel(0);
		}
	}

	/**
	 * 参数校验
	 * @param commentPOJO
	 * @return
	 */
	private ValidateUtil validate(CommentPOJO commentPOJO) {

		if(commentPOJO.getUserId() == null || commentPOJO.getUserId() == 0) {
			return ValidateUtil.fail("用户ID不能为空");
		}
		if(commentPOJO.getOrderId() == null || commentPOJO.getOrderId() == 0) {
			return ValidateUtil.fail("订单ID不能为空");
		}
		if(commentPOJO.getGoodsId() == null || commentPOJO.getGoodsId() == 0) {
			return ValidateUtil.fail("商品ID不能为空");
		}

		return ValidateUtil.success();
	}
}
