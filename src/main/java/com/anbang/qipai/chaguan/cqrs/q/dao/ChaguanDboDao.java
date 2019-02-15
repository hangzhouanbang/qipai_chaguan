package com.anbang.qipai.chaguan.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;

public interface ChaguanDboDao {

	void addChaguanDbo(ChaguanDbo dbo);

	ChaguanDbo findById(String chaguanId);

	void updateChaguanBaseInfo(String chaguanId, String name, String desc);

	void updateChaguanDboMemberNum(String chaguanId, int memberNum);

	long countAmountByAgentId(String agentId);

	List<ChaguanDbo> findByAgentId(int page, int size, String agentId);
}
