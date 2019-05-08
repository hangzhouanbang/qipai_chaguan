package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.MemberDayResult;

public interface MemberDayResultDao {

	void save(MemberDayResult result);

	long countByChaguanIdAndTime(String chaguanId, long startTime, long endTime);

	List<MemberDayResult> findByChaguanIdAndTime(int page, int size, String chaguanId, long startTime, long endTime);

	MemberDayResult findByPlayerIdAndTime(String playerId, long startTime, long endTime);

	MemberDayResult findById(String id);

	void updateIncById(String id, int dayingjiaCount, int chaguanYushiCost, int totalScore, long createTime);

	void removeById(String id);
}
