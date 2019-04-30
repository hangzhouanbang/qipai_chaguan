package com.anbang.qipai.chaguan.cqrs.q.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.CreateMemberChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanMemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dao.MemberChaguanYushiAccountDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dao.MemberChaguanYushiRecordDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.chaguan.web.vo.FreeReportVO;
import com.dml.accounting.AccountingRecord;
import com.highto.framework.web.page.ListPage;

@Service
public class MemberChaguanYushiService {

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private MemberChaguanYushiAccountDboDao memberChaguanYushiAccountDboDao;

	@Autowired
	private MemberChaguanYushiRecordDboDao memberChaguanYushiRecordDboDao;

	@Autowired
	private ChaguanMemberDboDao chaguanMemberDboDao;

	/**
	 * 创建茶馆玉石账户
	 */
	public MemberChaguanYushiAccountDbo createYushiAccountForNewMember(CreateMemberChaguanYushiAccountResult result,
			String chaguanId) {
		MemberChaguanYushiAccountDbo account = new MemberChaguanYushiAccountDbo();
		account.setId(result.getAccountId());
		account.setMemberId(result.getMemberId());
		account.setAgentId(result.getAgentId());
		account.setChaguanId(chaguanId);
		memberChaguanYushiAccountDboDao.insert(account);
		return account;
	}

	/**
	 * 记录流水并更新账户
	 */
	public MemberChaguanYushiRecordDbo withdraw(AccountingRecord record, String memberId, String agentId) {
		MemberChaguanYushiRecordDbo dbo = new MemberChaguanYushiRecordDbo();
		dbo.setAccountId(record.getAccountId());
		dbo.setAccountingAmount((int) record.getAccountingAmount());
		dbo.setAccountingNo(record.getAccountingNo());
		dbo.setAccountingTime(record.getAccountingTime());
		dbo.setAgentId(agentId);
		dbo.setBalanceAfter((int) record.getBalanceAfter());
		dbo.setMemberId(memberId);
		dbo.setSummary(record.getSummary());
		memberChaguanYushiRecordDboDao.insert(dbo);
		chaguanMemberDboDao.updateChaguanMemberDboYushiByMemberIdAndAgentId(memberId, agentId,
				(int) record.getBalanceAfter());
		memberChaguanYushiAccountDboDao.updateBalance(record.getAccountId(), (int) record.getBalanceAfter());
		return dbo;
	}

	public ListPage findFreeReportVOByMemberId(String agentId, String chaguanId, int page, int size) {
		List<FreeReportVO> reportList = new ArrayList<>();
		int amount = (int) memberChaguanYushiAccountDboDao.countByAgentIdAndChaguanIdAndBalanceLessThan(agentId,
				chaguanId, 0);
		List<MemberChaguanYushiAccountDbo> accountList = memberChaguanYushiAccountDboDao
				.findByAgentIdAndChaguanIdAndBalanceLessThan(agentId, chaguanId, 0, page, size);
		for (MemberChaguanYushiAccountDbo account : accountList) {
			FreeReportVO vo = new FreeReportVO();
			vo.setAccountId(account.getId());
			vo.setMemberId(account.getMemberId());
			MemberDbo member = memberDboDao.findById(account.getMemberId());
			vo.setNickname(member.getNickname());
			vo.setHeadimgurl(member.getHeadimgurl());
			vo.setCost(account.getBalance());
			vo.setFreeCount((int) memberChaguanYushiRecordDboDao
					.countByMemberIdAndLastestBalanceAfterLessThan(account.getMemberId(), 0));
			reportList.add(vo);
		}
		return new ListPage(reportList, page, size, amount);
	}

	public MemberChaguanYushiAccountDbo findMemberChaguanYushiAccountDboByAccountId(String accountId) {
		return memberChaguanYushiAccountDboDao.findByAccountId(accountId);
	}

	public MemberChaguanYushiAccountDbo findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(String chaguanId,
			String memberId) {
		return memberChaguanYushiAccountDboDao.findByChaguanIdAndMemberId(chaguanId, memberId);
	}

	public MemberChaguanYushiAccountDbo findMemberChaguanYushiAccountDboByAgentIdAndMemebrId(String agentId,
			String memberId) {
		return memberChaguanYushiAccountDboDao.findByAgentIdAndMemberId(agentId, memberId);
	}
}
