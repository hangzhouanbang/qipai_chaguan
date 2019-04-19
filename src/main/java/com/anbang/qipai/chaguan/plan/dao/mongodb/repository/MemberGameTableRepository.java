package com.anbang.qipai.chaguan.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.MemberGameTable;

public interface MemberGameTableRepository extends MongoRepository<MemberGameTable, String> {

	void deleteByMemberIdAndGameTableGameAndGameTableServerGameGameId(String memberId, Game game, String serverGameId);

	void deleteByGameTableGameAndGameTableServerGameGameId(Game game, String serverGameId);
}
