package com.anbang.qipai.chaguan.plan.dao;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.FreeReport;

public interface FreeReportDao {

	void save(FreeReport report);

	FreeReport findById(String id);

	long countByAgentId(String agentId);

	List<FreeReport> findByAgentId(String agentId);

	void removeById(String id);
}
