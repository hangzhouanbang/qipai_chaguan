package com.anbang.qipai.chaguan.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.plan.bean.FreeReport;
import com.anbang.qipai.chaguan.plan.dao.FreeReportDao;
import com.anbang.qipai.chaguan.plan.dao.mongodb.repository.FreeReportRepository;

@Component
public class MongodbFreeReportDao implements FreeReportDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private FreeReportRepository repository;

	@Override
	public void save(FreeReport report) {
		repository.save(report);
	}

	@Override
	public long countByAgentId(String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		return mongoTemplate.count(query, FreeReport.class);
	}

	@Override
	public List<FreeReport> findByAgentId(String agentId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("agentId").is(agentId));
		return mongoTemplate.find(query, FreeReport.class);
	}

	@Override
	public FreeReport findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public void removeById(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoTemplate.remove(query, FreeReport.class);
	}

}
