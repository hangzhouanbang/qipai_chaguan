package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;

public interface MemberDboDao {

	void insert(MemberDbo dbo);

	MemberDbo findById(String memberId);

	void updateNickname(String memberId, String nickname);

	void updateGender(String memberId, String gender);

	void updateHeadimgurl(String memberId, String headimgurl);

	void remove(String memberId);
}
