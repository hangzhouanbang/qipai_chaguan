package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;

public interface AgentDboDao {

	void addAgentDbo(AgentDbo agentDbo);

	void updateBaseInfo(String agentId, String nickname, String headimgurl, String gender);

	AgentDbo findAgentDboByAgentId(String agentId);
}
