package com.anbang.qipai.chaguan.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.ChaguanShopOrder;
import com.anbang.qipai.chaguan.plan.dao.ChaguanShopOrderDao;

@Component
public class MongodbChaguanShopOrderDao implements ChaguanShopOrderDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addChaguanShopOrder(ChaguanShopOrder order) {
		mongoTemplate.insert(order);
	}

	@Override
	public void orderFinished(String id, String transaction_id, String status, long deliveTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("transaction_id", transaction_id);
		update.set("status", status);
		update.set("deliveTime", deliveTime);
		mongoTemplate.updateFirst(query, update, ChaguanShopOrder.class);
	}

	@Override
	public ChaguanShopOrder findChaguanShopOrderById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, ChaguanShopOrder.class);
	}

	@Override
	public ChaguanShopOrder findChaguanShopOrderByPayerIdAndProductName(String payerId, String productName) {
		Query query = new Query();
		query.addCriteria(Criteria.where("payerId").is(payerId));
		query.addCriteria(Criteria.where("productName").is(productName));
		return mongoTemplate.findOne(query, ChaguanShopOrder.class);
	}

}
