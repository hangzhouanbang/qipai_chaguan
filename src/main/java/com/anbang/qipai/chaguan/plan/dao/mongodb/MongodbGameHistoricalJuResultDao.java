package com.anbang.qipai.chaguan.plan.dao.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameHistoricalJuResult;
import com.anbang.qipai.chaguan.plan.dao.GameHistoricalJuResultDao;
import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBObject;

@Component
public class MongodbGameHistoricalJuResultDao implements GameHistoricalJuResultDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void addGameHistoricalResult(GameHistoricalJuResult result) {
		mongoTemplate.insert(result);
	}

	@Override
	public List<GameHistoricalJuResult> findGameHistoricalResultByMemberId(int page, int size, String memberId) {
		Query query = new Query(Criteria.where("playerResultList.playerId").is(memberId));
		Sort sort = new Sort(new Order(Direction.DESC, "finishTime"));
		query.with(sort);
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, GameHistoricalJuResult.class);
	}

	@Override
	public long getAmountByMemberId(String memberId) {
		Query query = new Query(Criteria.where("playerResultList.playerId").is(memberId));
		return mongoTemplate.count(query, GameHistoricalJuResult.class);
	}

	@Override
	public GameHistoricalJuResult findGameHistoricalResultById(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, GameHistoricalJuResult.class);
	}

	@Override
	public int countGameNumByGameAndTime(Game game, long startTime, long endTime) {
		List<DBObject> pipeline = new ArrayList<>();
		BasicDBObject match = new BasicDBObject();
		BasicDBObject criteria = new BasicDBObject("$gt", startTime);
		criteria.put("$lt", endTime);
		match.put("finishTime", criteria);
		match.put("game", game.name());
		DBObject queryMatch = new BasicDBObject("$match", match);
		pipeline.add(queryMatch);

		BasicDBObject group = new BasicDBObject();
		group.put("_id", null);
		group.put("num", new BasicDBObject("$sum", "$lastPanNo"));
		DBObject queryGroup = new BasicDBObject("$group", group);
		pipeline.add(queryGroup);
		Cursor cursor = mongoTemplate.getCollection("gameHistoricalJuResult").aggregate(pipeline,
				AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build());
		try {
			return (int) cursor.next().get("num");
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public List<GameHistoricalJuResult> findGameHistoricalResultByMemberIdAndChaguanIdAndRoomNoAndTime(int page,
			int size, String chaguanId, String memberId, String roomNo, long startTime, long endTime) {
		Query query = new Query();
		if (StringUtil.isNotBlank(memberId)) {
			query.addCriteria(Criteria.where("dayingjiaId").is(memberId));
		}
		if (StringUtil.isNotBlank(roomNo)) {
			query.addCriteria(Criteria.where("roomNo").is(roomNo));
		}
		if (StringUtil.isNotBlank(chaguanId)) {
			query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		}
		query.addCriteria(Criteria.where("finishTime").gt(startTime).lt(endTime));
		Sort sort = new Sort(new Order(Direction.DESC, "finishTime"));
		query.with(sort);
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, GameHistoricalJuResult.class);
	}

	@Override
	public long getAmountByMemberIdAndChaguanIdAndAndRoomNoTime(String chaguanId, String memberId, String roomNo,
			long startTime, long endTime) {
		Query query = new Query();
		if (StringUtil.isNotBlank(memberId)) {
			query.addCriteria(Criteria.where("dayingjiaId").is(memberId));
		}
		if (StringUtil.isNotBlank(roomNo)) {
			query.addCriteria(Criteria.where("roomNo").is(roomNo));
		}
		if (StringUtil.isNotBlank(chaguanId)) {
			query.addCriteria(Criteria.where("chaguanId").is(chaguanId));
		}
		query.addCriteria(Criteria.where("finishTime").gt(startTime).lt(endTime));
		return mongoTemplate.count(query, GameHistoricalJuResult.class);
	}

}
