package com.anbang.qipai.chaguan.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.chaguan.plan.bean.game.GameServer;

public interface GameServerRepository extends MongoRepository<GameServer, String> {

}
