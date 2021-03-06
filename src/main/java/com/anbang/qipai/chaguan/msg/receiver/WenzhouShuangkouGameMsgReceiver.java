package com.anbang.qipai.chaguan.msg.receiver;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.chaguan.cqrs.c.service.GameTableCmdService;
import com.anbang.qipai.chaguan.msg.channel.sink.WenzhouShuangkouGameSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.msg.service.GameTableMsgService;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.service.GameService;
import com.google.gson.Gson;

@EnableBinding(WenzhouShuangkouGameSink.class)
public class WenzhouShuangkouGameMsgReceiver {
	@Autowired
	private GameService gameService;

	@Autowired
	private GameTableCmdService gameTableCmdService;

	@Autowired
	private GameTableMsgService gameTableMsgService;

	private Gson gson = new Gson();

	@StreamListener(WenzhouShuangkouGameSink.WENZHOUSHUANGKOUGAME)
	public void receive(CommonMO mo) {
		String msg = mo.getMsg();
		if ("playerQuit".equals(msg)) {// 有人退出游戏
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			String playerId = (String) data.get("playerId");
			GameTable table = gameService.findTableByGameAndServerGameGameId(Game.wenzhouShuangkou, gameId);
			if (table != null) {
				gameService.gamePlayerQuitQame(Game.wenzhouShuangkou, gameId, playerId);
				gameTableMsgService.updateGameTable(table);
			}
		}
		if ("ju canceled".equals(msg)) {// 取消游戏
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			GameTable gameTable = gameService.findTableByGameAndServerGameGameId(Game.wenzhouShuangkou, gameId);
			if (gameTable != null) {
				gameTableCmdService.removeTable(gameTable.getNo());
				gameService.gameTableFinished(Game.wenzhouShuangkou, gameId);
				gameTableMsgService.updateGameTable(gameTable);
			}
		}
		if ("ju finished".equals(msg)) {// 一局游戏结束
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			GameTable gameTable = gameService.findTableByGameAndServerGameGameId(Game.wenzhouShuangkou, gameId);
			if (gameTable != null) {
				gameTableCmdService.removeTable(gameTable.getNo());
				gameService.gameTableFinished(Game.wenzhouShuangkou, gameId);
				gameTableMsgService.updateGameTable(gameTable);
			}
		}
		if ("pan finished".equals(msg)) {// 一盘游戏结束
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			int no = (int) data.get("no");
			List playerIds = (List) data.get("playerIds");
			GameTable gameTable = gameService.findTableByGameAndServerGameGameId(Game.wenzhouShuangkou, gameId);
			if (gameTable != null) {
				gameService.panFinished(Game.wenzhouShuangkou, gameId, no, playerIds);
				gameTableMsgService.updateGameTable(gameTable);
			}
		}
		if ("game start".equals(msg)) {// 游戏开始
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			GameTable gameTable = gameService.findTableByGameAndServerGameGameId(Game.wenzhouShuangkou, gameId);
			if (gameTable != null) {
				gameService.gameTablePlaying(Game.wenzhouShuangkou, gameId);
				gameTableMsgService.updateGameTable(gameTable);
			}
		}
		if ("game delay".equals(msg)) {// 游戏延时
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			GameTable gameTable = gameService.findTableByGameAndServerGameGameId(Game.wenzhouShuangkou, gameId);
			// 延时1小时
			gameService.delayGameTable(Game.wenzhouShuangkou, gameId, gameTable.getDeadlineTime() + 1 * 60 * 60 * 1000);
		}
	}
}
