package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;

@Component
public class MongodbChaguanDboDao implements ChaguanDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addChaguanDbo(ChaguanDbo dbo) {
		mongoTemplate.insert(dbo);
	}

	@Override
	public ChaguanDbo findById(String chaguanId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(chaguanId));
		return mongoTemplate.findOne(query, ChaguanDbo.class);
	}

	@Override
	public void updateChaguanBaseInfo(String chaguanId, String name, String desc) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(chaguanId));
		Update update = new Update();
		update.set("name", name);
		update.set("desc", desc);
		mongoTemplate.updateFirst(query, update, ChaguanDbo.class);
	}

	@Override
	public void updateChaguanDboMemberNum(String chaguanId, int memberNum) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(chaguanId));
		Update update = new Update();
		update.set("memberNum", memberNum);
		mongoTemplate.updateFirst(query, update, ChaguanDbo.class);
	}

	@Override
	public long countAmountByAgentId(String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		return mongoTemplate.count(query, ChaguanDbo.class);
	}

	@Override
	public List<ChaguanDbo> findByAgentId(int page, int size, String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, ChaguanDbo.class);
	}

}
