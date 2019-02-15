package com.anbang.qipai.chaguan.cqrs.c.domain.chaguan;

public class CreateChaguanYushiAccountResult {

	private String accountId;
	private String agentId;

	public CreateChaguanYushiAccountResult(String accountId, String agentId) {
		this.accountId = accountId;
		this.agentId = agentId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

}
