package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.MemberChaguanYushiRecordDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;
import com.mongodb.BasicDBObject;

@Component
public class MongodbMemberChaguanYushiRecordDboDao implements MemberChaguanYushiRecordDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(MemberChaguanYushiRecordDbo record) {
		mongoTemplate.insert(record);
	}

	@Override
	public long countByMemberIdAndLastestBalanceAfterLessThan(String memberId, int balanceAfter) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(memberId));
		query.addCriteria(Criteria.where("balanceAfter").lt(balanceAfter));
		return mongoTemplate.count(query, MemberChaguanYushiRecordDbo.class);
	}

	@Override
	public int countCostByMemberIdAndAgentIdAndSummaryAndTime(String memberId, String agentId, String text,
			long startTime, long endTime) {
		Aggregation aggregation = Aggregation.newAggregation(ChaguanYushiRecordDbo.class,
				Aggregation.match(
						Criteria.where("memberId").is(memberId).andOperator(Criteria.where("agentId").is(agentId))
								.andOperator(Criteria.where("accountingTime").gt(startTime).lt(endTime))
								.andOperator(Criteria.where("summary.text").is(text))),
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
