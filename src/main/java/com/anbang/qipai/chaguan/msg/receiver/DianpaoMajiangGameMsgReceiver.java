package com.anbang.qipai.chaguan.msg.receiver;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.chaguan.cqrs.c.service.GameTableCmdService;
import com.anbang.qipai.chaguan.msg.channel.sink.DianpaoMajiangGameSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.service.GameService;
import com.google.gson.Gson;

@EnableBinding(DianpaoMajiangGameSink.class)
public class DianpaoMajiangGameMsgReceiver {
	@Autowired
	private GameService gameService;

	@Autowired
	private GameTableCmdService gameTableCmdService;

	private Gson gson = new Gson();

	@StreamListener(DianpaoMajiangGameSink.DIANPAOMAJIANGGAME)
	public void receive(CommonMO mo) {
		String msg = mo.getMsg();
		if ("playerQuit".equals(msg)) {// 有人退出游戏
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			String playerId = (String) data.get("playerId");
			GameTable table = gameService.findTableByGameAndServerGameGameId(Game.dianpaoMajiang, gameId);
			if (table != null) {
				gameService.gamePlayerQuitQame(Game.dianpaoMajiang, gameId, playerId);
			}
		}
		if ("ju canceled".equals(msg)) {// 取消游戏
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			GameTable gameTable = gameService.findTableByGameAndServerGameGameId(Game.dianpaoMajiang, gameId);
			if (gameTable != null) {
				gameTableCmdService.removeTable(gameTable.getNo());
				gameService.gameTableFinished(Game.dianpaoMajiang, gameId);
			}
		}
		if ("ju finished".equals(msg)) {// 一局游戏结束
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			GameTable gameTable = gameService.findTableByGameAndServerGameGameId(Game.dianpaoMajiang, gameId);
			if (gameTable != null) {
				gameTableCmdService.removeTable(gameTable.getNo());
				gameService.gameTableFinished(Game.dianpaoMajiang, gameId);
			}
		}
		if ("pan finished".equals(msg)) {// 一盘游戏结束
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			int no = (int) data.get("no");
			List playerIds = (List) data.get("playerIds");
		}
		if ("game start".equals(msg)) {// 游戏开始
			Map data = (Map) mo.getData();
			String gameId = (String) data.get("gameId");
			GameTable gameTable = gameService.findTableByGameAndServerGameGameId(Game.dianpaoMajiang, gameId);
			if (gameTable != null) {
				gameService.gameTablePlaying(Game.dianpaoMajiang, gameId);
			}
		}
	}
}
