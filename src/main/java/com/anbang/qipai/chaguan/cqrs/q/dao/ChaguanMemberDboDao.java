package com.anbang.qipai.chaguan.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanMemberDbo;

public interface ChaguanMemberDboDao {

	void add(ChaguanMemberDbo dbo);

	long countOnlineMemberByChaguanId(String chaguanId);

	long countAmountByChaguanId(String chaguanId);

	List<ChaguanMemberDbo> findByChaguanId(int page, int size, String chaguanId);

	long getAmountByMemberId(String memberId);

	List<ChaguanMemberDbo> findByMemberId(int page, int size, String memberId);

	ChaguanMemberDbo findByMemberIdAndChaguanId(String memberId, String chaguanId, boolean remove);

	void updateChaguanMemberDboRemoveByMemberIdAndChaguanId(String memberId, String chaguanId, boolean remove);

	void updateChaguanMemberDboYushiByMemberIdAndAgentId(String memberId, String agentId, int amount);

	void updateChaguanMemberDboChaguanNameAndDescByChaguanId(String chaguanId, String chaguanName, String chaguanDesc);

	void updateChaguanMemberDboMemberDescByMemberIdAndChaguanId(String memberId, String chaguanId, String memberDesc);

	void updateChaguanMemberDboPayTypeByMemberIdAndChaguanId(String memberId, String chaguanId, String payType);

	void updateChaguanMemberDboOnlineStatusByMemberId(String memberId, String onlineStatus);
}
