package com.anbang.qipai.chaguan.cqrs.c.service;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.AgentNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.ChaguanHasYushiAccountAlreadyException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.CreateChaguanYushiAccountResult;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

public interface AgentChaguanYushiCmdService {

	CreateChaguanYushiAccountResult createNewAccountForAgent(String agentId)
			throws ChaguanHasYushiAccountAlreadyException;

	AccountingRecord giveChaguanyushiToAgent(String agentId, Integer giveChagunayushiAmount, String textSummary,
			Long giveTime) throws AgentNotFoundException;

	AccountingRecord withdraw(String agentId, Integer amount, String textSummary, Long withdrawTime)
			throws AgentNotFoundException, InsufficientBalanceException;
}
