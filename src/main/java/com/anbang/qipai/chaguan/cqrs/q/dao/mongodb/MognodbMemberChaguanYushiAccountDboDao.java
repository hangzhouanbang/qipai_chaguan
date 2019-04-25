package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.MemberChaguanYushiAccountDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;

@Component
public class MognodbMemberChaguanYushiAccountDboDao implements MemberChaguanYushiAccountDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(MemberChaguanYushiAccountDbo account) {
		mongoTemplate.insert(account);
	}

	@Override
	public List<MemberChaguanYushiAccountDbo> findByAgentIdAndChaguanIdAndBalanceLessThan(String agentId,
			String chaguanId, int balance, int page, int size) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("balance").lt(balance));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, MemberChaguanYushiAccountDbo.class);
	}

	@Override
	public long countByAgentIdAndChaguanIdAndBalanceLessThan(String agentId, String chaguanId, int balance) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("balance").lt(balance));
		return mongoTemplate.count(query, MemberChaguanYushiAccountDbo.class);
	}

	@Override
	public void updateBalance(String id, int balance) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("balance", balance);
		mongoTemplate.updateFirst(query, update, MemberChaguanYushiAccountDbo.class);
	}

	@Override
	public void updateMemberId(String id, String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("memberId", memberId);
		mongoTemplate.updateFirst(query, update, MemberChaguanYushiAccountDbo.class);
	}

	@Override
	public void updateAgentId(String id, String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("agentId", agentId);
		mongoTemplate.updateFirst(query, update, MemberChaguanYushiAccountDbo.class);
	}

	@Override
	public void remove(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query, MemberChaguanYushiAccountDbo.class);
	}

	@Override
	public MemberChaguanYushiAccountDbo findByAccountId(String accountId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(accountId));
		return mongoTemplate.findOne(query, MemberChaguanYushiAccountDbo.class);
	}

	@Override
	public MemberChaguanYushiAccountDbo findByChaguanIdAndMemberId(String chaguanId, String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("memberId").is(memberId));
		return mongoTemplate.findOne(query, MemberChaguanYushiAccountDbo.class);
	}

	@Override
	public MemberChaguanYushiAccountDbo findByAgentIdAndMemberId(String agentId, String memberId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("memberId").is(memberId));
		return mongoTemplate.findOne(query, MemberChaguanYushiAccountDbo.class);
	}

}
