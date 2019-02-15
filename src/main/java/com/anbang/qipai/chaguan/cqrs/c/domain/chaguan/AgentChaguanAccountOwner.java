package com.anbang.qipai.chaguan.cqrs.c.domain.chaguan;

import com.dml.accounting.AccountOwner;

public class AgentChaguanAccountOwner implements AccountOwner {

	private String agentId;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

}
