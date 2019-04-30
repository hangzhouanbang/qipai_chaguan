package com.anbang.qipai.chaguan.plan.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.chaguan.plan.bean.FreeReport;
import com.anbang.qipai.chaguan.plan.dao.FreeReportDao;
import com.anbang.qipai.chaguan.web.vo.FreeReportVO;
import com.highto.framework.web.page.ListPage;

@Service
public class FreeReportService {

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private FreeReportDao freeReportDao;

	public void saveFreeReport(FreeReport report) {
		freeReportDao.save(report);
	}

	public FreeReport findFreeReportById(String id) {
		return freeReportDao.findById(id);
	}

	public ListPage findFreeReportVOByMemberId(String agentId, String chaguanId, int page, int size) {
		List<FreeReportVO> reportList = new ArrayList<>();
		int amount = (int) freeReportDao.countByAgentId(agentId);
		List<FreeReport> accountList = freeReportDao.findByAgentId(agentId);
		for (FreeReport account : accountList) {
			FreeReportVO vo = new FreeReportVO();
			vo.setAccountId(account.getId());
			vo.setMemberId(account.getMemberId());
			MemberDbo member = memberDboDao.findById(account.getMemberId());
			vo.setNickname(member.getNickname());
			vo.setHeadimgurl(member.getHeadimgurl());
			vo.setCost(account.getCost());
			vo.setFreeCount(account.getFreeCount());
			reportList.add(vo);
		}
		return new ListPage(reportList, page, size, amount);
	}

	public void removeFreeReportById(String id) {
		freeReportDao.removeById(id);
	}

}
