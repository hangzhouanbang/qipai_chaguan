package com.anbang.qipai.chaguan.plan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalPanResult;
import com.anbang.qipai.chaguan.plan.dao.GameHistoricalPanResultDao;
import com.highto.framework.web.page.ListPage;

@Service
public class GameHistoricalPanResultService {

	@Autowired
	private GameHistoricalPanResultDao majiangHistoricalPanResultDao;

	public void addGameHistoricalResult(GameHistoricalPanResult result) {
		majiangHistoricalPanResultDao.addGameHistoricalResult(result);
	}

	public ListPage findGameHistoricalResultByGameIdAndGame(int page, int size, String gameId, Game game) {
		long amount = majiangHistoricalPanResultDao.getAmountByGameIdAndGame(gameId, game);
		List<GameHistoricalPanResult> list = majiangHistoricalPanResultDao.findGameHistoricalResultByGameIdAndGame(page,
				size, gameId, game);
		ListPage listPage = new ListPage(list, page, size, (int) amount);
		return listPage;
	}

	public int countFinishPanResultByGameIdAndGame(Game game, String gameId) {
		return (int) majiangHistoricalPanResultDao.getAmountByGameIdAndGame(gameId, game);
	}
}
