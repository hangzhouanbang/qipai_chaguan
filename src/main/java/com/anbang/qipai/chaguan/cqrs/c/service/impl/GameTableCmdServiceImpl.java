package com.anbang.qipai.chaguan.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.domain.game.GameTableNoManager;
import com.anbang.qipai.chaguan.cqrs.c.service.GameTableCmdService;

@Component
public class GameTableCmdServiceImpl extends CmdServiceBase implements GameTableCmdService {

	@Override
	public String createTable(String memberId, Long createTime) {
		GameTableNoManager gameTableNoManager = singletonEntityRepository.getEntity(GameTableNoManager.class);
		String roomNo = gameTableNoManager.newNo(createTime);
		return roomNo;
	}

	@Override
	public String removeTable(String no) {
		GameTableNoManager gameTableNoManager = singletonEntityRepository.getEntity(GameTableNoManager.class);
		return gameTableNoManager.removeNo(no);
	}

}
