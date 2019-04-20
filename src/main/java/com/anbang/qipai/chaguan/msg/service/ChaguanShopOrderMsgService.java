package com.anbang.qipai.chaguan.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.msg.channel.source.ChaguanShopOrderSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.plan.bean.ChaguanShopOrder;

@EnableBinding(ChaguanShopOrderSource.class)
public class ChaguanShopOrderMsgService {

	@Autowired
	private ChaguanShopOrderSource chaguanShopOrderSource;

	public void createChaguanShopOrder(ChaguanShopOrder order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("new order");
		mo.setData(order);
		chaguanShopOrderSource.chaguanShopOrder().send(MessageBuilder.withPayload(mo).build());
	}

	public void finishChaguanShopOrder(ChaguanShopOrder order) {
		CommonMO mo = new CommonMO();
		mo.setMsg("finish order");
		mo.setData(order);
		chaguanShopOrderSource.chaguanShopOrder().send(MessageBuilder.withPayload(mo).build());
	}

}
