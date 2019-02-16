package com.anbang.qipai.chaguan.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.AgentNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

@RestController
@RequestMapping("/chaguanyushi")
public class ChaguanYushiController {

	@Autowired
	private AgentChaguanYushiCmdService agentChaguanYushiCmdService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

	@Autowired
	private AgentAuthService agentAuthService;

	@RequestMapping(value = "/query_chaguanyushiaccount")
	public CommonVO queryAgentChaguanYushiAccount(String token) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(agentId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("balance", 0);
		if (account != null) {
			data.put("balance", account.getBalance());
		}
		return vo;
	}

	@RequestMapping(value = "/withdraw")
	public CommonVO withdraw(String agentId, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord rcd = agentChaguanYushiCmdService.withdraw(agentId, amount, textSummary,
					System.currentTimeMillis());
			ChaguanYushiRecordDbo dbo = chaguanYushiService.withdraw(rcd, agentId);
			return vo;
		} catch (InsufficientBalanceException e) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		} catch (AgentNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("AgentNotFoundException");
			return vo;
		}
	}

	@RequestMapping(value = "/givechaguanyushitoagent")
	public CommonVO giveGoldToMember(String agentId, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord rcd = agentChaguanYushiCmdService.giveChaguanyushiToAgent(agentId, amount, textSummary,
					System.currentTimeMillis());
			ChaguanYushiRecordDbo dbo = chaguanYushiService.withdraw(rcd, agentId);
			return vo;
		} catch (AgentNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("AgentNotFoundException");
			return vo;
		}
	}
}
