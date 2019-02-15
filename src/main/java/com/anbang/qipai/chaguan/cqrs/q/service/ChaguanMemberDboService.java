package com.anbang.qipai.chaguan.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanMemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanMemberDbo;
import com.highto.framework.web.page.ListPage;

@Service
public class ChaguanMemberDboService {

	@Autowired
	private ChaguanMemberDboDao chaguanMemberDboDao;

	public void addChaguanMemberDbo(ChaguanMemberDbo dbo) {
		chaguanMemberDboDao.add(dbo);
	}

	public long countOnlineMemberByChaguanId(String chaguanId) {
		return chaguanMemberDboDao.countOnlineMemberByChaguanId(chaguanId);
	}

	public ListPage findChaguanMemberDboByMemberId(int page, int size, String memberId) {
		long amount = chaguanMemberDboDao.getAmountByMemberId(memberId);
		List<ChaguanMemberDbo> chaguanList = chaguanMemberDboDao.findByMemberId(page, size, memberId);
		return new ListPage(chaguanList, page, size, (int) amount);
	}

	public ListPage findChaguanMemberDboByChaguanId(int page, int size, String chaguanId) {
		long amount = chaguanMemberDboDao.countAmountByChaguanId(chaguanId);
		List<ChaguanMemberDbo> chaguanList = chaguanMemberDboDao.findByChaguanId(page, size, chaguanId);
		return new ListPage(chaguanList, page, size, (int) amount);
	}

	public void updateChaguanMemberDboRemoveByMemberId(String memberId, boolean remove) {
		chaguanMemberDboDao.updateChaguanMemberDboRemoveByMemberId(memberId, remove);
	}
}