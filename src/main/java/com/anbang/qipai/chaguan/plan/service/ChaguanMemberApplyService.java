package com.anbang.qipai.chaguan.plan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApply;
import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApplyState;
import com.anbang.qipai.chaguan.plan.dao.ChaguanMemberApplyDao;
import com.highto.framework.web.page.ListPage;

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

	public ChaguanMemberApply findChaguanMemberApplyByMemberIdAndChaguanIdAndState(String memberId, String chaguanId,
			String state) {
		return chaguanMemberApplyDao.findByMemberIdAndChaguanIdAndState(memberId, chaguanId, state);
	}

	public ListPage findChaguanMemberApply(String chaguanId, int page, int size) {
		int amount = (int) chaguanMemberApplyDao.countByChaguanIdAndState(chaguanId, ChaguanMemberApplyState.APPLYING);
		List<ChaguanMemberApply> applyList = chaguanMemberApplyDao.findByChaguanIdAndState(chaguanId,
				ChaguanMemberApplyState.APPLYING, page, size);
		return new ListPage(applyList, page, size, amount);
	}

	public void chaguanMemberApplyPass(String applyId) {
		chaguanMemberApplyDao.updateState(applyId, ChaguanMemberApplyState.SUCCESS);
	}

	public void chaguanMemberApplyRefuse(String applyId) {
		chaguanMemberApplyDao.updateState(applyId, ChaguanMemberApplyState.FAIL);
	}
}
