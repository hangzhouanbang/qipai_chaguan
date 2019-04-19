package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

}
