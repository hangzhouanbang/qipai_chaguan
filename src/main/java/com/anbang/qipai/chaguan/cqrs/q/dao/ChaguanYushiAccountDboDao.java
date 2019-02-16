package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiAccountDbo;

public interface ChaguanYushiAccountDboDao {

	void insert(ChaguanYushiAccountDbo account);

	ChaguanYushiAccountDbo findByAgentId(String agentId);

	void updateBalance(String id, int balance);

	void updateAgentId(String id, String agentId);

	void remove(String id);
}
