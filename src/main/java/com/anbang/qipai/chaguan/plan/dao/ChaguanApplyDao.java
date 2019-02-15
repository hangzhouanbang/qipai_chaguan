package com.anbang.qipai.chaguan.plan.dao;

import com.anbang.qipai.chaguan.plan.bean.ChaguanApply;

public interface ChaguanApplyDao {

	void addApply(ChaguanApply chaguanApply);

	ChaguanApply fingByApplyId(String applyId);

	void updateApplyStatus(String applyId, String status);
}
