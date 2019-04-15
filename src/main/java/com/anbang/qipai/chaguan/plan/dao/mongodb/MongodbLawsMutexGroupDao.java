package com.anbang.qipai.chaguan.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.game.LawsMutexGroup;
import com.anbang.qipai.chaguan.plan.dao.LawsMutexGroupDao;
import com.anbang.qipai.chaguan.plan.dao.mongodb.repository.LawsMutexGroupRepository;

@Component
public class MongodbLawsMutexGroupDao implements LawsMutexGroupDao {

	@Autowired
	private LawsMutexGroupRepository repository;

	@Override
	public void save(LawsMutexGroup lawsMutexGroup) {
		repository.save(lawsMutexGroup);
	}

	@Override
	public void remove(String id) {
		repository.delete(id);
	}

}
