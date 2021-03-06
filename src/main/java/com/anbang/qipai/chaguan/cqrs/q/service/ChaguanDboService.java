package com.anbang.qipai.chaguan.cqrs.q.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanMemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;
import com.anbang.qipai.chaguan.web.vo.ChaguanVO;
import com.highto.framework.web.page.ListPage;

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

	public ListPage findChaguanDboByAgentId(int page, int size, String agentId) {
		List<ChaguanVO> list = new ArrayList<>();
		long amount = chaguanDboDao.countAmountByAgentId(agentId);
		List<ChaguanDbo> chaguanDboList = chaguanDboDao.findByAgentId(page, size, agentId);
		for (ChaguanDbo dbo : chaguanDboList) {
			ChaguanVO vo = new ChaguanVO();
			vo.setId(dbo.getId());
			vo.setHeadimgurl(dbo.getHeadimgurl());
			vo.setDesc(dbo.getDesc());
			vo.setMemberNum(dbo.getMemberNum());
			vo.setName(dbo.getName());
			vo.setAgentId(agentId);
			vo.setNickname(dbo.getNickname());
			vo.setStatus(dbo.getStatus());
			vo.setOnlineAmount((int) chaguanMemberDboDao.countOnlineMemberByChaguanId(dbo.getId()));
			list.add(vo);
		}
		return new ListPage(list, page, size, (int) amount);
	}

	public ChaguanDbo updateChaguanBaseInfo(String chaguanId, String name, String desc) {
		chaguanDboDao.updateChaguanBaseInfo(chaguanId, name, desc);
		chaguanMemberDboDao.updateChaguanMemberDboChaguanNameAndDescByChaguanId(chaguanId, name, desc);
		return chaguanDboDao.findById(chaguanId);
	}

	public ChaguanDbo updateChaguanDboMemberNum(String chaguanId) {
		long memberNum = chaguanMemberDboDao.countAmountByChaguanId(chaguanId);
		chaguanDboDao.updateChaguanDboMemberNum(chaguanId, (int) memberNum);
		return chaguanDboDao.findById(chaguanId);
	}
}
