package com.anbang.qipai.chaguan.plan.dao;

import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApply;

public interface ChaguanMemberApplyDao {

	void add(ChaguanMemberApply apply);

	ChaguanMemberApply findById(String applyId);

	void updateState(String applyId, String state);
}
