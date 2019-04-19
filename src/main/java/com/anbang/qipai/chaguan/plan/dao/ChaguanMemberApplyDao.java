package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApply;

public interface ChaguanMemberApplyDao {

	void add(ChaguanMemberApply apply);

	ChaguanMemberApply findById(String applyId);

	ChaguanMemberApply findByMemberIdAndChaguanIdAndState(String memberId, String chaguanId, String state);

	long countByChaguanIdAndState(String chaguanId, String state);

	List<ChaguanMemberApply> findByChaguanIdAndState(String chaguanId, String state, int page, int size);

	void updateState(String applyId, String state);
}
