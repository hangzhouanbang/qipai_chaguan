package com.anbang.qipai.chaguan.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.CreateMemberChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.q.dao.MemberChaguanYushiAccountDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dao.MemberChaguanYushiRecordDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;
import com.dml.accounting.AccountingRecord;

@Service
public class MemberChaguanYushiService {

	@Autowired
	private MemberChaguanYushiAccountDboDao memberChaguanYushiAccountDboDao;

	@Autowired
	private MemberChaguanYushiRecordDboDao memberChaguanYushiRecordDboDao;

	/**
	 * 创建茶馆玉石账户
	 */
	public MemberChaguanYushiAccountDbo createYushiAccountForNewMember(CreateMemberChaguanYushiAccountResult result) {
		MemberChaguanYushiAccountDbo account = new MemberChaguanYushiAccountDbo();
		account.setId(result.getAccountId());
		account.setMemberId(result.getMemberId());
		account.setAgentId(result.getAgentId());
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

		memberChaguanYushiAccountDboDao.updateBalance(record.getAccountId(), (int) record.getBalanceAfter());
		return dbo;
	}
}
