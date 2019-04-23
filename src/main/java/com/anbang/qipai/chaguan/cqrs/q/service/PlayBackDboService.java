package com.anbang.qipai.chaguan.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.PlayBackDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.PlayBackDbo;
import com.anbang.qipai.chaguan.plan.bean.game.Game;

@Service
public class PlayBackDboService {

	@Autowired
	private PlayBackDboDao playBackDboDao;

	public void save(PlayBackDbo dbo) {
		playBackDboDao.save(dbo);
	}

	public PlayBackDbo findById(String id) {
		return playBackDboDao.findById(id);
	}

	public PlayBackDbo findByGameAndGameIdAndPanNo(Game game, String gameId, int panNo) {
		return playBackDboDao.findByGameAndGameIdAndPanNo(game, gameId, panNo);
	}
}
