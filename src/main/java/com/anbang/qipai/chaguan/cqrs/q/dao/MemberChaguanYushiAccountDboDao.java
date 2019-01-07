package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;

public interface MemberChaguanYushiAccountDboDao {

	void insert(MemberChaguanYushiAccountDbo account);

	void updateBalance(String id, int balance);

	void updateMemberId(String id, String memberId);

	void updateAgentId(String id, String agentId);

	void remove(String id);
}
