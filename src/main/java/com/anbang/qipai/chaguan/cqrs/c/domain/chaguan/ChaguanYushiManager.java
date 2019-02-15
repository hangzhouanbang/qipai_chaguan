package com.anbang.qipai.chaguan.cqrs.c.domain.chaguan;

import java.util.HashMap;
import java.util.Map;

import com.dml.accounting.Account;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.AccountingSubject;
import com.dml.accounting.AccountingSummary;
import com.dml.accounting.InsufficientBalanceException;

/**
 * 茶馆玉石账户管理
 * 
 * @author lsc
 *
 */
public class ChaguanYushiManager {

	private Map<String, Account> accountIdAccountMap = new HashMap<>();

	private Map<String, String> agentIdAccountIdMap = new HashMap<>();

	public CreateChaguanYushiAccountResult createNewAccountForAgent(String agentId)
			throws ChaguanHasYushiAccountAlreadyException {
		if (agentIdAccountIdMap.containsKey(agentId)) {
			throw new ChaguanHasYushiAccountAlreadyException();
		}
		AgentChaguanAccountOwner aao = new AgentChaguanAccountOwner();
		aao.setAgentId(agentId);

		AccountingSubject subject = new AccountingSubject();
		subject.setName("wallet");

		Account account = new Account();
		account.setId(agentId + "_chaguanyushi_wallet");
		account.setCurrency("chaguanyushi");
		account.setOwner(aao);
		account.setSubject(subject);

		accountIdAccountMap.put(account.getId(), account);
		agentIdAccountIdMap.put(agentId, account.getId());
		return new CreateChaguanYushiAccountResult(account.getId(), agentId);
	}

	public AccountingRecord giveChaguanyushiToAgent(String agentId, int giveChagunayushiAmount,
			AccountingSummary accountingSummary, long giveTime) throws AgentNotFoundException {
		if (!agentIdAccountIdMap.containsKey(agentId)) {
			throw new AgentNotFoundException();
		}
		Account account = accountIdAccountMap.get(agentIdAccountIdMap.get(agentId));
		AccountingRecord record = account.deposit(giveChagunayushiAmount, accountingSummary, giveTime);
		return record;
	}

	public AccountingRecord withdraw(String agentId, int amount, AccountingSummary accountingSummary, long withdrawTime)
			throws AgentNotFoundException, InsufficientBalanceException {
		if (!agentIdAccountIdMap.containsKey(agentId)) {
			throw new AgentNotFoundException();
		}
		Account account = accountIdAccountMap.get(agentIdAccountIdMap.get(agentId));
		AccountingRecord record = account.withdraw(amount, accountingSummary, withdrawTime);
		return record;
	}

	public Map<String, Account> getAccountIdAccountMap() {
		return accountIdAccountMap;
	}

	public void setAccountIdAccountMap(Map<String, Account> accountIdAccountMap) {
		this.accountIdAccountMap = accountIdAccountMap;
	}

	public Map<String, String> getAgentIdAccountIdMap() {
		return agentIdAccountIdMap;
	}

	public void setAgentIdAccountIdMap(Map<String, String> agentIdAccountIdMap) {
		this.agentIdAccountIdMap = agentIdAccountIdMap;
	}

}
