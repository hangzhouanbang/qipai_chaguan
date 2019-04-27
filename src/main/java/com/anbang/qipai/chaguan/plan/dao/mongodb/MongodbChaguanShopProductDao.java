package com.anbang.qipai.chaguan.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.ChaguanShopProduct;
import com.anbang.qipai.chaguan.plan.dao.ChaguanShopProductDao;
import com.anbang.qipai.chaguan.plan.dao.mongodb.repository.ChaguanShopProductRepository;

@Component
public class MongodbChaguanShopProductDao implements ChaguanShopProductDao {

	@Autowired
	private ChaguanShopProductRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addChaguanShopProduct(ChaguanShopProduct product) {
		mongoTemplate.insert(product);
	}

	@Override
	public void updateChaguanShopProduct(ChaguanShopProduct product) {
		repository.save(product);
	}

	@Override
	public void removeChaguanShopProducts(String[] productIds) {
		Object[] ids = productIds;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(ids));
		mongoTemplate.remove(query, ChaguanShopProduct.class);
	}

	@Override
	public long countAmount() {
		return repository.count();
	}

	@Override
	public List<ChaguanShopProduct> findChaguanShopProduct(int page, int size) {
		Query query = new Query();
		query.skip((page - 1) * size);
		query.limit(size);
		query.with(new Sort(new Order(Direction.ASC, "weight")));
		return mongoTemplate.find(query, ChaguanShopProduct.class);
	}

	@Override
	public ChaguanShopProduct findById(String productId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(productId));
		return mongoTemplate.findOne(query, ChaguanShopProduct.class);
	}

}
