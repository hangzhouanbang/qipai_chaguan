package com.anbang.qipai.chaguan.plan.dao;

import com.anbang.qipai.chaguan.plan.bean.ChaguanShopOrder;

public interface ChaguanShopOrderDao {

	void addChaguanShopOrder(ChaguanShopOrder order);

	void orderFinished(String id, String transaction_id, String status, long deliveTime);

	ChaguanShopOrder findChaguanShopOrderById(String id);

	ChaguanShopOrder findChaguanShopOrderByPayerIdAndProductName(String payerId, String productName);

}
