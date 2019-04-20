package com.anbang.qipai.chaguan.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.msg.channel.source.ChaguanShopProductSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.plan.bean.ChaguanShopProduct;

@EnableBinding(ChaguanShopProductSource.class)
public class ChaguanShopProductMsgService {

	@Autowired
	private ChaguanShopProductSource chaguanShopProductSource;

	public void addChaguanShopProduct(ChaguanShopProduct product) {
		CommonMO mo = new CommonMO();
		mo.setMsg("new product");
		mo.setData(product);
		chaguanShopProductSource.chaguanShopProduct().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateChaguanShopProduct(ChaguanShopProduct product) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update product");
		mo.setData(product);
		chaguanShopProductSource.chaguanShopProduct().send(MessageBuilder.withPayload(mo).build());
	}

	public void removeChaguanShopProduct(String[] productIds) {
		CommonMO mo = new CommonMO();
		mo.setMsg("remove products");
		mo.setData(productIds);
		chaguanShopProductSource.chaguanShopProduct().send(MessageBuilder.withPayload(mo).build());
	}

}
