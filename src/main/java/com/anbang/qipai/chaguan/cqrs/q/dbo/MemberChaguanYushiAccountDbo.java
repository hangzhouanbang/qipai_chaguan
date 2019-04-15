package com.anbang.qipai.chaguan.cqrs.q.dbo;

/**
 * 玩家茶馆玉石账户
 * 
 * @author lsc
 *
 */
public class MemberChaguanYushiAccountDbo {

	private String id;

	private String memberId;// 玩家id

	private String agentId;// 馆主id

	private String chaguanId;// 茶馆id

	private int balance;// 余额

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getChaguanId() {
		return chaguanId;
	}

	public void setChaguanId(String chaguanId) {
		this.chaguanId = chaguanId;
	}
}
