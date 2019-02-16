package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.ChaguanShopProduct;

public interface ChaguanShopProductDao {

	void addChaguanShopProduct(ChaguanShopProduct product);

	void updateChaguanShopProduct(ChaguanShopProduct product);

	void removeChaguanShopProducts(String[] productIds);

	long countAmount();

	List<ChaguanShopProduct> findChaguanShopProduct(int page, int size);

	ChaguanShopProduct findById(String productId);
}
