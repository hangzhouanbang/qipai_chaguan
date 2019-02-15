package com.anbang.qipai.chaguan.plan.bean;

/**
 * 茶馆成员申请
 * 
 * @author lsc
 *
 */
public class ChaguanMemberApply {
	private String id;
	private String chaguanId;
	private String memberId;// 玩家id
	private String headimgurl;// 头像
	private String nickname;// 昵称
	private long createTime;// 申请时间
	private String state;// 申请状态

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChaguanId() {
		return chaguanId;
	}

	public void setChaguanId(String chaguanId) {
		this.chaguanId = chaguanId;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
