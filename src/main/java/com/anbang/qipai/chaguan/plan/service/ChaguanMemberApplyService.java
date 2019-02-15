package com.anbang.qipai.chaguan.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApply;
import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApplyState;
import com.anbang.qipai.chaguan.plan.dao.ChaguanMemberApplyDao;

@Service
public class ChaguanMemberApplyService {

	@Autowired
	private ChaguanMemberApplyDao chaguanMemberApplyDao;

	public void addChaguanMemberApply(ChaguanMemberApply apply) {
		chaguanMemberApplyDao.add(apply);
	}

	public ChaguanMemberApply findChaguanMemberApplyById(String applyId) {
		return chaguanMemberApplyDao.findById(applyId);
	}

	public void chaguanMemberApplyPass(String applyId) {
		chaguanMemberApplyDao.updateState(applyId, ChaguanMemberApplyState.SUCCESS);
	}

	public void chaguanMemberApplyRefuse(String applyId) {
		chaguanMemberApplyDao.updateState(applyId, ChaguanMemberApplyState.FAIL);
	}
}
