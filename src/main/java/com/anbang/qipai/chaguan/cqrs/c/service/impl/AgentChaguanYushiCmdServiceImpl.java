package com.anbang.qipai.chaguan.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.AgentNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.ChaguanHasYushiAccountAlreadyException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.ChaguanYushiManager;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.CreateChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.dml.accounting.TextAccountingSummary;

@Component
public class AgentChaguanYushiCmdServiceImpl extends CmdServiceBase implements AgentChaguanYushiCmdService {

	@Override
	public CreateChaguanYushiAccountResult createNewAccountForAgent(String agentId)
			throws ChaguanHasYushiAccountAlreadyException {
		ChaguanYushiManager chaguanYushiManager = singletonEntityRepository.getEntity(ChaguanYushiManager.class);
		return chaguanYushiManager.createNewAccountForAgent(agentId);
	}

	@Override
	public AccountingRecord giveChaguanyushiToAgent(String agentId, Integer giveChagunayushiAmount, String textSummary,
			Long giveTime) throws AgentNotFoundException {
		ChaguanYushiManager chaguanYushiManager = singletonEntityRepository.getEntity(ChaguanYushiManager.class);
		return chaguanYushiManager.giveChaguanyushiToAgent(agentId, giveChagunayushiAmount,
				new TextAccountingSummary(textSummary), giveTime);
	}

	@Override
	public AccountingRecord withdraw(String agentId, Integer amount, String textSummary, Long withdrawTime)
			throws AgentNotFoundException, InsufficientBalanceException {
		ChaguanYushiManager chaguanYushiManager = singletonEntityRepository.getEntity(ChaguanYushiManager.class);
		return chaguanYushiManager.withdraw(agentId, amount, new TextAccountingSummary(textSummary), withdrawTime);
	}

}
