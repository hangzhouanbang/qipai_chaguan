package com.anbang.qipai.chaguan.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;

@Service
public class MemberDboService {

	@Autowired
	private MemberDboDao memberDboDao;

	public void insertMemberDbo(MemberDbo dbo) {
		memberDboDao.insert(dbo);
	}

	public MemberDbo findMemberDboById(String memberId) {
		return memberDboDao.findById(memberId);
	}

	public void updateMemberBaseInfo(String memberId, String nickname, String headimgurl, String gender) {
		memberDboDao.updateMemberBaseInfo(memberId, nickname, headimgurl, gender);
	}

	public void removeMemberDboById(String memberId) {
		memberDboDao.remove(memberId);
	}
}
