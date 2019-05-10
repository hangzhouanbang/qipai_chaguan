package com.anbang.qipai.chaguan.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.cqrs.c.service.PlayBackCodeCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.PlayBackDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.PlayBackDboService;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameServer;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.bean.game.NoServerAvailableForGameException;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalJuResult;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalJuResultService;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalPanResultService;
import com.anbang.qipai.chaguan.plan.service.GameService;
import com.anbang.qipai.chaguan.plan.service.MemberAuthService;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.highto.framework.web.page.ListPage;

@CrossOrigin
@RestController
@RequestMapping("/result")
public class MemberHistoricalResultController {

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private GameHistoricalJuResultService majiangHistoricalResultService;

	@Autowired
	private GameHistoricalPanResultService majiangHistoricalPanResultService;

	@Autowired
	private GameService gameService;

	@Autowired
	private PlayBackCodeCmdService playBackCodeCmdService;

	@Autowired
	private PlayBackDboService playBackDboService;

	/**
	 * 战绩查询未完成
	 */
	@RequestMapping(value = "/query_member_dayresult")
	public CommonVO queryMemberDayResult(@RequestParam(name = "startTime", required = true) long startTime,
			@RequestParam(name = "endTime", required = true) long endTime,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, String chaguanId, String memberId) {
		CommonVO vo = new CommonVO();
		ListPage listPage = majiangHistoricalResultService.findMemberDayResultByTimeAndChaguanId(startTime, endTime,
				page, size, chaguanId, memberId);
		vo.setSuccess(true);
		vo.setMsg("historical result");
		Map data = new HashMap();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 战绩完成
	 */
	@RequestMapping(value = "/finish_member_dayresult")
	public CommonVO finishMemberDayResult(String resultId) {
		CommonVO vo = new CommonVO();
		majiangHistoricalResultService.finishMemberDayResultById(resultId);
		vo.setSuccess(true);
		return vo;
	}

	/**
	 * 战绩详情未完成
	 */
	@RequestMapping(value = "/query_member_historicalresult")
	public CommonVO queryMemberHistoricalResult(@RequestParam(name = "startTime", required = true) long startTime,
			@RequestParam(name = "endTime", required = true) long endTime,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, String chaguanId, String memberId) {
		CommonVO vo = new CommonVO();
		ListPage listPage = majiangHistoricalResultService
				.findGameHistoricalResultByMemberIdAndChaguanIdForMemberDayResult(startTime, endTime, page, size,
						memberId, chaguanId);
		vo.setSuccess(true);
		vo.setMsg("historical result");
		Map data = new HashMap();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 战绩详情已完成
	 */
	@RequestMapping(value = "/query_member_historicalresult_finish")
	public CommonVO queryMemberHistoricalResultFinish(@RequestParam(name = "startTime", required = true) long startTime,
			@RequestParam(name = "endTime", required = true) long endTime,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, String chaguanId, String memberId) {
		CommonVO vo = new CommonVO();
		ListPage listPage = majiangHistoricalResultService
				.findGameHistoricalResultByMemberIdAndChaguanIdForMemberDayHistoricalResult(startTime, endTime, page,
						size, memberId, chaguanId);
		vo.setSuccess(true);
		vo.setMsg("historical result");
		Map data = new HashMap();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 战绩查询已完成
	 */
	@RequestMapping(value = "/query_member_historicaldayresult")
	public CommonVO queryMemberHistoricalDayResult(@RequestParam(name = "startTime", required = true) long startTime,
			@RequestParam(name = "endTime", required = true) long endTime,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, String chaguanId, String memberId) {
		CommonVO vo = new CommonVO();
		ListPage listPage = majiangHistoricalResultService.findMemberDayHistoricalResultByTimeAndChaguanId(startTime,
				endTime, page, size, chaguanId, memberId);
		vo.setSuccess(true);
		vo.setMsg("historical result");
		Map data = new HashMap();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	@RequestMapping(value = "/query_historicalresult")
	public CommonVO queryHistoricalResult(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "20") Integer size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = majiangHistoricalResultService.findGameHistoricalResultByMemberId(page, size, memberId);
		vo.setSuccess(true);
		vo.setMsg("historical result");
		vo.setData(listPage);
		return vo;
	}

	@RequestMapping(value = "/query_historicalresult_detail")
	public CommonVO queryHistoricalResultDetail(String id, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		GameHistoricalJuResult majiangHistoricalResult = majiangHistoricalResultService
				.findGameHistoricalResultById(id);
		vo.setSuccess(true);
		vo.setMsg("historical result detail");
		vo.setData(majiangHistoricalResult);
		return vo;
	}

	@RequestMapping(value = "/query_historicalpanresult")
	public CommonVO queryHistoricalPanResult(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "20") Integer size, String token, Game game, String gameId) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = majiangHistoricalPanResultService.findGameHistoricalResultByMemberId(page, size, gameId,
				game);
		vo.setSuccess(true);
		vo.setMsg("historical result");
		vo.setData(listPage);
		return vo;
	}

	@RequestMapping(value = "/playback_self")
	public CommonVO playback(String token, Game game, String gameId, int panNo) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		GameServer gameServer;
		try {
			gameServer = gameService.getRandomGameServer(game);
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		GameTable table = gameService.findTableByGameAndServerGameGameId(game, gameId);
		Map data = new HashMap();
		data.put("httpUrl", gameServer.getHttpUrl());
		data.put("gameId", gameId);
		data.put("panNo", panNo);
		data.put("roomNo", table.getNo());
		data.put("game", table.getGame());
		vo.setSuccess(true);
		vo.setMsg("playback");
		vo.setData(data);
		return vo;
	}

	@RequestMapping(value = "/shareplayback")
	public CommonVO sharePlayback(String token, Game game, String gameId, int panNo) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PlayBackDbo playBackDbo = playBackDboService.findByGameAndGameIdAndPanNo(game, gameId, panNo);
		if (playBackDbo != null) {
			Map data = new HashMap();
			data.put("code", playBackDbo.getId());
			vo.setSuccess(true);
			vo.setMsg("playbackcode");
			vo.setData(data);
			return vo;
		}
		Integer code = playBackCodeCmdService.getPlayBackCode();
		int size = code.toString().length();
		String newCode = "";
		int i = 6 - size;
		while (i > 0) {
			newCode += "0";
			i--;
		}
		newCode += code.toString();
		PlayBackDbo dbo = new PlayBackDbo();
		dbo.setId(newCode);
		dbo.setGame(game);
		dbo.setGameId(gameId);
		dbo.setPanNo(panNo);
		playBackDboService.save(dbo);
		Map data = new HashMap();
		data.put("code", newCode);
		vo.setSuccess(true);
		vo.setMsg("playbackcode");
		vo.setData(data);
		return vo;
	}

	@RequestMapping(value = "/playback_code")
	public CommonVO playbackCode(String token, String code) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PlayBackDbo dbo = playBackDboService.findById(code);
		if (dbo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid code");
			return vo;
		}
		GameServer gameServer;
		try {
			gameServer = gameService.getRandomGameServer(dbo.getGame());
		} catch (NoServerAvailableForGameException e) {
			vo.setSuccess(false);
			vo.setMsg("NoServerAvailableForGameException");
			return vo;
		}
		GameTable table = gameService.findTableByGameAndServerGameGameId(dbo.getGame(), dbo.getGameId());
		Map data = new HashMap();
		data.put("httpUrl", gameServer.getHttpUrl());
		data.put("gameId", dbo.getGameId());
		data.put("panNo", dbo.getPanNo());
		data.put("roomNo", table.getNo());
		data.put("game", table.getGame());
		vo.setSuccess(true);
		vo.setMsg("playback");
		vo.setData(data);
		return vo;
	}

}
