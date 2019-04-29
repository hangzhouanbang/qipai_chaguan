package com.anbang.qipai.chaguan.plan.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.plan.bean.ChaguanShopOrder;
import com.anbang.qipai.chaguan.plan.bean.RewardType;
import com.anbang.qipai.chaguan.plan.dao.ChaguanShopOrderDao;

@Service
public class ChaguanShopOrderService {

	@Autowired
	private ChaguanShopOrderDao chaguanShopOrderDao;

	public ChaguanShopOrder addChaguanShopOrder(String payerId, String payerName, String receiverId,
			String receiverName, String productId, String productName, double productPrice, RewardType rewardType,
			double rewardNum, int number, String payType, String reqIP) {
		String id = UUID.randomUUID().toString().replace("-", "");
		ChaguanShopOrder order = new ChaguanShopOrder();
		order.setId(id);
		order.setPay_type(payType);
		order.setStatus("WAIT_BUYER_PAY");
		order.setPayerId(payerId);
		order.setPayerName(payerName);
		order.setReceiverId(receiverId);
		order.setReceiverName(receiverName);
		order.setProductId(productId);
		order.setProductName(productName);
		order.setProductPrice(
				new BigDecimal(Double.toString(productPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		order.setNumber(number);
		order.setRewardType(rewardType);
		order.setRewardNum(rewardNum * number);
		order.setTotalamount(order.getProductPrice() * number);
		order.setReqIP(reqIP);
		order.setCreateTime(System.currentTimeMillis());
		chaguanShopOrderDao.addChaguanShopOrder(order);
		return order;
	}

	public ChaguanShopOrder orderFinished(String id, String transaction_id, String status, long deliveTime) {
		chaguanShopOrderDao.orderFinished(id, transaction_id, status, deliveTime);
		return chaguanShopOrderDao.findChaguanShopOrderById(id);
	}

	public ChaguanShopOrder findChaguanShopOrderById(String id) {
		return chaguanShopOrderDao.findChaguanShopOrderById(id);
	}
}
