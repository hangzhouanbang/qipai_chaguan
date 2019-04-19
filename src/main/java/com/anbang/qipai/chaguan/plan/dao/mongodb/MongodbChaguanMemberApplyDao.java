package com.anbang.qipai.chaguan.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApply;
import com.anbang.qipai.chaguan.plan.dao.ChaguanMemberApplyDao;

@Component
public class MongodbChaguanMemberApplyDao implements ChaguanMemberApplyDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void add(ChaguanMemberApply apply) {
		mongoTemplate.insert(apply);
	}

	@Override
	public ChaguanMemberApply findById(String applyId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(applyId));
		return mongoTemplate.findOne(query, ChaguanMemberApply.class);
	}

	@Override
	public ChaguanMemberApply findByMemberIdAndChaguanIdAndState(String memberId, String chaguanId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("state").is(state));
		return mongoTemplate.findOne(query, ChaguanMemberApply.class);
	}

	@Override
	public long countByChaguanIdAndState(String chaguanId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("state").is(state));
		return mongoTemplate.count(query, ChaguanMemberApply.class);
	}

	@Override
	public List<ChaguanMemberApply> findByChaguanIdAndState(String chaguanId, String state, int page, int size) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("state").is(state));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, ChaguanMemberApply.class);
	}

	@Override
	public void updateState(String applyId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(applyId));
		Update update = new Update();
		update.set("state", state);
		mongoTemplate.updateFirst(query, update, ChaguanMemberApply.class);
	}

}
