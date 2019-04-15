package com.anbang.qipai.chaguan.web.vo;

public class FreeReportVO {
	private String accountId;// 玩家玉石账户ID
	private String memberId;// 玩家ID
	private String nickname;// 玩家昵称
	private int freeCount;// 免费局数
	private int cost;// 已消耗

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getFreeCount() {
		return freeCount;
	}

	public void setFreeCount(int freeCount) {
		this.freeCount = freeCount;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}
