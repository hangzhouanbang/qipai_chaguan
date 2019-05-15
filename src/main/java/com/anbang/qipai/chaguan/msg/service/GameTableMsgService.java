package com.anbang.qipai.chaguan.msg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.msg.channel.source.GameTableSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.msg.msjobs.GameTableMO;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.bean.game.MemberGameTable;
import com.anbang.qipai.chaguan.plan.service.GameHistoricalPanResultService;
import com.anbang.qipai.chaguan.plan.service.GameService;

@EnableBinding(GameTableSource.class)
public class GameTableMsgService {

	@Autowired
	private GameTableSource gameTableSource;

	@Autowired
	private GameService gameService;

	@Autowired
	private GameHistoricalPanResultService majiangHistoricalPanResultService;

	public void recordGameTable(GameTable gameTable) {
		CommonMO mo = new CommonMO();
		GameTableMO table = new GameTableMO(gameTable);
		List<MemberGameTable> memberTableList = gameService
				.findMemberGameTableByGameAndServerGameId(gameTable.getGame(), gameTable.getServerGame().getGameId());
		List<String> playerList = new ArrayList<>();
		for (MemberGameTable memberTable : memberTableList) {
			playerList.add(memberTable.getMemberId());
		}
		table.setPlayerList(playerList);
		int finishPanCount = majiangHistoricalPanResultService.countFinishPanResultByGameIdAndGame(gameTable.getGame(),
				gameTable.getServerGame().getGameId());
		table.setCurrentPanNo(finishPanCount);
		mo.setMsg("new gametable");
		mo.setData(table);
		gameTableSource.gameTable().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateGameTable(GameTable gameTable) {
		CommonMO mo = new CommonMO();
		GameTableMO table = new GameTableMO(gameTable);
		List<MemberGameTable> memberTableList = gameService
				.findMemberGameTableByGameAndServerGameId(gameTable.getGame(), gameTable.getServerGame().getGameId());
		List<String> playerList = new ArrayList<>();
		for (MemberGameTable memberTable : memberTableList) {
			playerList.add(memberTable.getMemberId());
		}
		table.setPlayerList(playerList);
		int finishPanCount = majiangHistoricalPanResultService.countFinishPanResultByGameIdAndGame(gameTable.getGame(),
				gameTable.getServerGame().getGameId());
		table.setCurrentPanNo(finishPanCount);
		mo.setMsg("update gametable");
		mo.setData(table);
		gameTableSource.gameTable().send(MessageBuilder.withPayload(mo).build());
	}
}
