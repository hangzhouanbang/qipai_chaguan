package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.q.dao.MemberChaguanYushiRecordDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;

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

}
