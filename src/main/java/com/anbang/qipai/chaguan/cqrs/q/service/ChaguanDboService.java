package com.anbang.qipai.chaguan.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;

@Service
public class ChaguanDboService {

	@Autowired
	private ChaguanDboDao chaguanDboDao;

	public void addChaguanDbo(ChaguanDbo dbo) {
		chaguanDboDao.addChaguanDbo(dbo);
	}

	public void updateChaguanBaseInfo(String chaguanId, String name, String desc) {
		chaguanDboDao.updateChaguanBaseInfo(chaguanId, name, desc);
	}

	public void updateChaguanDboMemberNum(String chaguanId, int memberNum) {
		chaguanDboDao.updateChaguanDboMemberNum(chaguanId, memberNum);
	}
}
