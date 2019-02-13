package com.anbang.qipai.chaguan.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.AgentDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;

@Service
public class AgentDboService {

	@Autowired
	private AgentDboDao agentDboDao;

	public void addAgentDbo(AgentDbo agentDbo) {
		agentDboDao.addAgentDbo(agentDbo);
	}

	public void updateBaseInfo(String agentId, String nickname, String headimgurl, String gender) {
		agentDboDao.updateBaseInfo(agentId, nickname, headimgurl, gender);
	}

	public AgentDbo findAgentDboByAgentId(String agentId) {
		return agentDboDao.findAgentDboByAgentId(agentId);
	}
}
