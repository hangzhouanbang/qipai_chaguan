package com.anbang.qipai.chaguan.cqrs.c.domain.yushi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.anbang.qipai.chaguan.cqrs.c.domain.member.MemberNotFoundException;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.AccountingSummary;
import com.dml.accounting.InsufficientBalanceException;

/**
 * 玩家茶馆玉石账户管理
 * 
 * @author lsc
 *
 */
public class MemberChaguanYushiAccountManager {

	private Map<String, ChaguanYushiAccount> idAccountMap = new HashMap<>();

	private Map<String, Set<String>> memberIdAccountIdsMap = new HashMap<>();

	/**
	 * 为玩家创建茶馆玉石账户
	 */
	public CreateMemberChaguanYushiAccountResult createYushiAccountForNewMember(String memberId, String agentId)
			throws MemberHasYushiAccountAlreadyException {
		if (idAccountMap.containsKey(memberId + "_" + agentId + "_chaguan_yushi_wallet")) {
			throw new MemberHasYushiAccountAlreadyException();
		}
		MemberChaguanYushiAccountOwner mao = new MemberChaguanYushiAccountOwner();
		mao.setMemberId(memberId);
		mao.setAgentId(agentId);

		ChaguanYushiAccount account = new ChaguanYushiAccount();
		account.setId(memberId + "_" + agentId + "_chaguan_yushi_wallet");
		account.setCurrency("chaguan_yushi");
		account.setOwner(mao);

		idAccountMap.put(account.getId(), account);
		Set<String> accountIds = memberIdAccountIdsMap.get(memberId);
		if (accountIds == null) {
			accountIds = new HashSet<>();
		}
		accountIds.add(account.getId());
		memberIdAccountIdsMap.put(memberId, accountIds);
		CreateMemberChaguanYushiAccountResult result = new CreateMemberChaguanYushiAccountResult(account.getId(), memberId,
				agentId);
		return result;
	}

	/**
	 * 移除玩家的一个茶馆玉石账户
	 */
	public ChaguanYushiAccount removeChaguanYushiAccountOfMemberByAgent(String memberId, String agentId)
			throws MemberNotFoundException {
		if (!memberIdAccountIdsMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		Set<String> accountIds = memberIdAccountIdsMap.get(memberId);
		if (accountIds != null && accountIds.contains(memberId + "_" + agentId + "_chaguan_yushi_wallet")) {
			accountIds.remove(memberId + "_" + agentId + "_chaguan_yushi_wallet");
		}
		return idAccountMap.remove(memberId + "_" + agentId + "_chaguan_yushi_wallet");
	}

	/**
	 * 清空玩家所有茶馆玉石账户
	 */
	public Set<String> clearChaguanYushiAccountOfMember(String memberId) throws MemberNotFoundException {
		if (!memberIdAccountIdsMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		Set<String> accountIds = memberIdAccountIdsMap.get(memberId);
		if (accountIds != null) {
			for (String agentId : accountIds) {
				idAccountMap.remove(memberId + "_" + agentId + "_chaguan_yushi_wallet");
			}
			memberIdAccountIdsMap.remove(memberId);
		}
		return accountIds;
	}

	/**
	 * 增加玩家的一个茶馆玉石账户余额
	 */
	public AccountingRecord giveYushiToMemberByAgent(String memberId, String agentId, int giveYushiAmount,
			AccountingSummary accountingSummary, long giveTime) throws MemberNotFoundException {
		if (!memberIdAccountIdsMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		ChaguanYushiAccount account = idAccountMap.get(memberId + "_" + agentId + "_chaguan_yushi_wallet");
		AccountingRecord record = account.deposit(giveYushiAmount, accountingSummary, giveTime);
		return record;
	}

	/**
	 * 减少玩家的一个茶馆玉石账户余额
	 */
	public AccountingRecord withdraw(String memberId, String agentId, int amount, AccountingSummary accountingSummary,
			long withdrawTime) throws MemberNotFoundException, InsufficientBalanceException {
		if (!memberIdAccountIdsMap.containsKey(memberId)) {
			throw new MemberNotFoundException();
		}
		ChaguanYushiAccount account = idAccountMap.get(memberId + "_" + agentId + "_chaguan_yushi_wallet");
		AccountingRecord record = account.withdraw(amount, accountingSummary, withdrawTime);
		return record;
	}
}
