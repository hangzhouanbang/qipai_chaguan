package com.anbang.qipai.chaguan.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.CreateChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanYushiAccountDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dao.ChaguanYushiRecordDboDao;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.dml.accounting.AccountingRecord;

@Service
public class ChaguanYushiService {

	@Autowired
	private ChaguanYushiAccountDboDao chaguanYushiAccountDboDao;

	@Autowired
	private ChaguanYushiRecordDboDao chaguanYushiRecordDboDao;

	/**
	 * 创建茶馆玉石账户
	 */
	public ChaguanYushiAccountDbo createYushiAccountForAgent(CreateChaguanYushiAccountResult result) {
		ChaguanYushiAccountDbo account = new ChaguanYushiAccountDbo();
		account.setId(result.getAccountId());
		account.setAgentId(result.getAgentId());
		chaguanYushiAccountDboDao.insert(account);
		return account;
	}

	public ChaguanYushiAccountDbo findChaguanYushiAccountDboByAgentId(String agentId) {
		return chaguanYushiAccountDboDao.findByAgentId(agentId);
	}

	/**
	 * 记录流水并更新账户
	 */
	public ChaguanYushiRecordDbo withdraw(AccountingRecord record, String agentId) {
		ChaguanYushiRecordDbo dbo = new ChaguanYushiRecordDbo();
		dbo.setAccountId(record.getAccountId());
		dbo.setAccountingAmount((int) record.getAccountingAmount());
		dbo.setAccountingNo(record.getAccountingNo());
		dbo.setAccountingTime(record.getAccountingTime());
		dbo.setAgentId(agentId);
		dbo.setBalanceAfter((int) record.getBalanceAfter());
		dbo.setSummary(record.getSummary());
		chaguanYushiRecordDboDao.insert(dbo);

		chaguanYushiAccountDboDao.updateBalance(record.getAccountId(), (int) record.getBalanceAfter());
		return dbo;
	}
}
