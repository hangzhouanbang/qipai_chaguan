package com.anbang.qipai.chaguan.plan.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.msg.GameServerMsgConstant;
import com.anbang.qipai.chaguan.msg.channel.GameServerSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameLaw;
import com.anbang.qipai.chaguan.plan.bean.game.GameRoom;
import com.anbang.qipai.chaguan.plan.bean.game.GameServer;
import com.anbang.qipai.chaguan.plan.bean.game.LawsMutexGroup;
import com.anbang.qipai.chaguan.plan.bean.game.MemberGameRoom;
import com.anbang.qipai.chaguan.plan.bean.game.NoServerAvailableForGameException;
import com.anbang.qipai.chaguan.plan.dao.GameLawDao;
import com.anbang.qipai.chaguan.plan.dao.GameRoomDao;
import com.anbang.qipai.chaguan.plan.dao.GameServerDao;
import com.anbang.qipai.chaguan.plan.dao.LawsMutexGroupDao;
import com.anbang.qipai.chaguan.plan.dao.MemberGameRoomDao;

@Service
@EnableBinding(GameServerSource.class)
public class GameService {

	public static final int GAME_SERVER_STATE_RUNNINT = 0;
	public static final int GAME_SERVER_STATE_STOP = 1;

	@Autowired
	private GameLawDao gameLawDao;

	@Autowired
	private GameServerDao gameServerDao;

	@Autowired
	private GameRoomDao gameRoomDao;

	@Autowired
	private MemberGameRoomDao memberGameRoomDao;

	@Autowired
	private LawsMutexGroupDao lawsMutexGroupDao;

	@Autowired
	private GameServerSource gameServerSource;

	public GameLaw findGameLaw(Game game, String lawName) {
		return gameLawDao.findByGameAndName(game, lawName);
	}

	public void saveGameRoom(GameRoom gameRoom) {
		gameRoomDao.save(gameRoom);
	}

	public GameServer getRandomGameServer(Game game) throws NoServerAvailableForGameException {
		List<GameServer> allServers = gameServerDao.findServersByState(game, GameService.GAME_SERVER_STATE_RUNNINT);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		return gameServer;
	}

	public void onlineServer(GameServer gameServer) {
		gameServer.setOnlineTime(System.currentTimeMillis());
		gameServerDao.save(gameServer);
	}

	public void offlineServer(String[] gameServerIds) {
		gameServerDao.remove(gameServerIds);
	}

	public void createGameRoom(GameRoom gameRoom) {
		gameRoomDao.save(gameRoom);
		String createMemberId = gameRoom.getCreateMemberId();
		MemberGameRoom mgr = new MemberGameRoom();
		mgr.setGameRoom(gameRoom);
		mgr.setMemberId(createMemberId);
		memberGameRoomDao.save(mgr);
	}

	public void joinGameRoom(GameRoom gameRoom, String memberId) {
		MemberGameRoom mgr = new MemberGameRoom();
		mgr.setGameRoom(gameRoom);
		mgr.setMemberId(memberId);
		memberGameRoomDao.save(mgr);
	}

	public void createGameLaw(GameLaw law) {
		gameLawDao.save(law);
	}

	public void removeGameLaw(String lawId) {
		gameLawDao.remove(lawId);
	}

	public void addLawsMutexGroup(LawsMutexGroup lawsMutexGroup) {
		lawsMutexGroupDao.save(lawsMutexGroup);
	}

	public void removeLawsMutexGroup(String groupId) {
		lawsMutexGroupDao.remove(groupId);
	}

	public GameRoom findRoomOpen(String roomNo) {
		return gameRoomDao.findRoomOpen(roomNo);
	}

	public void expireMemberGameRoom(Game game, String serverGameId) {
		memberGameRoomDao.removeExpireRoom(game, serverGameId);
	}

	public List<MemberGameRoom> queryMemberGameRoomForMember(String memberId) {
		return memberGameRoomDao.findMemberGameRoomByMemberId(memberId);
	}

	public List<GameRoom> findExpireGameRoom(long deadlineTime) {
		return gameRoomDao.findExpireGameRoom(deadlineTime, false);
	}

	public void expireGameRoom(List<String> ids) {
		gameRoomDao.updateGameRoomFinished(ids, true);
	}

	public void gameRoomFinished(Game game, String serverGameId) {
		gameRoomDao.updateFinishGameRoom(game, serverGameId, true);
		memberGameRoomDao.removeExpireRoom(game, serverGameId);
	}

	public GameRoom findRoomByGameAndServerGameGameId(Game game, String serverGameId) {
		return gameRoomDao.findRoomByGameAndServerGameGameId(game, serverGameId);
	}

	public void stopGameServer(List<String> ids) {
		try {
			this.gameServerDao.updateGameServerState(ids, GameService.GAME_SERVER_STATE_STOP);
		} catch (Exception e) {
			CommonMO commonMO = new CommonMO();
			commonMO.setMsg(GameServerMsgConstant.STOP_GAME_SERVERS_FAILED);
			commonMO.setData(ids);
			this.gameServerSource.gameServer().send(MessageBuilder.withPayload(commonMO).build());
		}
	}

	public void recoverGameServer(List<String> ids) {
		try {
			this.gameServerDao.updateGameServerState(ids, GAME_SERVER_STATE_RUNNINT);
		} catch (Throwable e) {
			CommonMO commonMO = new CommonMO();
			commonMO.setMsg(GameServerMsgConstant.RECOVER_GAME_SERVERS_FAILED);
			commonMO.setData(ids);
			this.gameServerSource.gameServer().send(MessageBuilder.withPayload(commonMO).build());
		}
	}
}
