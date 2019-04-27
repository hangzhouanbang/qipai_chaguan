package com.anbang.qipai.chaguan.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.AgentNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.msg.service.ChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.web.page.ListPage;

/**
 * 茶馆玉石管理
 * 
 * @author lsc
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/chaguanyushi")
public class ChaguanYushiController {

	@Autowired
	private AgentChaguanYushiCmdService agentChaguanYushiCmdService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

	@Autowired
	private AgentAuthService agentAuthService;

	@Autowired
	private ChaguanYushiRecordMsgService chaguanYushiRecordMsgService;

	/**
	 * 推广员查询茶馆玉石
	 */
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

	/**
	 * 推广员查询充值记录
	 */
	@RequestMapping(value = "/query_record")
	public CommonVO queryRecord(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
			String token) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = chaguanYushiService.findChaguanYushiRecordDbo(page, size, "玩家");
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 消耗推广员茶馆玉石
	 */
	@RequestMapping(value = "/withdraw")
	public CommonVO withdraw(String agentId, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord rcd = agentChaguanYushiCmdService.withdraw(agentId, amount, textSummary,
					System.currentTimeMillis());
			ChaguanYushiRecordDbo dbo = chaguanYushiService.withdraw(rcd, agentId);
			chaguanYushiRecordMsgService.recordChaguanYushiRecordDbo(dbo);
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

	/**
	 * 赠送推广员玉石
	 */
	@RequestMapping(value = "/givechaguanyushitoagent")
	public CommonVO giveGoldToMember(String agentId, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord rcd = agentChaguanYushiCmdService.giveChaguanyushiToAgent(agentId, amount, textSummary,
					System.currentTimeMillis());
			ChaguanYushiRecordDbo dbo = chaguanYushiService.withdraw(rcd, agentId);
			chaguanYushiRecordMsgService.recordChaguanYushiRecordDbo(dbo);
			return vo;
		} catch (AgentNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("AgentNotFoundException");
			return vo;
		}
	}
}
