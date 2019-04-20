package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.plan.bean.AgentType;

public interface AgentDboDao {

	void addAgentDbo(AgentDbo agentDbo);

	void updateBaseInfo(String agentId, String nickname, String headimgurl, String gender);

	AgentDbo findAgentDboByAgentId(String agentId);

	void updateAgentDboType(String agentId, AgentType type);

	void updateAgentAuth(String agentId, boolean agentAuth);

	void updateAgentInviteMemberNum(String agentId, int inviteMemberNum);

	void updateAgentState(String agentId, String state);
}
