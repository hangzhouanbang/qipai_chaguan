package com.anbang.qipai.chaguan.cqrs.c.service.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.ChaguanYushiAccount;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.CreateChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.MemberChaguanYushiAccountManager;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.MemberHasYushiAccountAlreadyException;
import com.anbang.qipai.chaguan.cqrs.c.service.MemberChaguanYushiCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.dml.accounting.TextAccountingSummary;

@Component
public class MemberChaguanYushiCmdServiceImpl extends CmdServiceBase implements MemberChaguanYushiCmdService {

	@Override
	public CreateChaguanYushiAccountResult createYushiAccountForNewMember(String memberId, String agentId)
			throws MemberHasYushiAccountAlreadyException {
		MemberChaguanYushiAccountManager memberChaguanYushiAccountManager = singletonEntityRepository
				.getEntity(MemberChaguanYushiAccountManager.class);
		CreateChaguanYushiAccountResult result = memberChaguanYushiAccountManager
				.createYushiAccountForNewMember(memberId, agentId);
		return result;
	}

	@Override
	public AccountingRecord giveYushiToMemberByAgent(String memberId, String agentId, Integer amount,
			String textSummary, Long currentTime) throws MemberNotFoundException {
		MemberChaguanYushiAccountManager memberChaguanYushiAccountManager = singletonEntityRepository
				.getEntity(MemberChaguanYushiAccountManager.class);
		AccountingRecord ar = memberChaguanYushiAccountManager.giveYushiToMemberByAgent(memberId, agentId, amount,
				new TextAccountingSummary(textSummary), currentTime);
		return ar;
	}

	@Override
	public AccountingRecord withdraw(String memberId, String agentId, Integer amount, String textSummary,
			Long currentTime) throws InsufficientBalanceException, MemberNotFoundException {
		MemberChaguanYushiAccountManager memberChaguanYushiAccountManager = singletonEntityRepository
				.getEntity(MemberChaguanYushiAccountManager.class);
		AccountingRecord ar = memberChaguanYushiAccountManager.withdraw(memberId, agentId, amount,
				new TextAccountingSummary(textSummary), currentTime);
		return ar;
	}

	@Override
	public ChaguanYushiAccount removeChaguanYushiAccountOfMemberByAgent(String memberId, String agentId)
			throws MemberNotFoundException {
		MemberChaguanYushiAccountManager memberChaguanYushiAccountManager = singletonEntityRepository
				.getEntity(MemberChaguanYushiAccountManager.class);
		ChaguanYushiAccount account = memberChaguanYushiAccountManager
				.removeChaguanYushiAccountOfMemberByAgent(memberId, agentId);
		return account;
	}

	@Override
	public Set<String> clearChaguanYushiAccountOfMember(String memberId) throws MemberNotFoundException {
		MemberChaguanYushiAccountManager memberChaguanYushiAccountManager = singletonEntityRepository
				.getEntity(MemberChaguanYushiAccountManager.class);
		Set<String> accountIds = memberChaguanYushiAccountManager.clearChaguanYushiAccountOfMember(memberId);
		return accountIds;
	}

}
