package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanYushiRecordDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.mongodb.BasicDBObject;

@Component
public class MongodbChaguanYushiRecordDboDao implements ChaguanYushiRecordDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(ChaguanYushiRecordDbo record) {
		mongoTemplate.insert(record);
	}

	@Override
	public long countByAgentIdAndSummary(String agentId, String text) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("summary.text").regex(text));
		return mongoTemplate.count(query, ChaguanYushiRecordDbo.class);
	}

	@Override
	public List<ChaguanYushiRecordDbo> findByAgentIdAndSummary(int page, int size, String agentId, String text) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		query.addCriteria(Criteria.where("summary.text").regex(text));
		query.skip((page - 1) * size);
		query.limit(size);
		query.with(new Sort(new Order(Direction.DESC, "accountingTime")));
		return mongoTemplate.find(query, ChaguanYushiRecordDbo.class);
	}

	@Override
	public int countCostByAgentIdAndSummaryAndTime(String agentId, String text, long startTime, long endTime) {
		Aggregation aggregation = Aggregation.newAggregation(ChaguanYushiRecordDbo.class,
				Aggregation.match(Criteria.where("agentId").is(agentId)
						.andOperator(Criteria.where("summary.text").is(text)
								.andOperator(Criteria.where("accountingTime").gt(startTime).lt(endTime)))),
				Aggregation.group().sum("accountingAmount").as("cost"));
		AggregationResults<BasicDBObject> result = mongoTemplate.aggregate(aggregation, ChaguanYushiRecordDbo.class,
				BasicDBObject.class);
		List<BasicDBObject> list = result.getMappedResults();
		if (list.isEmpty()) {
			return 0;
		}
		BasicDBObject basicObj = list.get(0);
		return basicObj.getInt("cost");
	}

}
