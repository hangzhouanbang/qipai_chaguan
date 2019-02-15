package com.anbang.qipai.chaguan.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanMemberDbo;

public interface ChaguanMemberDboDao {

	void add(ChaguanMemberDbo dbo);

	long countAmountByChaguanId(String chaguanId);

	long getAmountByMemberId(String memberId);

	List<ChaguanMemberDbo> findByMemberId(String memberId);
}
