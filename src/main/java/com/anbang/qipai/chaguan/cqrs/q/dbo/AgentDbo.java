package com.anbang.qipai.chaguan.cqrs.q.dbo;

/**
 * 馆主
 * 
 * @author lsc
 *
 */
public class AgentDbo {
	private String id;// 推广员id
	private String nickname;// 推广员昵称
	private String gender;// 推广员性别:男:male,女:female
	private String headimgurl;// 头像url

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

}
