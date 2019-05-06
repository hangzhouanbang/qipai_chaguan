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
import com.anbang.qipai.chaguan.msg.channel.sink.WenzhouShuangkouResultSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.msg.service.ChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.msg.service.MemberChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.plan.bean.FreeReport;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameLaw;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalJuResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalPanResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameJuPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GamePanPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.puke.WenzhouShuangkouJuPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.puke.WenzhouShuangkouPanPlayerResult;
import com.anbang.qipai.chaguan.plan.service.FreeReportService;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalJuResultService;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalPanResultService;
import com.anbang.qipai.chaguan.plan.service.GameService;
import com.anbang.qipai.chaguan.web.fb.WzskLawsFB;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.google.gson.Gson;

@EnableBinding(WenzhouShuangkouResultSink.class)
public class WenzhouShuangkouResultMsgReceiver {
	@Autowired
	private GameHistoricalJuResultService gameHistoricalResultService;

	@Autowired
	private GameHistoricalPanResultService gameHistoricalPanResultService;

	@Autowired
	private MemberChaguanYushiCmdService memberChaguanYushiCmdService;

	@Autowired
	private MemberChaguanYushiService memberChaguanYushiService;

	@Autowired
	private ChaguanDboService chaguanDboService;

	@Autowired
	private MemberChaguanYushiRecordMsgService memberChaguanYushiRecordMsgService;

	@Autowired
	private GameService gameService;

	@Autowired
	private AgentChaguanYushiCmdService agentChaguanYushiCmdService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

	@Autowired
	private ChaguanYushiRecordMsgService chaguanYushiRecordMsgService;

	@Autowired
	private FreeReportService freeReportService;

	private Gson gson = new Gson();

	@StreamListener(WenzhouShuangkouResultSink.WENZHOUSHUANGKOURESULT)
	public void recordMajiangHistoricalResult(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		Map map = gson.fromJson(json, Map.class);
		if ("wenzhoushuangkou ju result".equals(msg)) {
			Object gid = map.get("gameId");
			Object dyjId = map.get("dayingjiaId");
			Object dthId = map.get("datuhaoId");
			if (gid != null && dyjId != null && dthId != null) {
				String gameId = (String) gid;
				GameTable table = gameService.findTableByGameAndServerGameGameId(Game.wenzhouShuangkou, gameId);
				if (table != null) {
					ChaguanDbo chaguan = chaguanDboService.findChaguanDboById(table.getChaguanId());
					GameHistoricalJuResult pukeHistoricalResult = new GameHistoricalJuResult();
					pukeHistoricalResult.setGameId(gameId);
					pukeHistoricalResult.setRoomNo(table.getNo());
					pukeHistoricalResult.setGame(Game.wenzhouShuangkou);
					pukeHistoricalResult.setDayingjiaId((String) dyjId);
					pukeHistoricalResult.setDatuhaoId((String) dthId);
					pukeHistoricalResult.setChaguanId(table.getChaguanId());

					long finishTime = ((Double) map.get("finishTime")).longValue();
					Object playerList = map.get("playerResultList");
					if (playerList != null) {
						List<String> laws = new ArrayList<>();
						for (GameLaw law : table.getLaws()) {
							laws.add(law.getName());
						}
						List<GameJuPlayerResult> juPlayerResultList = new ArrayList<>();
						((List) playerList).forEach((juPlayerResult) -> {
							String playerId = (String) ((Map) juPlayerResult).get("playerId");
							jiesaun(chaguan.getAgentId(), playerId, finishTime, laws);
							juPlayerResultList.add(new WenzhouShuangkouJuPlayerResult((Map) juPlayerResult));
						});
						pukeHistoricalResult.setPlayerResultList(juPlayerResultList);

						pukeHistoricalResult.setPanshu(((Double) map.get("panshu")).intValue());
						pukeHistoricalResult.setLastPanNo(((Double) map.get("lastPanNo")).intValue());
						pukeHistoricalResult.setFinishTime(finishTime);

						gameHistoricalResultService.addGameHistoricalResult(pukeHistoricalResult);
					}
				}
			}
		}
		if ("wenzhoushuangkou pan result".equals(msg)) {
			Object gid = map.get("gameId");
			if (gid != null) {
				String gameId = (String) gid;
				GameTable table = gameService.findTableByGameAndServerGameGameId(Game.wenzhouShuangkou, gameId);
				if (table != null) {
					GameHistoricalPanResult pukeHistoricalResult = new GameHistoricalPanResult();
					pukeHistoricalResult.setGameId(gameId);
					pukeHistoricalResult.setGame(Game.wenzhouShuangkou);

					Object playerList = map.get("playerResultList");
					if (playerList != null) {
						List<GamePanPlayerResult> panPlayerResultList = new ArrayList<>();
						((List) playerList).forEach((panPlayerResult) -> panPlayerResultList
								.add(new WenzhouShuangkouPanPlayerResult((Map) panPlayerResult)));
						pukeHistoricalResult.setPlayerResultList(panPlayerResultList);

						pukeHistoricalResult.setNo(((Double) map.get("no")).intValue());
						pukeHistoricalResult.setFinishTime(((Double) map.get("finishTime")).longValue());

						gameHistoricalPanResultService.addGameHistoricalResult(pukeHistoricalResult);
					}
				}
			}
		}
	}

	public void jiesaun(String agentId, String memberId, long finishTime, List<String> lawNames) {
		WzskLawsFB fb = new WzskLawsFB(lawNames);
		int gold = fb.payForCreateRoom();
		try {
			AccountingRecord memberAr = memberChaguanYushiCmdService.withdraw(memberId, agentId, gold, "game ju finish",
					finishTime);
			MemberChaguanYushiRecordDbo memberRecord = memberChaguanYushiService.withdraw(memberAr, memberId, agentId);
			memberChaguanYushiRecordMsgService.recordMemberChaguanYushiRecordDbo(memberRecord);
		} catch (Exception e) {
			if (e instanceof InsufficientBalanceException) {
				try {
					AccountingRecord agentAr = agentChaguanYushiCmdService.withdraw(agentId, gold,
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
					report.setCost(report.getCost() + gold);
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
