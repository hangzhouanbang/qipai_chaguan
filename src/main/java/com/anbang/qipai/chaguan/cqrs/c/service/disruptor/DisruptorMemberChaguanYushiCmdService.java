package com.anbang.qipai.chaguan.cqrs.c.service.disruptor;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.ChaguanYushiAccount;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.CreateMemberChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.MemberHasYushiAccountAlreadyException;
import com.anbang.qipai.chaguan.cqrs.c.service.MemberChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.impl.MemberChaguanYushiCmdServiceImpl;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "memberChaguanYushiCmdService")
public class DisruptorMemberChaguanYushiCmdService extends DisruptorCmdServiceBase
		implements MemberChaguanYushiCmdService {

	@Autowired
	private MemberChaguanYushiCmdServiceImpl memberChaguanYushiCmdServiceImpl;

	@Override
	public CreateMemberChaguanYushiAccountResult createYushiAccountForNewMember(String memberId, String agentId)
			throws MemberHasYushiAccountAlreadyException {
		CommonCommand cmd = new CommonCommand(MemberChaguanYushiCmdServiceImpl.class.getName(),
				"createYushiAccountForNewMember", memberId, agentId);
		DeferredResult<CreateMemberChaguanYushiAccountResult> result = publishEvent(
				disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
					CreateMemberChaguanYushiAccountResult createResult = memberChaguanYushiCmdServiceImpl
							.createYushiAccountForNewMember(cmd.getParameter(), cmd.getParameter());
					return createResult;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof MemberHasYushiAccountAlreadyException) {
				throw (MemberHasYushiAccountAlreadyException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public AccountingRecord giveYushiToMemberByAgent(String memberId, String agentId, Integer amount,
			String textSummary, Long currentTime) throws MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberChaguanYushiCmdServiceImpl.class.getName(),
				"giveYushiToMemberByAgent", memberId, agentId, amount, textSummary, currentTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord accountingRecord = memberChaguanYushiCmdServiceImpl.giveYushiToMemberByAgent(
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return accountingRecord;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof MemberNotFoundException) {
				throw (MemberNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public AccountingRecord withdraw(String memberId, String agentId, Integer amount, String textSummary,
			Long currentTime) throws InsufficientBalanceException, MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberChaguanYushiCmdServiceImpl.class.getName(), "withdraw", memberId,
				agentId, amount, textSummary, currentTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord accountingRecord = memberChaguanYushiCmdServiceImpl.withdraw(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return accountingRecord;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof InsufficientBalanceException) {
				throw (InsufficientBalanceException) e;
			} else if (e instanceof MemberNotFoundException) {
				throw (MemberNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public ChaguanYushiAccount removeChaguanYushiAccountOfMemberByAgent(String memberId, String agentId)
			throws MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberChaguanYushiCmdServiceImpl.class.getName(),
				"removeChaguanYushiAccountOfMemberByAgent", memberId, agentId);
		DeferredResult<ChaguanYushiAccount> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			ChaguanYushiAccount account = memberChaguanYushiCmdServiceImpl
					.removeChaguanYushiAccountOfMemberByAgent(cmd.getParameter(), cmd.getParameter());
			return account;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof MemberNotFoundException) {
				throw (MemberNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Set<String> clearChaguanYushiAccountOfMember(String memberId) throws MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberChaguanYushiCmdServiceImpl.class.getName(),
				"clearChaguanYushiAccountOfMember", memberId);
		DeferredResult<Set<String>> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			Set<String> accountIds = memberChaguanYushiCmdServiceImpl
					.clearChaguanYushiAccountOfMember(cmd.getParameter());
			return accountIds;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof MemberNotFoundException) {
				throw (MemberNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public AccountingRecord withdrawAnyway(String memberId, String agentId, Integer amount, String textSummary,
			Long currentTime) throws MemberNotFoundException {
		CommonCommand cmd = new CommonCommand(MemberChaguanYushiCmdServiceImpl.class.getName(), "withdrawAnyway",
				memberId, agentId, amount, textSummary, currentTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord accountingRecord = memberChaguanYushiCmdServiceImpl.withdrawAnyway(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return accountingRecord;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof MemberNotFoundException) {
				throw (MemberNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}
