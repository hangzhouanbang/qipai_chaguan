package com.anbang.qipai.chaguan.plan.dao;

import com.anbang.qipai.chaguan.plan.bean.ChaguanApply;

public interface ChaguanApplyDao {

	void addApply(ChaguanApply chaguanApply);

	ChaguanApply fingByApplyId(String applyId);

	ChaguanApply fingByAgentIdAndStatus(String agentId, String status);

	void updateApplyStatus(String applyId, String status);
}
