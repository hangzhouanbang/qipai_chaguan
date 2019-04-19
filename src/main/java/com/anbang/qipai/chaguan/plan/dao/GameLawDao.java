package com.anbang.qipai.chaguan.plan.dao;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameLaw;

public interface GameLawDao {

	void save(GameLaw law);

	void update(GameLaw law);

	void remove(String id);

	GameLaw findByGameAndName(Game game, String name);

}
