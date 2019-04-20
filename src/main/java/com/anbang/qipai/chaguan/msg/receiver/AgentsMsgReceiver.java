package com.anbang.qipai.chaguan.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.AgentDboService;
import com.anbang.qipai.chaguan.msg.channel.sink.AgentsSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.google.gson.Gson;

@EnableBinding(AgentsSink.class)
public class AgentsMsgReceiver {

	@Autowired
	private AgentDboService agentDboService;

	private Gson gson = new Gson();

	@StreamListener(AgentsSink.AGENTS)
	public void recordAgent(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		AgentDbo agent = gson.fromJson(json, AgentDbo.class);
		if ("new agent".equals(msg)) {
			agentDboService.addAgentDbo(agent);
		}
		if ("apply pass".equals(msg)) {
			agentDboService.updateAgnetInfoAndAgentAuth(agent);
		}
		if ("update agent type".equals(msg)) {
			agentDboService.updateAgentDboType(agent.getId(), agent.getAgentType());
		}
		if ("update agent invitemembernum".equals(msg)) {
			agentDboService.updateAgentInviteMemberNum(agent.getId(), agent.getInviteMemberNum());
		}
		if ("ban agent".equals(msg)) {
			agentDboService.updateAgentState(agent.getId(), agent.getState());
		}
		if ("liberate agent".equals(msg)) {
			agentDboService.updateAgentState(agent.getId(), agent.getState());
		}
	}
}
