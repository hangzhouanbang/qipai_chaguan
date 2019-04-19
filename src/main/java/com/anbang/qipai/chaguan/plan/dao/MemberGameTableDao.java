package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.MemberGameTable;

public interface MemberGameTableDao {

	void save(MemberGameTable memberGameTable);

	long count(String memberId);

	MemberGameTable findByMemberIdAndGameTableId(String memberId, String gameTableId);

	long countMemberByGameAndServerGameId(Game game, String serverGameId);

	List<MemberGameTable> findMemberGameTableByGameAndServerGameId(Game game, String serverGameId);

	void remove(Game game, String serverGameId, String memberId);

	void removeExpireRoom(Game game, String serverGameId);

	List<MemberGameTable> findMemberGameTableByMemberId(String memberId);
}
