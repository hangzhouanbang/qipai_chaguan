package com.anbang.qipai.chaguan.web.vo;

public class ChaguanVO {
	private String id;// 茶馆id
	private String headimgurl;// 头像url
	private String name;// 茶馆名称
	private String desc;// 茶馆描述
	private int memberNum;// 玩家人数
	private int balance;// 茶馆玉石
	private int onlineAmount;
	private boolean join;// 是否加入

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public int getOnlineAmount() {
		return onlineAmount;
	}

	public void setOnlineAmount(int onlineAmount) {
		this.onlineAmount = onlineAmount;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

}
