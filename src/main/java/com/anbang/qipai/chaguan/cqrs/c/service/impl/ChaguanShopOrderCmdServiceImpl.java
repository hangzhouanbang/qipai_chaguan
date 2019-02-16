package com.anbang.qipai.chaguan.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguanorder.ChaguanShopOrderManager;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguanorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguanorder.OrderNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.service.ChaguanShopOrderCmdService;

@Component
public class ChaguanShopOrderCmdServiceImpl extends CmdServiceBase implements ChaguanShopOrderCmdService {

	@Override
	public String createOrder(String orderId, String payerId, Long currentTime)
			throws OrderHasAlreadyExistenceException {
		ChaguanShopOrderManager chaguanShopOrderManager = singletonEntityRepository
				.getEntity(ChaguanShopOrderManager.class);
		return chaguanShopOrderManager.createOrder(orderId, payerId, currentTime);
	}

	@Override
	public String finishOrder(String orderId) throws OrderNotFoundException {
		ChaguanShopOrderManager chaguanShopOrderManager = singletonEntityRepository
				.getEntity(ChaguanShopOrderManager.class);
		return chaguanShopOrderManager.finishOrder(orderId);
	}

}
