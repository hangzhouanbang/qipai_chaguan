package com.anbang.qipai.chaguan.msg.receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.chaguan.cqrs.c.service.MemberChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberChaguanYushiService;
import com.anbang.qipai.chaguan.msg.channel.sink.PaodekuaiResultSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.msg.service.MemberChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalJuResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalPanResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameJuPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GamePanPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.puke.PaodekuaiJuPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.puke.PaodekuaiPanPlayerResult;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalJuResultService;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalPanResultService;
import com.anbang.qipai.chaguan.plan.service.GameService;
import com.dml.accounting.AccountingRecord;
import com.google.gson.Gson;

@EnableBinding(PaodekuaiResultSink.class)
public class PaodekuaiResultMsgReceiver {
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
	private Gson gson = new Gson();

	@StreamListener(PaodekuaiResultSink.PAODEKUAIRESULT)
	public void recordMajiangHistoricalResult(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		Map map = gson.fromJson(json, Map.class);
		if ("paodekuai ju result".equals(msg)) {
			Object gid = map.get("gameId");
			Object dyjId = map.get("dayingjiaId");
			Object dthId = map.get("datuhaoId");
			if (gid != null && dyjId != null && dthId != null) {
				String gameId = (String) gid;
				GameTable table = gameService.findTableByGameAndServerGameGameId(Game.paodekuai, gameId);
				if (table != null) {
					ChaguanDbo chaguan = chaguanDboService.findChaguanDboById(table.getChaguanId());
					GameHistoricalJuResult majiangHistoricalResult = new GameHistoricalJuResult();
					majiangHistoricalResult.setGameId(gameId);
					majiangHistoricalResult.setRoomNo(table.getNo());
					majiangHistoricalResult.setGame(Game.paodekuai);
					majiangHistoricalResult.setDayingjiaId((String) dyjId);
					majiangHistoricalResult.setDatuhaoId((String) dthId);
					long finishTime = ((Double) map.get("finishTime")).longValue();
					Object playerList = map.get("playerResultList");
					if (playerList != null) {
						List<GameJuPlayerResult> juPlayerResultList = new ArrayList<>();
						((List) playerList).forEach((juPlayerResult) -> {
							String playerId = (String) ((Map) juPlayerResult).get("playerId");
							jiesaun(chaguan.getAgentId(), playerId, finishTime);
							juPlayerResultList.add(new PaodekuaiJuPlayerResult((Map) juPlayerResult));
						});
						majiangHistoricalResult.setPlayerResultList(juPlayerResultList);

						majiangHistoricalResult.setPanshu(((Double) map.get("panshu")).intValue());
						majiangHistoricalResult.setLastPanNo(((Double) map.get("lastPanNo")).intValue());
						majiangHistoricalResult.setFinishTime(finishTime);

						majiangHistoricalResultService.addGameHistoricalResult(majiangHistoricalResult);
					}
				}
			}
		}
		if ("paodekuai pan result".equals(msg)) {
			Object gid = map.get("gameId");
			if (gid != null) {
				String gameId = (String) gid;
				GameTable table = gameService.findTableByGameAndServerGameGameId(Game.paodekuai, gameId);
				if (table != null) {
					GameHistoricalPanResult majiangHistoricalResult = new GameHistoricalPanResult();
					majiangHistoricalResult.setGameId(gameId);
					majiangHistoricalResult.setGame(Game.paodekuai);

					Object playerList = map.get("playerResultList");
					if (playerList != null) {
						List<GamePanPlayerResult> panPlayerResultList = new ArrayList<>();
						((List) map.get("playerResultList")).forEach((panPlayerResult) -> panPlayerResultList
								.add(new PaodekuaiPanPlayerResult((Map) panPlayerResult)));
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
			AccountingRecord memberAr = memberChaguanYushiCmdService.withdrawAnyway(memberId, agentId, 100,
					"game ju finish", finishTime);
			MemberChaguanYushiRecordDbo memberRecord = memberChaguanYushiService.withdraw(memberAr, memberId, agentId);
			memberChaguanYushiRecordMsgService.recordMemberChaguanYushiRecordDbo(memberRecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
