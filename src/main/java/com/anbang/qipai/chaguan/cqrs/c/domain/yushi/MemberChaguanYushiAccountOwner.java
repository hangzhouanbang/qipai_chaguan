package com.anbang.qipai.chaguan.cqrs.c.domain.yushi;

import com.dml.accounting.AccountOwner;

/**
 * 茶馆玉石持有者
 * 
 * @author lsc
 *
 */
public class MemberChaguanYushiAccountOwner implements AccountOwner {

	private String memberId;// 玩家

	private String agentId;// 馆主

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
