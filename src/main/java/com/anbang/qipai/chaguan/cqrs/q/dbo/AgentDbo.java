package com.anbang.qipai.chaguan.cqrs.q.dbo;

import com.anbang.qipai.chaguan.plan.bean.AgentType;

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
	private AgentType agentType;// 推广员类型
	private boolean agentAuth;// 是否通过推广员申请
	private String state;// 正常,封停
	private int inviteMemberNum;// 邀请玩家数量

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

	public AgentType getAgentType() {
		return agentType;
	}

	public void setAgentType(AgentType agentType) {
		this.agentType = agentType;
	}

	public boolean isAgentAuth() {
		return agentAuth;
	}

	public void setAgentAuth(boolean agentAuth) {
		this.agentAuth = agentAuth;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getInviteMemberNum() {
		return inviteMemberNum;
	}

	public void setInviteMemberNum(int inviteMemberNum) {
		this.inviteMemberNum = inviteMemberNum;
	}

}
