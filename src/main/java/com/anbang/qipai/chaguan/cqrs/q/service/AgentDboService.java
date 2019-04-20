package com.anbang.qipai.chaguan.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.AgentDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AuthorizationDbo;

@Service
public class AgentDboService {

	@Autowired
	private AgentDboDao agentDboDao;

	@Autowired
	private AuthorizationDboDao authorizationDboDao;

	public AuthorizationDbo findThirdAuthorizationDbo(String publisher, String uuid) {
		return authorizationDboDao.findAuthorizationDboByPublisherAndUuid(publisher, uuid);
	}

	public AuthorizationDbo findThirdAuthorizationDboByAgentId(String agentId, String publisher) {
		return authorizationDboDao.findAuthorizationDboByAgentId(agentId, publisher, true);
	}

	public void addAuthorizationDbo(AuthorizationDbo authDbo) {
		authorizationDboDao.addAuthorizationDbo(authDbo);
	}

	public void addAgentDbo(AgentDbo agentDbo) {
		agentDboDao.addAgentDbo(agentDbo);
	}

	public void updateBaseInfo(String agentId, String nickname, String headimgurl, String gender) {
		agentDboDao.updateBaseInfo(agentId, nickname, headimgurl, gender);
	}

	public AgentDbo findAgentDboByAgentId(String agentId) {
		return agentDboDao.findAgentDboByAgentId(agentId);
	}

	public void updateAgentAuth(String agentId, boolean agentAuth) {
		agentDboDao.updateAgentAuth(agentId, agentAuth);
	}

	public void updateAgentState(String agentId, String state) {
		agentDboDao.updateAgentState(agentId, state);
	}
}
