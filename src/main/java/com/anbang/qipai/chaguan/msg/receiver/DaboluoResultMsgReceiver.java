package com.anbang.qipai.chaguan.msg.receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.MemberChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberChaguanYushiService;
import com.anbang.qipai.chaguan.msg.channel.sink.DaboluoResultSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.msg.service.ChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.msg.service.MemberChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.plan.bean.FreeReport;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalJuResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalPanResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameJuPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GamePanPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.majiang.DianpaoMajiangJuPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.majiang.DianpaoMajiangPanPlayerResult;
import com.anbang.qipai.chaguan.plan.service.FreeReportService;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalJuResultService;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalPanResultService;
import com.anbang.qipai.chaguan.plan.service.GameService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.google.gson.Gson;

@EnableBinding(DaboluoResultSink.class)
public class DaboluoResultMsgReceiver {
	@Autowired
	private GameHistoricalJuResultService majiangHistoricalResultService;

	@Autowired
	private GameHistoricalPanResultService majiangHistoricalPanResultService;

	@Autowired
	private MemberChaguanYushiCmdService memberChaguanYushiCmdService;

	@Autowired
	private MemberChaguanYushiService memberChaguanYushiService;

	@Autowired
	private ChaguanDboService chaguanDboService;

	@Autowired
	private GameService gameService;

	@Autowired
	private MemberChaguanYushiRecordMsgService memberChaguanYushiRecordMsgService;

	@Autowired
	private AgentChaguanYushiCmdService agentChaguanYushiCmdService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

	@Autowired
	private ChaguanYushiRecordMsgService chaguanYushiRecordMsgService;

	@Autowired
	private FreeReportService freeReportService;

	private Gson gson = new Gson();

	@StreamListener(DaboluoResultSink.DABOLUORESULT)
	public void recordMajiangHistoricalResult(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		Map map = gson.fromJson(json, Map.class);
		if ("daboluo ju result".equals(msg)) {
			Object gid = map.get("gameId");
			Object dyjId = map.get("dayingjiaId");
			Object dthId = map.get("datuhaoId");
			if (gid != null && dyjId != null && dthId != null) {
				String gameId = (String) gid;
				GameTable table = gameService.findTableByGameAndServerGameGameId(Game.daboluo, gameId);
				if (table != null) {
					ChaguanDbo chaguan = chaguanDboService.findChaguanDboById(table.getChaguanId());
					GameHistoricalJuResult pukeHistoricalResult = new GameHistoricalJuResult();
					pukeHistoricalResult.setGameId(gameId);
					pukeHistoricalResult.setRoomNo(table.getNo());
					pukeHistoricalResult.setGame(Game.daboluo);
					pukeHistoricalResult.setDayingjiaId((String) dyjId);
					pukeHistoricalResult.setDatuhaoId((String) dthId);
					pukeHistoricalResult.setChaguanId(table.getChaguanId());

					long finishTime = ((Double) map.get("finishTime")).longValue();
					Object playerList = map.get("playerResultList");
					if (playerList != null) {
						List<GameJuPlayerResult> juPlayerResultList = new ArrayList<>();
						((List) playerList).forEach((juPlayerResult) -> {
							String playerId = (String) ((Map) juPlayerResult).get("playerId");
							jiesaun(chaguan.getAgentId(), playerId, finishTime);
							juPlayerResultList.add(new DianpaoMajiangJuPlayerResult((Map) juPlayerResult));
						});
						pukeHistoricalResult.setPlayerResultList(juPlayerResultList);

						pukeHistoricalResult.setPanshu(((Double) map.get("panshu")).intValue());
						pukeHistoricalResult.setLastPanNo(((Double) map.get("lastPanNo")).intValue());
						pukeHistoricalResult.setFinishTime(finishTime);

						majiangHistoricalResultService.addGameHistoricalResult(pukeHistoricalResult);
					}
				}
			}
		}
		if ("daboluo pan result".equals(msg)) {
			Object gid = map.get("gameId");
			if (gid != null) {
				String gameId = (String) gid;
				GameTable table = gameService.findTableByGameAndServerGameGameId(Game.daboluo, gameId);
				if (table != null) {
					GameHistoricalPanResult majiangHistoricalResult = new GameHistoricalPanResult();
					majiangHistoricalResult.setGameId(gameId);
					majiangHistoricalResult.setGame(Game.daboluo);

					Object playerList = map.get("playerResultList");
					if (playerList != null) {
						List<GamePanPlayerResult> panPlayerResultList = new ArrayList<>();
						((List) map.get("playerResultList")).forEach((panPlayerResult) -> panPlayerResultList
								.add(new DianpaoMajiangPanPlayerResult((Map) panPlayerResult)));
						majiangHistoricalResult.setPlayerResultList(panPlayerResultList);

						majiangHistoricalResult.setNo(((Double) map.get("no")).intValue());
						majiangHistoricalResult.setFinishTime(((Double) map.get("finishTime")).longValue());

						majiangHistoricalPanResultService.addGameHistoricalResult(majiangHistoricalResult);
					}
				}
			}
		}
	}

	public void jiesaun(String agentId, String memberId, long finishTime) {
		try {
			AccountingRecord memberAr = memberChaguanYushiCmdService.withdraw(memberId, agentId, 100, "game ju finish",
					finishTime);
			MemberChaguanYushiRecordDbo memberRecord = memberChaguanYushiService.withdraw(memberAr, memberId, agentId);
			memberChaguanYushiRecordMsgService.recordMemberChaguanYushiRecordDbo(memberRecord);
		} catch (Exception e) {
			if (e instanceof InsufficientBalanceException) {
				try {
					AccountingRecord agentAr = agentChaguanYushiCmdService.withdraw(agentId, 100,
							"free ju finish:" + memberId, finishTime);
					ChaguanYushiRecordDbo dbo = chaguanYushiService.withdraw(agentAr, agentId);
					chaguanYushiRecordMsgService.recordChaguanYushiRecordDbo(dbo);
					MemberChaguanYushiAccountDbo account = memberChaguanYushiService
							.findMemberChaguanYushiAccountDboByAgentIdAndMemebrId(agentId, memberId);
					FreeReport report = freeReportService.findFreeReportById(account.getId());
					if (report == null) {
						report = new FreeReport();
					}
					report.setId(account.getId());
					report.setMemberId(memberId);
					report.setAgentId(agentId);
					report.setFreeCount(report.getFreeCount() + 1);
					report.setCost(report.getCost() + 100);
					freeReportService.saveFreeReport(report);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				e.printStackTrace();
			}
		}
	}
}
