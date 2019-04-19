package com.anbang.qipai.chaguan.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.chaguan.plan.bean.game.GameTable;

public interface GameTableRepository extends MongoRepository<GameTable, String> {

	GameTable findByNoAndFinished(String no, boolean finished);

}
