package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AuthorizationDbo;

@Component
public class MongodbAuthorizationDboDao implements AuthorizationDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public AuthorizationDbo findAuthorizationDboByPublisherAndUuid(String publisher, String uuid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("publisher").is(publisher));
		query.addCriteria(Criteria.where("uuid").is(uuid));
		return mongoTemplate.findOne(query, AuthorizationDbo.class);
	}

	@Override
	public AuthorizationDbo findAuthorizationDboByAgentId(String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		return mongoTemplate.findOne(query, AuthorizationDbo.class);
	}

	@Override
	public void addAuthorizationDbo(AuthorizationDbo authDbo) {
		mongoTemplate.insert(authDbo);
	}

}
