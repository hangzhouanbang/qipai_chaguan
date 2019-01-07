package com.anbang.qipai.chaguan.cqrs.c.service;

import java.util.Set;

import com.anbang.qipai.chaguan.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.ChaguanYushiAccount;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.CreateChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.MemberHasYushiAccountAlreadyException;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

public interface MemberChaguanYushiCmdService {

	CreateChaguanYushiAccountResult createYushiAccountForNewMember(String memberId, String agentId)
			throws MemberHasYushiAccountAlreadyException;

	ChaguanYushiAccount removeChaguanYushiAccountOfMemberByAgent(String memberId, String agentId)
			throws MemberNotFoundException;

	Set<String> clearChaguanYushiAccountOfMember(String memberId) throws MemberNotFoundException;

	AccountingRecord giveYushiToMemberByAgent(String memberId, String agentId, Integer amount, String textSummary,
			Long currentTime) throws MemberNotFoundException;

	AccountingRecord withdraw(String memberId, String agentId, Integer amount, String textSummary, Long currentTime)
			throws InsufficientBalanceException, MemberNotFoundException;
}
