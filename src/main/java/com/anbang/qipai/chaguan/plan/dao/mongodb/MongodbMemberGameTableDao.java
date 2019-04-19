package com.anbang.qipai.chaguan.plan.dao.mongodb;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.MemberGameTable;
import com.anbang.qipai.chaguan.plan.dao.MemberGameTableDao;
import com.anbang.qipai.chaguan.plan.dao.mongodb.repository.MemberGameTableRepository;

@Component
public class MongodbMemberGameTableDao implements MemberGameTableDao {

	@Autowired
	private MemberGameTableRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(MemberGameTable memberGameTable) {
		repository.save(memberGameTable);
	}

	@Override
	public long count(String memberId) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("memberId").is(memberId));
		query.addCriteria(criteria);
		return mongoTemplate.count(query, MemberGameTable.class);
	}

	@Override
	public MemberGameTable findByMemberIdAndGameTableId(String memberId, String gameTableId) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("memberId").is(memberId),
				Criteria.where("gameTable.id").is(new ObjectId(gameTableId)));
		query.addCriteria(criteria);
		return mongoTemplate.findOne(query, MemberGameTable.class);
	}

	@Override
	public void remove(Game game, String serverGameId, String memberId) {
		repository.deleteByMemberIdAndGameTableGameAndGameTableServerGameGameId(memberId, game, serverGameId);
	}

	@Override
	public void removeExpireRoom(Game game, String serverGameId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("gameTable.game").is(game));
		query.addCriteria(Criteria.where("gameTable.serverGame.gameId").is(serverGameId));
		mongoTemplate.remove(query, MemberGameTable.class);
	}

	@Override
	public List<MemberGameTable> findMemberGameTableByMemberId(String memberId) {
		Query query = new Query(Criteria.where("memberId").is(memberId));
		query.with(new Sort(new Order(Direction.DESC, "gameTable.createTime")));
		return mongoTemplate.find(query, MemberGameTable.class);
	}

	@Override
	public long countMemberByGameAndServerGameId(Game game, String serverGameId) {
		Query query = new Query(Criteria.where("gameTable.game").is(game));
		query.addCriteria(Criteria.where("gameTable.serverGame.gameId").is(serverGameId));
		return mongoTemplate.count(query, MemberGameTable.class);
	}

	@Override
	public List<MemberGameTable> findMemberGameTableByGameAndServerGameId(Game game, String serverGameId) {
		Query query = new Query(Criteria.where("gameTable.game").is(game));
		query.addCriteria(Criteria.where("gameTable.serverGame.gameId").is(serverGameId));
		return mongoTemplate.find(query, MemberGameTable.class);
	}

}
