package com.anbang.qipai.chaguan.cqrs.q.dbo;

/**
 * 玩家
 * 
 * @author lsc
 *
 */
public class MemberDbo {
	private String id;// 会员id
	private String nickname;// 会员昵称
	private String gender;// 会员性别:男:male,女:female
	private String headimgurl;// 头像url
	private String onlineStatus;// 在线状态

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

}
