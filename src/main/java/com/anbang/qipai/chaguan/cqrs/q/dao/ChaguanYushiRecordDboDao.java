package com.anbang.qipai.chaguan.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;

public interface ChaguanYushiRecordDboDao {

	void insert(ChaguanYushiRecordDbo record);

	long countByAgentIdAndSummary(String agentId, String text);

	List<ChaguanYushiRecordDbo> findByAgentIdAndSummary(int page, int size, String agentId, String text);
}
