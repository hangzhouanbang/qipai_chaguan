package com.anbang.qipai.chaguan.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.ChaguanApply;
import com.anbang.qipai.chaguan.plan.dao.ChaguanApplyDao;

@Component
public class MongodbChaguanApplyDao implements ChaguanApplyDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addApply(ChaguanApply chaguanApply) {
		mongoTemplate.insert(chaguanApply);
	}

	@Override
	public ChaguanApply fingByApplyId(String applyId) {
		Query query = new Query(Criteria.where("id").is(applyId));
		return mongoTemplate.findOne(query, ChaguanApply.class);
	}

	@Override
	public ChaguanApply fingByAgentIdAndStatus(String agentId, String status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("status").is(status));
		return mongoTemplate.findOne(query, ChaguanApply.class);
	}

	@Override
	public void updateApplyStatus(String applyId, String status) {
		Query query = new Query(Criteria.where("id").is(applyId));
		Update update = new Update();
		update.set("status", status);
		mongoTemplate.updateFirst(query, update, ChaguanApply.class);
	}

}
