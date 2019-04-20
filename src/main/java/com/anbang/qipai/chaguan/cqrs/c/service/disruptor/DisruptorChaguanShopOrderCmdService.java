package com.anbang.qipai.chaguan.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguanorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguanorder.OrderNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.service.ChaguanShopOrderCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.impl.ChaguanShopOrderCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "chaguanShopOrderCmdService")
public class DisruptorChaguanShopOrderCmdService extends DisruptorCmdServiceBase implements ChaguanShopOrderCmdService {

	@Autowired
	private ChaguanShopOrderCmdServiceImpl chaguanShopOrderCmdServiceImpl;

	@Override
	public String createOrder(String orderId, String payerId, Long currentTime)
			throws OrderHasAlreadyExistenceException {
		CommonCommand cmd = new CommonCommand(ChaguanShopOrderCmdServiceImpl.class.getName(), "createOrder", orderId,
				payerId, currentTime);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String id = chaguanShopOrderCmdServiceImpl.createOrder(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter());
			return id;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderHasAlreadyExistenceException) {
				throw (OrderHasAlreadyExistenceException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public String finishOrder(String orderId) throws OrderNotFoundException {
		CommonCommand cmd = new CommonCommand(ChaguanShopOrderCmdServiceImpl.class.getName(), "finishOrder", orderId);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String id = chaguanShopOrderCmdServiceImpl.finishOrder(cmd.getParameter());
			return id;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof OrderNotFoundException) {
				throw (OrderNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}
