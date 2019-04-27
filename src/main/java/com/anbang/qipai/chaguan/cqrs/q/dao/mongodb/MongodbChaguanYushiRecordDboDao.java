package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanYushiRecordDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;

@Component
public class MongodbChaguanYushiRecordDboDao implements ChaguanYushiRecordDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(ChaguanYushiRecordDbo record) {
		mongoTemplate.insert(record);
	}

	@Override
	public long countBySummary(String text) {
		Query query = new Query();
		query.addCriteria(Criteria.where("summary.text").regex(text));
		return mongoTemplate.count(query, ChaguanYushiRecordDbo.class);
	}

	@Override
	public List<ChaguanYushiRecordDbo> findBySummary(int page, int size, String text) {
		Query query = new Query();
		query.addCriteria(Criteria.where("summary.text").regex(text));
		query.skip((page - 1) * size);
		query.limit(size);
		query.with(new Sort(new Order(Direction.DESC, "accountingTime")));
		return mongoTemplate.find(query, ChaguanYushiRecordDbo.class);
	}

}
