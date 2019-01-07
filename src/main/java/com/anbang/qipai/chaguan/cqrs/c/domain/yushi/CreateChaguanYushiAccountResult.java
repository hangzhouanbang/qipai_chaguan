package com.anbang.qipai.chaguan.cqrs.c.domain.yushi;

/**
 * 创建茶馆玉石账户结果
 * 
 * @author lsc
 *
 */
public class CreateChaguanYushiAccountResult {

	private String accountId;
	private String memberId;
	private String agentId;

	public CreateChaguanYushiAccountResult(String accountId, String memberId, String agentId) {
		this.accountId = accountId;
		this.memberId = memberId;
		this.agentId = agentId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
}
