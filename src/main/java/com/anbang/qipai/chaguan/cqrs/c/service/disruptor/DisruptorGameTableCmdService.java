package com.anbang.qipai.chaguan.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.service.GameTableCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.impl.GameTableCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "gameTableCmdService")
public class DisruptorGameTableCmdService extends DisruptorCmdServiceBase implements GameTableCmdService {

	@Autowired
	private GameTableCmdServiceImpl gameRoomCmdServiceImpl;

	@Override
	public String createTable(String memberId, Long createTime) {
		CommonCommand cmd = new CommonCommand(GameTableCmdServiceImpl.class.getName(), "createTable", memberId,
				createTime);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String roomNo = gameRoomCmdServiceImpl.createTable(cmd.getParameter(), cmd.getParameter());
			return roomNo;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String removeTable(String no) {
		CommonCommand cmd = new CommonCommand(GameTableCmdServiceImpl.class.getName(), "removeTable", no);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String roomNo = gameRoomCmdServiceImpl.removeTable(cmd.getParameter());
			return roomNo;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
