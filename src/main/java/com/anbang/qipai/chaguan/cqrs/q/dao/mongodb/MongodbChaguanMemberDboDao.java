package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanMemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanMemberDbo;

@Component
public class MongodbChaguanMemberDboDao implements ChaguanMemberDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void add(ChaguanMemberDbo dbo) {
		mongoTemplate.insert(dbo);
	}

	@Override
	public long countOnlineMemberByChaguanId(String chaguanId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("onlineStatus").is("online"));
		query.addCriteria(Criteria.where("remove").is(false));
		return mongoTemplate.count(query, ChaguanMemberDbo.class);
	}

	@Override
	public long countAmountByChaguanId(String chaguanId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("remove").is(false));
		return mongoTemplate.count(query, ChaguanMemberDbo.class);
	}

	@Override
	public List<ChaguanMemberDbo> findByChaguanId(int page, int size, String chaguanId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.skip((page - 1) * size);
		query.limit(size);
		query.addCriteria(Criteria.where("remove").is(false));
		return mongoTemplate.find(query, ChaguanMemberDbo.class);
	}

	@Override
	public long getAmountByMemberId(String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("remove").is(false));
		return mongoTemplate.count(query, ChaguanMemberDbo.class);
	}

	@Override
	public List<ChaguanMemberDbo> findByMemberId(int page, int size, String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("remove").is(false));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, ChaguanMemberDbo.class);
	}

	@Override
	public ChaguanMemberDbo findByMemberIdAndChaguanId(String memberId, String chaguanId, boolean remove) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("remove").is(remove));
		return mongoTemplate.findOne(query, ChaguanMemberDbo.class);
	}

	@Override
	public void updateChaguanMemberDboRemoveByMemberIdAndChaguanId(String memberId, String chaguanId, boolean remove) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		Update update = new Update();
		update.set("remove", remove);
		mongoTemplate.updateFirst(query, update, ChaguanMemberDbo.class);
	}

	@Override
	public void updateChaguanMemberDboChaguanNameAndDescByChaguanId(String chaguanId, String chaguanName,
			String chaguanDesc) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		Update update = new Update();
		update.set("chaguanName", chaguanName);
		update.set("chaguanDesc", chaguanDesc);
		mongoTemplate.updateMulti(query, update, ChaguanMemberDbo.class);
	}

	@Override
	public void updateChaguanMemberDboMemberDescByMemberIdAndChaguanId(String memberId, String chaguanId,
			String memberDesc) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		Update update = new Update();
		update.set("memberDesc", memberDesc);
		mongoTemplate.updateFirst(query, update, ChaguanMemberDbo.class);
	}

	@Override
	public void updateChaguanMemberDboPayTypeByMemberIdAndChaguanId(String memberId, String chaguanId, String payType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		Update update = new Update();
		update.set("payType", payType);
		mongoTemplate.updateFirst(query, update, ChaguanMemberDbo.class);
	}

	@Override
	public void updateChaguanMemberDboOnlineStatusByMemberId(String memberId, String onlineStatus) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		Update update = new Update();
		update.set("onlineStatus", onlineStatus);
		mongoTemplate.updateFirst(query, update, ChaguanMemberDbo.class);
	}

}
