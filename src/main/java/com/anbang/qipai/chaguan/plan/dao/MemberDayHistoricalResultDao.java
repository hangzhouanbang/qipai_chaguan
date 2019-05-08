package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.MemberDayHistoricalResult;

public interface MemberDayHistoricalResultDao {

	void save(MemberDayHistoricalResult result);

	long countByChaguanIdAndTime(String chaguanId, long startTime, long endTime);

	List<MemberDayHistoricalResult> findByChaguanIdAndTime(int page, int size, String chaguanId, long startTime,
			long endTime);

	MemberDayHistoricalResult findByPlayerIdAndTime(String playerId, long startTime, long endTime);

	void updateIncById(String id, int dayingjiaCount, int chaguanYushiCost, int totalScore, long createTime);
}
