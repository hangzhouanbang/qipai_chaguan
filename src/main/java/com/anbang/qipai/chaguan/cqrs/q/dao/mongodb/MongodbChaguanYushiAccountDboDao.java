package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanYushiAccountDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiAccountDbo;

@Component
public class MongodbChaguanYushiAccountDboDao implements ChaguanYushiAccountDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(ChaguanYushiAccountDbo account) {
		mongoTemplate.insert(account);
	}

	@Override
	public ChaguanYushiAccountDbo findByAgentId(String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		return mongoTemplate.findOne(query, ChaguanYushiAccountDbo.class);
	}

	@Override
	public void updateBalance(String id, int balance) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("balance", balance);
		mongoTemplate.updateFirst(query, update, ChaguanYushiAccountDbo.class);
	}

	@Override
	public void updateAgentId(String id, String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("agentId", agentId);
		mongoTemplate.updateFirst(query, update, ChaguanYushiAccountDbo.class);
	}

	@Override
	public void remove(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query, ChaguanYushiAccountDbo.class);
	}

}
