package com.anbang.qipai.chaguan.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameLaw;

public interface GameLawRepository extends MongoRepository<GameLaw, String> {

	GameLaw findOneByGameAndName(Game game, String name);

}
