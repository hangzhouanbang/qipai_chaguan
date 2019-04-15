package com.anbang.qipai.chaguan.plan.dao;

import com.anbang.qipai.chaguan.plan.bean.game.LawsMutexGroup;

public interface LawsMutexGroupDao {

	void save(LawsMutexGroup lawsMutexGroup);

	void remove(String id);

}
