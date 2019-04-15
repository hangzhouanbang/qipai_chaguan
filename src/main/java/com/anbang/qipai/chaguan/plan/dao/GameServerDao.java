package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameServer;

public interface GameServerDao {

	void save(GameServer gameServer);

	void remove(String[] ids);

	List<GameServer> findAll();

	List<GameServer> findByGame(Game game);

	void updateGameServerState(List<String> ids, int gameServerStateStop);

	List<GameServer> findServersByState(Game game, int state);
}
