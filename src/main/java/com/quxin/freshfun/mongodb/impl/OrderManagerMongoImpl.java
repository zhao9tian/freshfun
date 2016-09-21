package com.quxin.freshfun.mongodb.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.quxin.freshfun.model.OrderInPieces;
import com.quxin.freshfun.model.OrdersPOJO;
import com.quxin.freshfun.mongodb.OrderManagerMongo;
@Repository
public class OrderManagerMongoImpl implements OrderManagerMongo {
	//待收货
	public static final String STAYR_DELIVERY_GOODS_ORDER = "0";
	public static final String STAYR_DELIVERY_GOODS_ORDER2 = "1";
	//待发货
	public static final String STAYR_ECEIVE_ORDER = "2";
	//待评价
	public static final String STAYR_COMMENT_STATUS = "0";
	//退货状态
	public static final String STAYR_REFUND_STATUS = "0";
	public static final String STAYR_REFUND_FINISH_STATUS = "1";
	//是否删除
	public static final String IS_DELETE = "0";
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void addOrderDetail(OrderInPieces order) {
		mongoTemplate.insert(order,"OrderInPieces");
	}

	@Override
	public int delOrder(Integer orderId) {
		WriteResult updateFirst = mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(orderId)), Update.update("isDeleted", "1"), OrderInPieces.class);
		int n = updateFirst.getN();
		System.out.println(n);
		return n;
	}

	@Override
	public List<OrdersPOJO> findAll(Integer userId, int currentPage,
			int pageSize) {
		return null;
	}
	/**
	 * 根据用户编号查询待收货订单
	 */
	@Override
	public List<OrderInPieces> selectObligationOrder(String userId,
			Integer currentPage, Integer pageSize) {
		Criteria criteria = Criteria.where("userId").is(userId).and("isDeleted").is(IS_DELETE);
		Query query = new Query().addCriteria(criteria);
		query.with(new Sort(Sort.Direction.DESC,"createDate"));
		int skip = (currentPage - 1)*pageSize;
		query.skip(skip);
		query.limit(pageSize);
		List<OrderInPieces> orders = mongoTemplate.find(query, OrderInPieces.class,"OrderInPieces");
		return orders;
	}
	/**
	 * 根据用户编号查询待收货订单
	 */
	@Override
	public List<OrderInPieces> selectStayReceiveOrder(String userId,Integer currentPage,Integer pageSize) {
		Query query = new Query(Criteria.where("userId").is(userId).and("orderStatus").is(STAYR_ECEIVE_ORDER).and("isDeleted").is(IS_DELETE));
		query.with(new Sort(Sort.Direction.DESC,"createDate"));
		int skip = (currentPage - 1)*pageSize;
		query.skip(skip);
		query.limit(pageSize);
		List<OrderInPieces> orders = mongoTemplate.find(query, OrderInPieces.class,"OrderInPieces");
		return orders;
	}
	/**
	 * 根据用户编号查询待收货订单
	 */
	@Override
	public List<OrderInPieces> selectStayDeliveryGoodsOrders(String userId,Integer currentPage,Integer pageSize) {
		List<String> listStatus = new ArrayList<>();
		listStatus.add(STAYR_DELIVERY_GOODS_ORDER);
		listStatus.add(STAYR_DELIVERY_GOODS_ORDER2);
		Query query = new Query(Criteria.where("userId").is(userId).and("orderStatus").in(listStatus).and("isDeleted").is(IS_DELETE));
		query.with(new Sort(Sort.Direction.DESC,"createDate"));
		int skip = (currentPage - 1)*pageSize;
		query.skip(skip);
		query.limit(pageSize);
		List<OrderInPieces> orders = mongoTemplate.find(query, OrderInPieces.class,"OrderInPieces");
		return orders;
	}
	/**
	 * 根据用户编号查询待收货订单
	 */
	@Override
	public List<OrderInPieces> selectStayAssessOrder(String userId,Integer currentPage,Integer pageSize) {
		Query query = new Query(Criteria.where("userId").is(userId).and("commentStatus").is(STAYR_COMMENT_STATUS).and("isDeleted").is(IS_DELETE));
		query.with(new Sort(Sort.Direction.DESC,"createDate"));
		int skip = (currentPage - 1)*pageSize;
		query.skip(skip);
		query.limit(pageSize);
		List<OrderInPieces> orders = mongoTemplate.find(query, OrderInPieces.class,"OrderInPieces");
		return orders;
	}
	/**
	 * 根据用户编号查询待收货订单
	 */
	@Override
	public List<OrderInPieces> selectCancelOrder(String userId,Integer currentPage,Integer pageSize) {
		List<String> refundList = new ArrayList<>();
		refundList.add(STAYR_REFUND_STATUS);
		refundList.add(STAYR_REFUND_FINISH_STATUS);
		Query query = new Query(Criteria.where("userId").is(userId).and("refundStatus").in(refundList).and("isDeleted").is(IS_DELETE));
		query.with(new Sort(Sort.Direction.DESC,"createDate"));
		int skip = (currentPage - 1)*pageSize;
		query.skip(skip);
		query.limit(pageSize);
		List<OrderInPieces> orderList = mongoTemplate.find(query, OrderInPieces.class,"OrderInPieces");
		return orderList;
	}
	/**
	 * 修改订单状态
	 */
	@Override
	public int updateOrderDetailPayStatus(String orderId) {
		Query query = new Query(Criteria.where("orderId").is(orderId));
		WriteResult result = mongoTemplate.upsert(query, Update.update("orderStatus", "1"), OrderInPieces.class,"OrderInPieces");
		return result.getN();
	}

}
