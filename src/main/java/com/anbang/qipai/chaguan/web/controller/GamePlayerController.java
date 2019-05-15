package com.anbang.qipai.chaguan.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.cqrs.c.service.GameTableCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanMemberDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanMemberDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberChaguanYushiService;
import com.anbang.qipai.chaguan.msg.service.ChayuanShuangkouGameRoomMsgService;
import com.anbang.qipai.chaguan.msg.service.DaboluoGameRoomMsgService;
import com.anbang.qipai.chaguan.msg.service.DianpaoGameRoomMsgService;
import com.anbang.qipai.chaguan.msg.service.DoudizhuGameRoomMsgService;
import com.anbang.qipai.chaguan.msg.service.FangpaoGameRoomMsgService;
import com.anbang.qipai.chaguan.msg.service.GameTableMsgService;
import com.anbang.qipai.chaguan.msg.service.PaodekuaiGameRoomMsgService;
import com.anbang.qipai.chaguan.msg.service.RuianGameRoomMsgService;
import com.anbang.qipai.chaguan.msg.service.WenzhouGameRoomMsgService;
import com.anbang.qipai.chaguan.msg.service.WenzhouShuangkouGameRoomMsgService;
import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberPayType;
import com.anbang.qipai.chaguan.plan.bean.MemberLoginLimitRecord;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameLaw;
import com.anbang.qipai.chaguan.plan.bean.game.GameServer;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.bean.game.IllegalGameLawsException;
import com.anbang.qipai.chaguan.plan.bean.game.MemberGameTable;
import com.anbang.qipai.chaguan.plan.bean.game.NoServerAvailableForGameException;
import com.anbang.qipai.chaguan.plan.service.GameService;
import com.anbang.qipai.chaguan.plan.service.MemberAuthService;
import com.anbang.qipai.chaguan.plan.service.MemberLoginLimitRecordService;
import com.anbang.qipai.chaguan.web.fb.CyskLawsFB;
import com.anbang.qipai.chaguan.web.fb.DblLawsFB;
import com.anbang.qipai.chaguan.web.fb.DdzLawsFB;
import com.anbang.qipai.chaguan.web.fb.DpmjLawsFB;
import com.anbang.qipai.chaguan.web.fb.FpmjLawsFB;
import com.anbang.qipai.chaguan.web.fb.PdkLawsFB;
import com.anbang.qipai.chaguan.web.fb.RamjLawsFB;
import com.anbang.qipai.chaguan.web.fb.WzmjLawsFB;
import com.anbang.qipai.chaguan.web.fb.WzskLawsFB;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.anbang.qipai.chaguan.web.vo.CommonVoUtil;
import com.anbang.qipai.chaguan.web.vo.GameTableVO;
import com.google.gson.Gson;

