package com.quxin.freshfun.mongodb.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.Share;
import com.quxin.freshfun.mongodb.MongoCRUD;
import com.quxin.freshfun.utils.ReflectionUtils;
@Repository
public class MongoCRUDImpl<T> implements MongoCRUD<T> {
	
	private Class<T> genricType;
	public MongoCRUDImpl(){
		genricType = ReflectionUtils.getSuperClassGenricType(getClass());
	}
	@Autowired
	private MongoTemplate mongoTemplate;
	/** 
     * 通过条件查询实体(集合) 
     *  
     * @param query 
     */
	@Override
	public List<T> find(Query query) {
		return mongoTemplate.find(query, this.getEntityClass());
	}

	/** 
     * 通过一定的条件查询一个实体 
     *  
     * @param query 
     * @return 
     */
	@Override
	public T findOne(Query query) {
		return mongoTemplate.findOne(query, this.getEntityClass());
	}

	 /** 
     * 通过条件查询更新数据 
     *  
     * @param query 
     * @param update 
     * @return 
     */
	@Override
	public void update(Query query, Update update) {
		mongoTemplate.findAndModify(query, update, this.getEntityClass());
	}
	 /** 
     * 保存一个对象到mongodb 
     *  
     * @param entity 
     * @return 
     */
	@Override
	public T save(T entity) {
		mongoTemplate.insert(entity);
        return entity;
	}
	/** 
     * 通过ID获取记录 
     *  
     * @param id 
     * @return 
     */
	@Override
	public T findById(String id) {
		return mongoTemplate.findById(id, this.getEntityClass());
	}
	/** 
     * 通过ID获取记录,并且指定了集合名(表的意思) 
     *  
     * @param id 
     * @param collectionName 
     *            集合名 
     * @return 
     */
	@Override
	public T findById(String id, String collectionName) {
		return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
	}
	/** 
     * 求数据总和 
     * @param query 
     * @return 
     */  
	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, this.getEntityClass());
	}
	/** 
     * 获取需要操作的实体类class 
     *  
     * @return 
     */  
    private Class<T> getEntityClass(){  
        return  genricType;
    }

	@Override
	public List<Share> findAll() {
		List<Share> shareList = mongoTemplate.findAll(Share.class);
		return shareList;
	} 
}
