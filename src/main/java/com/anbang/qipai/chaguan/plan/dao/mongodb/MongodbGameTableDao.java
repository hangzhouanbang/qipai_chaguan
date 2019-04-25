package com.anbang.qipai.chaguan.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.conf.GameTableStateConfig;
import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameTable;
import com.anbang.qipai.chaguan.plan.dao.GameTableDao;
import com.anbang.qipai.chaguan.plan.dao.mongodb.repository.GameTableRepository;

@Component
public class MongodbGameTableDao implements GameTableDao {

	@Autowired
	private GameTableRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(GameTable gameTable) {
		repository.save(gameTable);
	}

	@Override
	public GameTable findTableOpen(String roomNo) {
		Query query = new Query();
		query.addCriteria(Criteria.where("no").is(roomNo));
		query.addCriteria(Criteria.where("state").in(GameTableStateConfig.WAITING, GameTableStateConfig.PLAYING));
		return mongoTemplate.findOne(query, GameTable.class);
	}

	@Override
	public GameTable findTableByGameAndServerGameGameId(Game game, String serverGameId) {
		Query query = new Query();
		query.addCriteria(
				Criteria.where("game").is(game).andOperator(Criteria.where("serverGame.gameId").is(serverGameId)));
		return mongoTemplate.findOne(query, GameTable.class);
	}

	@Override
	public List<GameTable> findExpireGameTable(long deadlineTime, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("deadlineTime").lte(deadlineTime));
		query.addCriteria(Criteria.where("state").is(state));
		return mongoTemplate.find(query, GameTable.class);
	}

	@Override
	public void updateGameTableState(List<String> ids, String state) {
		Query query = new Query(Criteria.where("id").in(ids));
		Update update = new Update();
		update.set("state", state);
		mongoTemplate.updateMulti(query, update, GameTable.class);
	}

	@Override
	public void updateStateGameTable(Game game, String serverGameId, String state) {
		Query query = new Query();
		query.addCriteria(Criteria.where("game").is(game));
		query.addCriteria(Criteria.where("serverGame.gameId").is(serverGameId));
		Update update = new Update();
		update.set("state", state);
		mongoTemplate.updateFirst(query, update, GameTable.class);
	}

	@Override
	public List<GameTable> findGameTableByChaguanId(String chaguanId, int page, int size) {
		Query query = new Query();
		query.addCriteria(Criteria.where("chaguanId").is(chaguanId)
				.andOperator(Criteria.where("state").in(GameTableStateConfig.PLAYING, GameTableStateConfig.WAITING)));
		query.skip((page - 1) * size);
		query.limit(size);
		return mongoTemplate.find(query, GameTable.class);
	}

}
