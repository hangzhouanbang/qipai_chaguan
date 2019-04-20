package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;

public interface GameTableDao {

	void save(GameTable gameTable);

	GameTable findTableOpen(String roomNo);

	GameTable findTableByGameAndServerGameGameId(Game game, String serverGameId);

	List<GameTable> findExpireGameTable(long deadlineTime, String state);

	List<GameTable> findGameTableByChaguanId(String chaguanId, int page, int size);

	void updateGameTableState(List<String> ids, String state);

	void updateStateGameTable(Game game, String serverGameId, String state);

}
