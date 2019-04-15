package com.anbang.qipai.chaguan.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.chaguan.plan.bean.game.GameRoom;

public interface GameRoomRepository extends MongoRepository<GameRoom, String> {

	GameRoom findByNoAndFinished(String no, boolean finished);

}
