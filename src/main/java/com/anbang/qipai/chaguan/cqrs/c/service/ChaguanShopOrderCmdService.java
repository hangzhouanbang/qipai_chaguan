package com.anbang.qipai.chaguan.cqrs.c.service;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguanorder.OrderHasAlreadyExistenceException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguanorder.OrderNotFoundException;

public interface ChaguanShopOrderCmdService {

	String createOrder(String orderId, String payerId, Long currentTime) throws OrderHasAlreadyExistenceException;

	String finishOrder(String orderId) throws OrderNotFoundException;
}
