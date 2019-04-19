package com.anbang.qipai.chaguan.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;

public interface MemberChaguanYushiAccountDboDao {

	void insert(MemberChaguanYushiAccountDbo account);

	List<MemberChaguanYushiAccountDbo> findByAgentIdAndChaguanIdAndBalanceLessThan(String agentId, String chaguanId,
			int balance, int page, int size);

	long countByAgentIdAndChaguanIdAndBalanceLessThan(String agentId, String chaguanId, int balance);

	void updateBalance(String id, int balance);

	void updateMemberId(String id, String memberId);

	void updateAgentId(String id, String agentId);

	void remove(String id);

	MemberChaguanYushiAccountDbo findByAccountId(String accountId);

	MemberChaguanYushiAccountDbo findByChaguanIdAndMemberId(String chaguanId, String memberId);
}
