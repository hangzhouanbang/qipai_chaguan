package com.anbang.qipai.chaguan.plan.bean;

/**
 * 馆主申请开通茶馆
 * 
 * @author lsc
 *
 */
public class ChaguanApply {
	private String id;
	private String agentId;// 申请人
	private String status;// 申请状态
	private long createTime;// 申请时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
