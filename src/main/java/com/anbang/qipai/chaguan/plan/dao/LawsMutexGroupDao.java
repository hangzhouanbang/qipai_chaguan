package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.game.LawsMutexGroup;

public interface LawsMutexGroupDao {

	void save(LawsMutexGroup lawsMutexGroup);

	void remove(String id);

	List<LawsMutexGroup> findLawsMutexGroupByConditions(int page, int size, LawsMutexGroup lawsMutexGroup);

	long getAmountByConditions(LawsMutexGroup lawsMutexGroup);
}
