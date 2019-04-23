package com.anbang.qipai.chaguan.plan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.conf.GameTableStateConfig;
import com.anbang.qipai.chaguan.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameLaw;
import com.anbang.qipai.chaguan.plan.bean.game.GameServer;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.bean.game.IllegalGameLawsException;
import com.anbang.qipai.chaguan.plan.bean.game.LawsMutexGroup;
import com.anbang.qipai.chaguan.plan.bean.game.MemberGameTable;
import com.anbang.qipai.chaguan.plan.bean.game.NoServerAvailableForGameException;
import com.anbang.qipai.chaguan.plan.bean.game.ServerGame;
import com.anbang.qipai.chaguan.plan.dao.GameLawDao;
import com.anbang.qipai.chaguan.plan.dao.GameServerDao;
import com.anbang.qipai.chaguan.plan.dao.GameTableDao;
import com.anbang.qipai.chaguan.plan.dao.LawsMutexGroupDao;
import com.anbang.qipai.chaguan.plan.dao.MemberGameTableDao;
import com.anbang.qipai.chaguan.web.vo.GameTableVO;
import com.anbang.qipai.chaguan.web.vo.PlayerVO;

@Service
public class GameService {
	public static final int GAME_SERVER_STATE_RUNNING = 0;
	public static final int GAME_SERVER_STATE_STOP = 1;

	@Autowired
	private GameLawDao gameLawDao;

	@Autowired
	private LawsMutexGroupDao lawsMutexGroupDao;

	@Autowired
	private GameServerDao gameServerDao;

	@Autowired
	private GameTableDao gameTableDao;

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private MemberGameTableDao memberGameTableDao;

