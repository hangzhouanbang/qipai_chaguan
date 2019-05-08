package com.anbang.qipai.chaguan.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.MemberDayResult;
import com.anbang.qipai.chaguan.plan.dao.MemberDayResultDao;

@Component
public class MongodbMemberDayResultDao implements MemberDayResultDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(MemberDayResult result) {
		mongoTemplate.insert(result);
	}

	@Override
	public long countByChaguanIdAndTime(String chaguanId, long startTime, long endTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("createTime").gt(startTime).lt(endTime));
		return mongoTemplate.count(query, MemberDayResult.class);
	}

	@Override
	public List<MemberDayResult> findByChaguanIdAndTime(int page, int size, String chaguanId, long startTime,
			long endTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		query.addCriteria(Criteria.where("createTime").gt(startTime).lt(endTime));
		query.skip((page - 1) * size);
		query.limit(size);
		query.with(new Sort(new Order(Direction.DESC, "dayingjiaCount")));
		return mongoTemplate.find(query, MemberDayResult.class);
	}

	@Override
	public MemberDayResult findByPlayerIdAndTime(String playerId, long startTime, long endTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("playerId").is(playerId));
		query.addCriteria(Criteria.where("createTime").gt(startTime).lt(endTime));
		return mongoTemplate.findOne(query, MemberDayResult.class);
	}

	@Override
	public void updateIncById(String id, int dayingjiaCount, int chaguanYushiCost, int totalScore, long createTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = new Update();
		update.inc("dayingjiaCount", dayingjiaCount);
		update.inc("chaguanYushiCost", chaguanYushiCost);
		update.inc("totalScore", totalScore);
		update.set("createTime", createTime);
		mongoTemplate.updateFirst(query, update, MemberDayResult.class);
	}

	@Override
	public void removeById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query, MemberDayResult.class);
	}

	@Override
	public MemberDayResult findById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, MemberDayResult.class);
	}

}
