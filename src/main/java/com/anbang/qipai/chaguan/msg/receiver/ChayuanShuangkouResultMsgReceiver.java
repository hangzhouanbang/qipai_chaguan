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
import com.anbang.qipai.chaguan.msg.channel.sink.ChayuanShuangkouResultSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalJuResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalPanResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameJuPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GamePanPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.puke.ChayuanShuangkouJuPlayerResult;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.puke.ChayuanShuangkouPanPlayerResult;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalJuResultService;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalPanResultService;
import com.anbang.qipai.chaguan.plan.service.GameService;
import com.dml.accounting.AccountingRecord;
import com.google.gson.Gson;

@EnableBinding(ChayuanShuangkouResultSink.class)
public class ChayuanShuangkouResultMsgReceiver {
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
	private GameService gameService;

	private Gson gson = new Gson();

	@StreamListener(ChayuanShuangkouResultSink.CHAYUANSHUANGKOURESULT)
	public void recordMajiangHistoricalResult(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		Map map = gson.fromJson(json, Map.class);
		if ("chayuanshuangkou ju result".equals(msg)) {
			Object gid = map.get("gameId");
			Object dyjId = map.get("dayingjiaId");
			Object dthId = map.get("datuhaoId");
			if (gid != null && dyjId != null && dthId != null) {
				String gameId = (String) gid;
				GameTable table = gameService.findTableByGameAndServerGameGameId(Game.chayuanShuangkou, gameId);
				if (table != null) {
					ChaguanDbo chaguan = chaguanDboService.findChaguanDboById(table.getChaguanId());
					GameHistoricalJuResult pukeHistoricalResult = new GameHistoricalJuResult();
					pukeHistoricalResult.setGameId(gameId);
					pukeHistoricalResult.setRoomNo(table.getNo());
					pukeHistoricalResult.setGame(Game.chayuanShuangkou);
					pukeHistoricalResult.setDayingjiaId((String) dyjId);
					pukeHistoricalResult.setDatuhaoId((String) dthId);

					Object playerList = map.get("playerResultList");
					if (playerList != null) {
						List<GameJuPlayerResult> juPlayerResultList = new ArrayList<>();
						long finishTime = ((Double) map.get("finishTime")).longValue();
						((List) playerList).forEach((juPlayerResult) -> {
							String playerId = (String) ((Map) juPlayerResult).get("playerId");
							jiesaun(chaguan.getAgentId(), playerId, finishTime);
							juPlayerResultList.add(new ChayuanShuangkouJuPlayerResult((Map) juPlayerResult));
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
		if ("chayuanshuangkou pan result".equals(msg)) {
			Object gid = map.get("gameId");
			if (gid != null) {
				String gameId = (String) gid;
				GameTable table = gameService.findTableByGameAndServerGameGameId(Game.chayuanShuangkou, gameId);
				if (table != null) {
					GameHistoricalPanResult pukeHistoricalResult = new GameHistoricalPanResult();
					pukeHistoricalResult.setGameId(gameId);
					pukeHistoricalResult.setGame(Game.chayuanShuangkou);

					Object playerList = map.get("playerResultList");
					if (playerList != null) {
						List<GamePanPlayerResult> panPlayerResultList = new ArrayList<>();
						((List) playerList).forEach((panPlayerResult) -> panPlayerResultList
								.add(new ChayuanShuangkouPanPlayerResult((Map) panPlayerResult)));
						pukeHistoricalResult.setPlayerResultList(panPlayerResultList);

						pukeHistoricalResult.setNo(((Double) map.get("no")).intValue());
						pukeHistoricalResult.setFinishTime(((Double) map.get("finishTime")).longValue());

						gameHistoricalPanResultService.addGameHistoricalResult(pukeHistoricalResult);
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
			// TODO Kafka发消息 玩家充值记录
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
