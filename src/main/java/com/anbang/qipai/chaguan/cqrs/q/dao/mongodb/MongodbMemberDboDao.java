package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;

@Component
public class MongodbMemberDboDao implements MemberDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(MemberDbo dbo) {
		mongoTemplate.insert(dbo);
	}

	@Override
	public MemberDbo findById(String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(memberId));
		return mongoTemplate.findOne(query, MemberDbo.class);
	}

	@Override
	public void updateMemberBaseInfo(String memberId, String nickname, String headimgurl, String gender) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(memberId));
		Update update = new Update();
		update.set("nickname", nickname);
		update.set("headimgurl", headimgurl);
		update.set("gender", gender);
		mongoTemplate.updateFirst(query, update, MemberDbo.class);
	}

	@Override
	public void remove(String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(memberId));
		mongoTemplate.remove(query, MemberDbo.class);
	}

}
