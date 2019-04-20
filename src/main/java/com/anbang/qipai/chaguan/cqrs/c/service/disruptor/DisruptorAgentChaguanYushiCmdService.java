package com.anbang.qipai.chaguan.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.AgentNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.ChaguanHasYushiAccountAlreadyException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.CreateChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.impl.AgentChaguanYushiCmdServiceImpl;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "agentChaguanYushiCmdService")
public class DisruptorAgentChaguanYushiCmdService extends DisruptorCmdServiceBase
		implements AgentChaguanYushiCmdService {

	@Autowired
	private AgentChaguanYushiCmdServiceImpl agentChaguanYushiCmdServiceImpl;

	@Override
	public CreateChaguanYushiAccountResult createNewAccountForAgent(String agentId)
			throws ChaguanHasYushiAccountAlreadyException {
		CommonCommand cmd = new CommonCommand(AgentChaguanYushiCmdServiceImpl.class.getName(),
				"createNewAccountForAgent", agentId);
		DeferredResult<CreateChaguanYushiAccountResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(),
				cmd, () -> {
					CreateChaguanYushiAccountResult createResult = agentChaguanYushiCmdServiceImpl
							.createNewAccountForAgent(cmd.getParameter());
					return createResult;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof ChaguanHasYushiAccountAlreadyException) {
				throw (ChaguanHasYushiAccountAlreadyException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public AccountingRecord giveChaguanyushiToAgent(String agentId, Integer giveChagunayushiAmount, String textSummary,
			Long giveTime) throws AgentNotFoundException {
		CommonCommand cmd = new CommonCommand(AgentChaguanYushiCmdServiceImpl.class.getName(),
				"giveChaguanyushiToAgent", agentId, giveChagunayushiAmount, textSummary, giveTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord ar = agentChaguanYushiCmdServiceImpl.giveChaguanyushiToAgent(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return ar;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof AgentNotFoundException) {
				throw (AgentNotFoundException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public AccountingRecord withdraw(String agentId, Integer amount, String textSummary, Long withdrawTime)
			throws AgentNotFoundException, InsufficientBalanceException {
		CommonCommand cmd = new CommonCommand(AgentChaguanYushiCmdServiceImpl.class.getName(), "withdraw", agentId,
				amount, textSummary, withdrawTime);
		DeferredResult<AccountingRecord> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			AccountingRecord ar = agentChaguanYushiCmdServiceImpl.withdraw(cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter());
			return ar;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof AgentNotFoundException) {
				throw (AgentNotFoundException) e;
			} else if (e instanceof InsufficientBalanceException) {
				throw (InsufficientBalanceException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}
