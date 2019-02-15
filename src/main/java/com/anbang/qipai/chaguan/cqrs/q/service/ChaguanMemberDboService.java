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

	public ListPage findByMemberId(int page, int size, String memberId) {
		long amount = chaguanMemberDboDao.getAmountByMemberId(memberId);
		List<ChaguanMemberDbo> chaguanList = chaguanMemberDboDao.findByMemberId(memberId);
		return new ListPage(chaguanList, page, size, (int) amount);
	}
}