/**
 * 游戏管理
 * 
 * @author lsc
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/game")
public class GamePlayerController {

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private GameService gameService;

	@Autowired
	private MemberChaguanYushiService memberChaguanYushiService;

	@Autowired
	private ChaguanMemberDboService chaguanMemberDboService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

	@Autowired
	private GameTableCmdService gameTableCmdService;

	@Autowired
	private GameTableMsgService gameTableMsgService;

	@Autowired
	private MemberLoginLimitRecordService memberLoginLimitRecordService;

	@Autowired
	private RuianGameRoomMsgService ruianGameRoomMsgService;

	@Autowired
	private FangpaoGameRoomMsgService fangpaoGameRoomMsgService;

	@Autowired
	private WenzhouGameRoomMsgService wenzhouGameRoomMsgService;

	@Autowired
	private DianpaoGameRoomMsgService dianpaoGameRoomMsgService;

	@Autowired
	private WenzhouShuangkouGameRoomMsgService wenzhouShuangkouGameRoomMsgService;

	@Autowired
	private ChayuanShuangkouGameRoomMsgService chayuanShuangkouGameRoomMsgService;

	@Autowired
	private DoudizhuGameRoomMsgService doudizhuGameRoomMsgService;

	@Autowired
	private PaodekuaiGameRoomMsgService paodekuaiGameRoomMsgService;

	@Autowired
	private DaboluoGameRoomMsgService daboluoGameRoomMsgService;

	@Autowired
	private HttpClient httpClient;

	private Gson gson = new Gson();

	/**
	 * 创建瑞安麻将房间
	 */
	@RequestMapping(value = "/create_ramj_table")
	@ResponseBody
	public CommonVO createRamjTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildRamjGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			vo.setSuccess(false);
			vo.setMsg("IllegalGameLawsException");
			return vo;
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		RamjLawsFB fb = new RamjLawsFB(lawNames);
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			int gold = fb.payForCreateRoom();
			if (account == null || account.getBalance() < gold) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			int gold = fb.payForCreateRoom();
			if (account == null || account.getBalance() < gold) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("difen", fb.getDifen());
		req.param("taishu", fb.getTaishu());
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());
		req.param("dapao", fb.getDapao());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}
		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;

	}

	/**
	 * 创建放炮麻将房间
	 */
	@RequestMapping(value = "/create_fpmj_table")
	@ResponseBody
	public CommonVO createFpmjTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildFpmjGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			vo.setSuccess(false);
			vo.setMsg("IllegalGameLawsException");
			return vo;
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		FpmjLawsFB fb = new FpmjLawsFB(lawNames);
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());
		req.param("hongzhongcaishen", fb.getHognzhongcaishen());
		req.param("dapao", fb.getDapao());
		req.param("sipaofanbei", fb.getSipaofanbei());
		req.param("zhuaniao", fb.getZhuaniao());
		req.param("niaoshu", fb.getNiaoshu());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}

		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;

	}

	/**
	 * 创建温州麻将房间
	 */
	@RequestMapping(value = "/create_wzmj_table")
	@ResponseBody
	public CommonVO createWzmjTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildWzmjGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			vo.setSuccess(false);
			vo.setMsg("IllegalGameLawsException");
			return vo;
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		WzmjLawsFB fb = new WzmjLawsFB(lawNames);
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());
		req.param("jinjie1", fb.getJinjie1());
		req.param("jinjie2", fb.getJinjie2());
		req.param("gangsuanfen", fb.getGangsuanfen());
		req.param("teshushuangfan", fb.getTeshushuangfan());
		req.param("caishenqian", fb.getCaishenqian());
		req.param("shaozhongfa", fb.getShaozhongfa());
		req.param("lazila", fb.getLazila());
		req.param("queyise", fb.getQueyise());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}

		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;

	}

	/**
	 * 创建点炮麻将房间
	 */
	@RequestMapping(value = "/create_dpmj_table")
	@ResponseBody
	public CommonVO createDpmjTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildDpmjGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			vo.setSuccess(false);
			vo.setMsg("IllegalGameLawsException");
			return vo;
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		DpmjLawsFB fb = new DpmjLawsFB(lawNames);
		// 远程调用游戏服务器的newgame
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());
		req.param("dianpao", fb.getDianpao());
		req.param("dapao", fb.getDapao());
		req.param("quzhongfabai", fb.getQuzhongfabai());
		req.param("zhuaniao", fb.getZhuaniao());
		req.param("niaoshu", fb.getNiaoshu());
		req.param("qingyise", fb.getQingyise());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			// 游戏服务器回传的参数带有gameId和token
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}

		// 创建游戏房间的编号
		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		// 将带roomNo的gameRoom写入数据库
		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		// 存入了游戏服务器传回的token
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;

	}

	/**
	 * 创建温州双扣房间
	 */
	@RequestMapping(value = "/create_wzsk_table")
	@ResponseBody
	public CommonVO createWzskTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildWzskGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			vo.setSuccess(false);
			vo.setMsg("IllegalGameLawsException");
			return vo;
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		WzskLawsFB fb = new WzskLawsFB(lawNames);
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			int gold = fb.payForCreateRoom();
			if (account == null || account.getBalance() < gold) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			int gold = fb.payForCreateRoom();
			if (account == null || account.getBalance() < gold) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		// 远程调用游戏服务器的newgame
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());
		req.param("bx", fb.getBx());
		req.param("chapai", fb.getChapai());
		req.param("fapai", fb.getFapai());
		req.param("chaodi", fb.getChaodi());
		req.param("shuangming", fb.getShuangming());
		req.param("bxfd", fb.getBxfd());
		req.param("jxfd", fb.getJxfd());
		req.param("sxfd", fb.getSxfd());
		req.param("gxjb", fb.getGxjb());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			// 游戏服务器回传的参数带有gameId和token
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}

		// 创建游戏房间的编号
		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		// 存入了游戏服务器传回的token
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;
	}

	/**
	 * 创建斗地主房间
	 */
	@RequestMapping(value = "/create_ddz_table")
	@ResponseBody
	public CommonVO createDdzTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildDdzGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			vo.setSuccess(false);
			vo.setMsg("IllegalGameLawsException");
			return vo;
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		DdzLawsFB fb = new DdzLawsFB(lawNames);
		// 远程调用游戏服务器的newgame
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());
		req.param("difen", fb.getDifen());
		req.param("qxp", fb.getQxp());
		req.param("szfbxp", fb.getSzfbxp());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			// 游戏服务器回传的参数带有gameId和token
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}

		// 创建游戏房间的编号
		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		// 将带roomNo的gameRoom写入数据库
		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		// 存入了游戏服务器传回的token
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;
	}

	/**
	 * 创建大菠萝房间
	 */
	@RequestMapping(value = "/create_dbl_table")
	@ResponseBody
	public CommonVO createDblTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildDblGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			vo.setSuccess(false);
			vo.setMsg("IllegalGameLawsException");
			return vo;
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		DblLawsFB fb = new DblLawsFB(lawNames);
		int gold = fb.payForCreateRoom();
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			if (account == null || account.getBalance() < gold) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			if (account == null || account.getBalance() < gold) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		// 远程调用游戏服务器的newgame
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());
		req.param("dqef", fb.getDqef());
		req.param("dqsf", fb.getDqsf());
		req.param("bx", fb.getBx());
		req.param("bihuase", fb.getBihuase());
		req.param("zidongzupai", fb.getZidongzupai());
		req.param("yitiaolong", fb.getYitiaolong());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			// 游戏服务器回传的参数带有gameId和token
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}

		// 创建游戏房间的编号
		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		// 将带roomNo的gameRoom写入数据库
		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		// 存入了游戏服务器传回的token
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;
	}

	/**
	 * 创建跑得快房间
	 */
	@RequestMapping(value = "/create_pdk_table")
	@ResponseBody
	public CommonVO createPdkTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			return CommonVoUtil.error("invalid token");
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			return CommonVoUtil.error("login limit");
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildPdkGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			return CommonVoUtil.error(e.getClass().getName());
		} catch (NoServerAvailableForGameException e) {
			return CommonVoUtil.error(e.getClass().getName());
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		PdkLawsFB fb = new PdkLawsFB(lawNames);
		int gold = fb.payForCreateRoom();
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			if (account == null || account.getBalance() < gold) {
				return CommonVoUtil.error("InsufficientBalanceException");
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			if (account == null || account.getBalance() < gold) {
				return CommonVoUtil.error("InsufficientBalanceException");
			}
		}
		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		// 远程调用游戏服务器的newgame
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());

		req.param("bichu", fb.getBichu());
		req.param("biya", fb.getBiya());
		req.param("aBoom", fb.getaBoom());
		req.param("sandaique", fb.getSandaique());
		req.param("feijique", fb.getFeijique());
		req.param("showShoupaiNum", fb.getShowShoupaiNum());
		req.param("zhuaniao", fb.getZhuaniao());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			// 游戏服务器回传的参数带有gameId和token
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			return CommonVoUtil.error("SysException");
		}

		// 创建游戏房间的编号
		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		// 将带roomNo的gameRoom写入数据库
		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		// 存入了游戏服务器传回的token
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		return CommonVoUtil.success(data, "creat paodekuai success");
	}

	/**
	 * 创建茶苑双扣房间
	 */
	@RequestMapping(value = "/create_cysk_table")
	@ResponseBody
	public CommonVO createCyskTable(String chaguanId, String token, @RequestBody List<String> lawNames) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap();
		GameTable gameTable;
		try {
			gameTable = gameService.buildCyskGameTable(chaguanId, memberId, lawNames);
		} catch (IllegalGameLawsException e) {
			vo.setSuccess(false);
			vo.setMsg("IllegalGameLawsException");
			return vo;
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId,
				false);
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		if (dbo.getPayType().equals(ChaguanMemberPayType.FREE)) {
			ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(dbo.getAgentId());
			if (account == null || account.getBalance() < 100) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		CyskLawsFB fb = new CyskLawsFB(lawNames);

		GameServer gameServer = gameTable.getServerGame().getServer();
		// 游戏服务器rpc，需要手动httpclientrpc
		// 远程调用游戏服务器的newgame
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/newgame_player_quit");
		req.param("playerId", memberId);
		req.param("panshu", fb.getPanshu());
		req.param("renshu", fb.getRenshu());
		req.param("bx", fb.getBx());
		req.param("fapai", fb.getFapai());
		req.param("shuangming", fb.getShuangming());
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			// 游戏服务器回传的参数带有gameId和token
			resData = (Map) resVo.getData();
			gameTable.getServerGame().setGameId((String) resData.get("gameId"));
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}

		// 创建游戏房间的编号
		String roomNo = gameTableCmdService.createTable(memberId, System.currentTimeMillis());
		gameTable.setNo(roomNo);

		// 将带roomNo的gameRoom写入数据库
		gameService.createGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("gameId", gameTable.getServerGame().getGameId());
		// 存入了游戏服务器传回的token
		data.put("token", resData.get("token"));
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;
	}

	/**
	 * 加入房间
	 */
	@RequestMapping(value = "/join_table")
	@ResponseBody
	public CommonVO joinTable(String token, String roomNo) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		GameTable gameTable = gameService.findTableOpen(roomNo);
		if (gameTable == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid room number");
			return vo;
		}
		String serverGameId = gameTable.getServerGame().getGameId();
		// 处理如果是自己暂时离开的房间
		MemberGameTable memberGameTable = gameService.findMemberGameTable(memberId, gameTable.getId());
		if (memberGameTable != null) {
			// 游戏服务器rpc返回房间
			GameServer gameServer = gameTable.getServerGame().getServer();
			Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/backtogame");
			req.param("playerId", memberId);
			req.param("gameId", serverGameId);
			Map resData;
			try {
				ContentResponse res = req.send();
				String resJson = new String(res.getContent());
				CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
				if (resVo.isSuccess()) {
					resData = (Map) resVo.getData();
				} else {
					vo.setSuccess(false);
					vo.setMsg(resVo.getMsg());
					return vo;
				}
			} catch (Exception e) {
				vo.setSuccess(false);
				vo.setMsg("SysException");
				return vo;
			}

			Map data = new HashMap();
			data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
			data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
			data.put("roomNo", gameTable.getNo());
			data.put("token", resData.get("token"));
			data.put("gameId", serverGameId);
			data.put("game", gameTable.getGame());
			vo.setData(data);
			return vo;
		}
		ChaguanMemberDbo dbo = chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId,
				gameTable.getChaguanId(), false);
		if (dbo.getPayType() == null || dbo.getPayType().equals(ChaguanMemberPayType.SELF)) {
			int gold = 100;
			List<String> lawNames = new ArrayList<>();
			List<GameLaw> laws = gameTable.getLaws();
			// 构建list laws
			laws.forEach((law) -> lawNames.add(law.getName()));
			if (gameTable.getGame().equals(Game.wenzhouShuangkou)) {
				gold = new WzskLawsFB(lawNames).payForJoinRoom();
			} else if (gameTable.getGame().equals(Game.ruianMajiang)) {
				gold = new RamjLawsFB(lawNames).payForJoinRoom();
			} else if (gameTable.getGame().equals(Game.paodekuai)) {
				gold = new PdkLawsFB(lawNames).payForJoinRoom();
			} else if (gameTable.getGame().equals(Game.daboluo)) {
				gold = new DblLawsFB(lawNames).payForJoinRoom();
			}
			// 判断普通会员个人账户的余额能否支付加入房间的费用
			MemberChaguanYushiAccountDbo account = memberChaguanYushiService
					.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(gameTable.getChaguanId(), memberId);
			if (account == null || account.getBalance() < gold) {
				vo.setSuccess(false);
				vo.setMsg("InsufficientBalanceException");
				return vo;
			}
		}
		// 游戏服务器rpc加入房间
		GameServer gameServer = gameTable.getServerGame().getServer();
		Request req = httpClient.newRequest(gameServer.getHttpUrl() + "/game/joingame");
		req.param("playerId", memberId);
		req.param("gameId", serverGameId);
		Map resData;
		try {
			ContentResponse res = req.send();
			String resJson = new String(res.getContent());
			CommonVO resVo = gson.fromJson(resJson, CommonVO.class);
			if (resVo.isSuccess()) {
				resData = (Map) resVo.getData();
			} else {
				vo.setSuccess(false);
				vo.setMsg(resVo.getMsg());
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg("SysException");
			return vo;
		}
		gameService.joinGameTable(gameTable, memberId);
		gameTableMsgService.recordGameTable(gameTable);

		Map data = new HashMap();
		data.put("httpUrl", gameTable.getServerGame().getServer().getHttpUrl());
		data.put("wsUrl", gameTable.getServerGame().getServer().getWsUrl());
		data.put("roomNo", gameTable.getNo());
		data.put("token", resData.get("token"));
		data.put("gameId", serverGameId);
		data.put("game", gameTable.getGame());
		vo.setData(data);
		return vo;
	}

	/**
	 * 查询桌子
	 */
	@RequestMapping(value = "/query_table")
	@ResponseBody
	public CommonVO queryTable(String token, String chaguanId, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberLoginLimitRecord limit = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (limit != null) {
			vo.setSuccess(false);
			vo.setMsg("login limit");
			return vo;
		}
		Map data = new HashMap<>();
		vo.setData(data);
		List<GameTableVO> tableList = gameService.findGameTableByChaguanId(chaguanId, page, size);
		data.put("tableList", tableList);
		return vo;
	}

	/**
	 * 房间到时定时器，每小时
	 */
	@Scheduled(cron = "0 0 0/1 * * ?")
	public void removeGameRoom() {
		long deadlineTime = System.currentTimeMillis();
		List<GameTable> tableList = gameService.findExpireGameTable(deadlineTime);
		Map<Game, List<String>> gameIdMap = new HashMap<>();
		for (Game game : Game.values()) {
			gameIdMap.put(game, new ArrayList<>());
		}
		for (GameTable table : tableList) {
			Game game = table.getGame();
			String serverGameId = table.getServerGame().getGameId();
			if (!StringUtils.isBlank(serverGameId)) {
				gameIdMap.get(game).add(serverGameId);
			}
		}
		ruianGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.ruianMajiang));
		fangpaoGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.fangpaoMajiang));
		wenzhouGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.wenzhouMajiang));
		dianpaoGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.dianpaoMajiang));
		wenzhouShuangkouGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.wenzhouShuangkou));
		doudizhuGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.doudizhu));
		paodekuaiGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.paodekuai));
		daboluoGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.daboluo));
		chayuanShuangkouGameRoomMsgService.removeGameRoom(gameIdMap.get(Game.chayuanShuangkou));
	}
}
