package com.anbang.qipai.chaguan.cqrs.q.dbo;

/**
 * 玩家加入的茶馆
 * 
 * @author lsc
 *
 */
public class MemberChaguanDbo {

	private String id;
	private String memberId;// 玩家id
	private String chaguanId;// 茶馆id
	private String chaguanName;// 茶馆名称
	private String chaguanDesc;// 茶馆描述
	private String agentId;// 馆主id

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

	public String getChaguanId() {
		return chaguanId;
	}

	public void setChaguanId(String chaguanId) {
		this.chaguanId = chaguanId;
	}

	public String getChaguanName() {
		return chaguanName;
	}

	public void setChaguanName(String chaguanName) {
		this.chaguanName = chaguanName;
	}

	public String getChaguanDesc() {
		return chaguanDesc;
	}

	public void setChaguanDesc(String chaguanDesc) {
		this.chaguanDesc = chaguanDesc;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

}
