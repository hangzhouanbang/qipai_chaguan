package com.anbang.qipai.chaguan.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.service.ChaguanCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.impl.ChaguanCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "chaguanCmdService")
public class DisruptorChaguanCmdService extends DisruptorCmdServiceBase implements ChaguanCmdService {

	@Autowired
	private ChaguanCmdServiceImpl chaguanCmdServiceImpl;

	@Override
	public String createNewChaguanId(Long currentTime) {
		CommonCommand cmd = new CommonCommand(ChaguanCmdServiceImpl.class.getName(), "createNewChaguanId", currentTime);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String chaguanId = chaguanCmdServiceImpl.createNewChaguanId(cmd.getParameter());
			return chaguanId;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String removeChaguan(String chaguanId) {
		CommonCommand cmd = new CommonCommand(ChaguanCmdServiceImpl.class.getName(), "removeChaguan", chaguanId);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String id = chaguanCmdServiceImpl.removeChaguan(cmd.getParameter());
			return id;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
