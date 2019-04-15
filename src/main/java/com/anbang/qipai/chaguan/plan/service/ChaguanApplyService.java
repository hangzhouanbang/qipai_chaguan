package com.anbang.qipai.chaguan.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.plan.bean.ChaguanApply;
import com.anbang.qipai.chaguan.plan.dao.ChaguanApplyDao;

@Service
public class ChaguanApplyService {

	@Autowired
	private ChaguanApplyDao chaguanApplyDao;

	public void addApply(ChaguanApply chaguanApply) {
		chaguanApplyDao.addApply(chaguanApply);
	}

	public ChaguanApply fingChaguanApplyByApplyId(String applyId) {
		return chaguanApplyDao.fingByApplyId(applyId);
	}

	public ChaguanApply fingChaguanApplyByAgentIdAndStatus(String agentId, String status) {
		return chaguanApplyDao.fingByAgentIdAndStatus(agentId, status);
	}

	public void updateApplyStatus(String applyId, String status) {
		chaguanApplyDao.updateApplyStatus(applyId, status);
	}
}
