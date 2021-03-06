package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalJuResult;

public interface GameHistoricalJuResultDao {

	void addGameHistoricalResult(GameHistoricalJuResult result);

	List<GameHistoricalJuResult> findGameHistoricalResultByMemberId(int page, int size, String memberId);

	long getAmountByMemberId(String memberId);

	int countGameNumByGameAndTime(Game game, long startTime, long endTime);

	GameHistoricalJuResult findGameHistoricalResultById(String id);

	List<GameHistoricalJuResult> findGameHistoricalResultByMemberIdAndChaguanIdAndRoomNoAndTime(int page, int size,
			String chaguanId, String memberId, String roomNo, long startTime, long endTime);

	long getAmountByMemberIdAndChaguanIdAndAndRoomNoTime(String chaguanId, String memberId, String roomNo,
			long startTime, long endTime);
}