	public GameServer getRandomGameServer(Game game) throws NoServerAvailableForGameException {
		List<GameServer> allServers = gameServerDao.findServersByState(game, GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		return gameServer;
	}

	/**
	 * 创建瑞安麻将房间
	 */
	public GameTable buildRamjGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		List<GameServer> allServers = gameServerDao.findServersByState(Game.ruianMajiang,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.ruianMajiang, name)));
		gameTable.setLaws(laws);
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}

		gameTable.setGame(Game.ruianMajiang);
		if (lawNames.contains("sj")) {
			gameTable.setPanCountPerJu(4);
		} else if (lawNames.contains("bj")) {
			gameTable.setPanCountPerJu(8);
		} else if (lawNames.contains("sej")) {
			gameTable.setPanCountPerJu(12);
		} else if (lawNames.contains("slj")) {
			gameTable.setPanCountPerJu(16);
		} else {
			gameTable.setPanCountPerJu(4);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sanr")) {
			gameTable.setPlayersCount(3);
		} else if (lawNames.contains("sir")) {
			gameTable.setPlayersCount(4);
		} else {
			gameTable.setPlayersCount(4);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	/**
	 * 创建放炮麻将房间
	 */
	public GameTable buildFpmjGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		List<GameServer> allServers = gameServerDao.findServersByState(Game.fangpaoMajiang,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.fangpaoMajiang, name)));
		gameTable.setLaws(laws);
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}

		gameTable.setGame(Game.fangpaoMajiang);
		if (lawNames.contains("sj")) {
			gameTable.setPanCountPerJu(4);
		} else if (lawNames.contains("bj")) {
			gameTable.setPanCountPerJu(8);
		} else if (lawNames.contains("sej")) {
			gameTable.setPanCountPerJu(12);
		} else if (lawNames.contains("slj")) {
			gameTable.setPanCountPerJu(16);
		} else {
			gameTable.setPanCountPerJu(4);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sanr")) {
			gameTable.setPlayersCount(3);
		} else if (lawNames.contains("sir")) {
			gameTable.setPlayersCount(4);
		} else {
			gameTable.setPlayersCount(4);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	/**
	 * 创建温州麻将房间
	 */
	public GameTable buildWzmjGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		List<GameServer> allServers = gameServerDao.findServersByState(Game.wenzhouMajiang,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.wenzhouMajiang, name)));
		gameTable.setLaws(laws);
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}
		gameTable.setGame(Game.wenzhouMajiang);
		if (lawNames.contains("sj")) {
			gameTable.setPanCountPerJu(4);
		} else if (lawNames.contains("bj")) {
			gameTable.setPanCountPerJu(8);
		} else if (lawNames.contains("sej")) {
			gameTable.setPanCountPerJu(12);
		} else if (lawNames.contains("slj")) {
			gameTable.setPanCountPerJu(16);
		} else {
			gameTable.setPanCountPerJu(4);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sanr")) {
			gameTable.setPlayersCount(3);
		} else if (lawNames.contains("sir")) {
			gameTable.setPlayersCount(4);
		} else {
			gameTable.setPlayersCount(4);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	public GameTable buildDpmjGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		// 校验是否有当前游戏服务器
		List<GameServer> allServers = gameServerDao.findServersByState(Game.dianpaoMajiang,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		// 随机选一个服务器
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		// 将gameServer包装成GameServer,加上gameId
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		// 构建list laws
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.dianpaoMajiang, name)));
		gameTable.setLaws(laws);
		// 校验规则是否重复
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}

		gameTable.setGame(Game.dianpaoMajiang);
		if (lawNames.contains("sj")) {
			gameTable.setPanCountPerJu(4);
		} else if (lawNames.contains("bj")) {
			gameTable.setPanCountPerJu(8);
		} else if (lawNames.contains("sej")) {
			gameTable.setPanCountPerJu(12);
		} else if (lawNames.contains("slj")) {
			gameTable.setPanCountPerJu(16);
		} else {
			gameTable.setPanCountPerJu(4);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sanr")) {
			gameTable.setPlayersCount(3);
		} else if (lawNames.contains("sir")) {
			gameTable.setPlayersCount(4);
		} else {
			gameTable.setPlayersCount(4);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	public GameTable buildWzskGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		// 校验是否有当前游戏服务器
		List<GameServer> allServers = gameServerDao.findServersByState(Game.wenzhouShuangkou,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		// 随机选一个服务器
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		// 将serverGame放入gameRoom
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		// 构建list laws
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.wenzhouShuangkou, name)));
		gameTable.setLaws(laws);
		// 校验规则是否重复
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}

		gameTable.setGame(Game.wenzhouShuangkou);
		if (lawNames.contains("sj")) {
			gameTable.setPanCountPerJu(4);
		} else if (lawNames.contains("bj")) {
			gameTable.setPanCountPerJu(8);
		} else if (lawNames.contains("slj")) {
			gameTable.setPanCountPerJu(16);
		} else if (lawNames.contains("ssj")) {
			gameTable.setPanCountPerJu(30);
		} else {
			gameTable.setPanCountPerJu(4);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sir")) {
			gameTable.setPlayersCount(4);
		} else {
			gameTable.setPlayersCount(4);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	public GameTable buildDdzGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		// 校验是否有当前游戏服务器
		List<GameServer> allServers = gameServerDao.findServersByState(Game.doudizhu,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		// 随机选一个服务器
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		// 构建list laws
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.doudizhu, name)));
		gameTable.setLaws(laws);
		// 校验规则是否重复
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}

		gameTable.setGame(Game.doudizhu);
		if (lawNames.contains("sj")) {
			gameTable.setPanCountPerJu(4);
		} else if (lawNames.contains("bj")) {
			gameTable.setPanCountPerJu(8);
		} else if (lawNames.contains("slj")) {
			gameTable.setPanCountPerJu(16);
		} else {
			gameTable.setPanCountPerJu(4);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sanr")) {
			gameTable.setPlayersCount(3);
		} else {
			gameTable.setPlayersCount(3);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	public GameTable buildDblGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		// 校验是否有当前游戏服务器
		List<GameServer> allServers = gameServerDao.findServersByState(Game.daboluo,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		// 随机选一个服务器
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		// 构建list laws
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.daboluo, name)));
		gameTable.setLaws(laws);
		// 校验规则是否重复
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}

		gameTable.setGame(Game.daboluo);
		if (lawNames.contains("shj")) {
			gameTable.setPanCountPerJu(10);
		} else if (lawNames.contains("esj")) {
			gameTable.setPanCountPerJu(20);
		} else if (lawNames.contains("sansj")) {
			gameTable.setPanCountPerJu(30);
		} else if (lawNames.contains("sisj")) {
			gameTable.setPanCountPerJu(40);
		} else {
			gameTable.setPanCountPerJu(10);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sanr")) {
			gameTable.setPlayersCount(3);
		} else if (lawNames.contains("sir")) {
			gameTable.setPlayersCount(4);
		} else {
			gameTable.setPlayersCount(4);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	public GameTable buildPdkGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		// 校验是否有当前游戏服务器
		List<GameServer> allServers = gameServerDao.findServersByState(Game.paodekuai,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		// 随机选一个服务器
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		// 构建list laws
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.paodekuai, name)));
		gameTable.setLaws(laws);
		// 校验规则是否重复
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}

		gameTable.setGame(Game.paodekuai);
		if (lawNames.contains("sj")) {
			gameTable.setPanCountPerJu(4);
		} else if (lawNames.contains("bj")) {
			gameTable.setPanCountPerJu(8);
		} else if (lawNames.contains("slj")) {
			gameTable.setPanCountPerJu(16);
		} else {
			gameTable.setPanCountPerJu(4);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sanr")) {
			gameTable.setPlayersCount(3);
		} else {
			gameTable.setPlayersCount(3);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	/**
	 * 创建茶苑双扣房间
	 */
	public GameTable buildCyskGameTable(String chaguanId, String memberId, List<String> lawNames)
			throws IllegalGameLawsException, NoServerAvailableForGameException {
		List<GameServer> allServers = gameServerDao.findServersByState(Game.chayuanShuangkou,
				GameService.GAME_SERVER_STATE_RUNNING);
		if (allServers == null || allServers.isEmpty()) {
			throw new NoServerAvailableForGameException();
		}
		Random r = new Random();
		GameServer gameServer = allServers.get(r.nextInt(allServers.size()));
		ServerGame serverGame = new ServerGame();
		serverGame.setServer(gameServer);
		GameTable gameTable = new GameTable();
		gameTable.setChaguanId(chaguanId);
		gameTable.setState(GameTableStateConfig.WAITING);
		gameTable.setServerGame(serverGame);

		List<GameLaw> laws = new ArrayList<>();
		lawNames.forEach((name) -> laws.add(gameLawDao.findByGameAndName(Game.chayuanShuangkou, name)));
		gameTable.setLaws(laws);
		if (!gameTable.validateLaws()) {
			throw new IllegalGameLawsException();
		}

		gameTable.setGame(Game.chayuanShuangkou);
		if (lawNames.contains("sj")) {
			gameTable.setPanCountPerJu(4);
		} else if (lawNames.contains("bj")) {
			gameTable.setPanCountPerJu(8);
		} else if (lawNames.contains("slj")) {
			gameTable.setPanCountPerJu(16);
		} else if (lawNames.contains("ssj")) {
			gameTable.setPanCountPerJu(30);
		} else {
			gameTable.setPanCountPerJu(4);
		}

		if (lawNames.contains("er")) {
			gameTable.setPlayersCount(2);
		} else if (lawNames.contains("sir")) {
			gameTable.setPlayersCount(4);
		} else {
			gameTable.setPlayersCount(4);
		}
		gameTable.setCreateTime(System.currentTimeMillis());
		return gameTable;
	}

	public void gamePlayerQuitQame(Game game, String serverGameId, String playerId) {
		memberGameTableDao.remove(game, serverGameId, playerId);
		if (memberGameTableDao.countMemberByGameAndServerGameId(game, serverGameId) == 0) {
			gameTableDao.updateStateGameTable(game, serverGameId, GameTableStateConfig.FINISH);
		}
	}

	public void onlineGameServer(GameServer gameServer) {
		gameServerDao.save(gameServer);
	}

	public void offlineGameServer(String[] ids) {
		gameServerDao.remove(ids);
	}

	public List<GameServer> findAllServersForGame(Game game) {
		return gameServerDao.findAllByGame(game);
	}

	public void createGameLaw(GameLaw law) {
		gameLawDao.save(law);
	}

	public void updateGameLaw(GameLaw law) {
		gameLawDao.update(law);
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

	public void startGameServers(List<String> ids) {
		if (ids != null && ids.size() > 0) {
			this.gameServerDao.updateGameServerState(ids, GameService.GAME_SERVER_STATE_RUNNING);
		}
	}

	public void stopGameServers(List<String> ids) {
		if (ids != null && ids.size() > 0) {
			this.gameServerDao.updateGameServerState(ids, GameService.GAME_SERVER_STATE_STOP);
		}
	}

	public void saveGameTable(GameTable gameTable) {
		gameTableDao.save(gameTable);
	}

	public void createGameTable(GameTable gameTable, String createMemberId) {
		gameTableDao.save(gameTable);
		MemberDbo member = memberDboDao.findById(createMemberId);
		MemberGameTable mgr = new MemberGameTable();
		mgr.setGameTable(gameTable);
		mgr.setMemberId(createMemberId);
		mgr.setNickname(member.getNickname());
		mgr.setHeadimgurl(member.getHeadimgurl());
		memberGameTableDao.save(mgr);
	}

	public GameTable findTableOpen(String roomNo) {
		return gameTableDao.findTableOpen(roomNo);
	}

	public void joinGameTable(GameTable gameTable, String memberId) {
		MemberDbo member = memberDboDao.findById(memberId);
		MemberGameTable mgr = new MemberGameTable();
		mgr.setGameTable(gameTable);
		mgr.setMemberId(memberId);
		mgr.setNickname(member.getNickname());
		mgr.setHeadimgurl(member.getHeadimgurl());
		memberGameTableDao.save(mgr);
	}

	public MemberGameTable findMemberGameTable(String memberId, String gameTableId) {
		return memberGameTableDao.findByMemberIdAndGameTableId(memberId, gameTableId);
	}

	public void gameTableFinished(Game game, String serverGameId) {
		gameTableDao.updateStateGameTable(game, serverGameId, GameTableStateConfig.FINISH);
		memberGameTableDao.removeExpireRoom(game, serverGameId);
	}

	public List<GameTableVO> findGameTableByChaguanId(String chaguanId, int page, int size) {
		List<GameTableVO> tableList = new ArrayList<>();
		List<GameTable> gameTableList = gameTableDao.findGameTableByChaguanId(chaguanId, page, size);
		for (GameTable gameTable : gameTableList) {
			GameTableVO table = new GameTableVO();
			table.setChaguanId(gameTable.getChaguanId());
			table.setGame(gameTable.getGame());
			table.setLaws(gameTable.getLaws());
			table.setNo(gameTable.getNo());
			table.setPlayersCount(gameTable.getPlayersCount());
			table.setState(gameTable.getState());
			List<PlayerVO> playerList = new ArrayList<>();
			table.setPlayerList(playerList);
			List<MemberGameTable> memberGameTableList = memberGameTableDao.findMemberGameTableByGameAndServerGameId(
					gameTable.getGame(), gameTable.getServerGame().getGameId());
			for (MemberGameTable memberGameTable : memberGameTableList) {
				PlayerVO player = new PlayerVO();
				player.setMemberId(memberGameTable.getMemberId());
				player.setNickname(memberGameTable.getNickname());
				player.setHeadimgurl(memberGameTable.getHeadimgurl());
				playerList.add(player);
			}
			tableList.add(table);
		}
		return tableList;
	}

	public void gameTablePlaying(Game game, String serverGameId) {
		gameTableDao.updateStateGameTable(game, serverGameId, GameTableStateConfig.PLAYING);
	}

	public GameTable findTableByGameAndServerGameGameId(Game game, String serverGameId) {
		return gameTableDao.findTableByGameAndServerGameGameId(game, serverGameId);
	}
}
