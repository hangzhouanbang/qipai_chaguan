package com.anbang.qipai.chaguan.cqrs.q.dbo;

/**
 * 茶馆成员
 * 
 * @author lsc
 *
 */
public class ChaguanMemberDbo {
	private String id;
	private String memberId;// 玩家id
	private String headimgurl;// 玩家头像
	private String memberNickname;// 玩家昵称
	private String chaguanId;// 茶馆id
	private String agentId;// 馆主id
	private String onlineStatus;// 在线状态
	private int chaguanYushi;// 茶馆玉石
	private String payType;// 支付方式
	private long joinTime;// 加入时间

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

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getMemberNickname() {
		return memberNickname;
	}

	public void setMemberNickname(String memberNickname) {
		this.memberNickname = memberNickname;
	}

	public String getChaguanId() {
		return chaguanId;
	}

	public void setChaguanId(String chaguanId) {
		this.chaguanId = chaguanId;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public int getChaguanYushi() {
		return chaguanYushi;
	}

	public void setChaguanYushi(int chaguanYushi) {
		this.chaguanYushi = chaguanYushi;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public long getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(long joinTime) {
		this.joinTime = joinTime;
	}

}
