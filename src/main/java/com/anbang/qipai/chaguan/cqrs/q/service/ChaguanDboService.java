package com.anbang.qipai.chaguan.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanMemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;

@Service
public class ChaguanDboService {

	@Autowired
	private ChaguanDboDao chaguanDboDao;

	@Autowired
	private ChaguanMemberDboDao chaguanMemberDboDao;

	public void addChaguanDbo(ChaguanDbo dbo) {
		chaguanDboDao.addChaguanDbo(dbo);
	}

	public ChaguanDbo findChaguanDboById(String chaguanId) {
		return chaguanDboDao.findById(chaguanId);
	}

	public void updateChaguanBaseInfo(String chaguanId, String name, String desc) {
		chaguanDboDao.updateChaguanBaseInfo(chaguanId, name, desc);
	}

	public void updateChaguanDboMemberNum(String chaguanId) {
		long memberNum = chaguanMemberDboDao.countAmountByChaguanId(chaguanId);
		chaguanDboDao.updateChaguanDboMemberNum(chaguanId, (int) memberNum);
	}
}
