package com.anbang.qipai.chaguan.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.MemberGameRoom;

public interface MemberGameRoomRepository extends MongoRepository<MemberGameRoom, String> {

	void deleteByMemberIdAndGameRoomGameAndGameRoomServerGameGameId(String memberId, Game game, String serverGameId);

	void deleteByGameRoomGameAndGameRoomServerGameGameId(Game game, String serverGameId);
}
