package com.anbang.qipai.chaguan.plan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.plan.bean.ChaguanShopProduct;
import com.anbang.qipai.chaguan.plan.dao.ChaguanShopProductDao;
import com.highto.framework.web.page.ListPage;

@Service
public class ChaguanShopProductService {

	@Autowired
	private ChaguanShopProductDao chaguanShopProductDao;

	public void addChaguanShopProduct(ChaguanShopProduct product) {
		chaguanShopProductDao.addChaguanShopProduct(product);
	}

	public void updateChaguanShopProduct(ChaguanShopProduct product) {
		chaguanShopProductDao.updateChaguanShopProduct(product);
	}

	public void removeChaguanShopProducts(String[] productIds) {
		chaguanShopProductDao.removeChaguanShopProducts(productIds);
	}

	public ListPage findChaguanShopProduct(int page, int size) {
		long amount = chaguanShopProductDao.countAmount();
		List<ChaguanShopProduct> productList = chaguanShopProductDao.findChaguanShopProduct(page, size);
		return new ListPage(productList, page, size, (int) amount);
	}

	public ChaguanShopProduct findChaguanShopProductById(String productId) {
		return chaguanShopProductDao.findById(productId);
	}
}
