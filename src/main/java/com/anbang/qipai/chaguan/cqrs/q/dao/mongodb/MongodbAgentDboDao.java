package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.AgentDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.plan.bean.AgentType;

@Component
public class MongodbAgentDboDao implements AgentDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addAgentDbo(AgentDbo agentDbo) {
		mongoTemplate.insert(agentDbo);
	}

	@Override
	public void updateBaseInfo(String agentId, String nickname, String headimgurl, String gender) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(agentId));
		Update update = new Update();
		update.set("nickname", nickname);
		update.set("headimgurl", headimgurl);
		update.set("gender", gender);
		mongoTemplate.updateFirst(query, update, AgentDbo.class);
	}

	@Override
	public AgentDbo findAgentDboByAgentId(String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(agentId));
		return mongoTemplate.findOne(query, AgentDbo.class);
	}

	@Override
	public void updateAgentAuth(String agentId, boolean agentAuth) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(agentId));
		Update update = new Update();
		update.set("agentAuth", agentAuth);
		mongoTemplate.updateFirst(query, update, AgentDbo.class);
	}

	@Override
	public void updateAgentState(String agentId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(agentId));
		Update update = new Update();
		update.set("state", state);
		mongoTemplate.updateFirst(query, update, AgentDbo.class);
	}

	@Override
	public void updateAgentDboType(String agentId, AgentType type) {
		Query query = new Query(Criteria.where("id").is(agentId));
		Update update = new Update();
		update.set("agentType", type);
		mongoTemplate.updateFirst(query, update, AgentDbo.class);
	}

	@Override
	public void updateAgentInviteMemberNum(String agentId, int inviteMemberNum) {
		Query query = new Query(Criteria.where("id").is(agentId));
		Update update = new Update();
		update.set("inviteMemberNum", inviteMemberNum);
		mongoTemplate.updateFirst(query, update, AgentDbo.class);
	}

}
