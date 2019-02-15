package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;

public interface MemberDboDao {

	void insert(MemberDbo dbo);

	MemberDbo findById(String memberId);

	void updateMemberBaseInfo(String memberId, String nickname, String headimgurl, String gender);

	void remove(String memberId);
}
