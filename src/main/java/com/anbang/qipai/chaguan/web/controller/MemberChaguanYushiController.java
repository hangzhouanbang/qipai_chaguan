package com.anbang.qipai.chaguan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.AgentNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.MemberChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberChaguanYushiService;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberDboService;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

@RestController
@RequestMapping("/memberchaguanyushi")
public class MemberChaguanYushiController {

	@Autowired
	private AgentChaguanYushiCmdService agentChaguanYushiCmdService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

	@Autowired
	private MemberChaguanYushiCmdService memberChaguanYushiCmdService;

	@Autowired
	private MemberChaguanYushiService memberChaguanYushiService;

	@Autowired
	private AgentAuthService agentAuthService;

	@Autowired
	private MemberDboService memberDboService;

	@RequestMapping("/recharge_confirm")
	public CommonVO rechargeConfirm(String token, String memberId, int amount) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberDbo memberDbo = memberDboService.findMemberDboById(memberId);
		if (memberDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("memebr not found");
			return vo;
		}
		ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(agentId);
		if (account == null || account.getBalance() < amount) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		}
		return vo;
	}

	@RequestMapping("/recharge_member_chaguanyushi")
	public CommonVO rechargeMemberChaguanYushi(String token, String memberId, int amount) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberDbo memberDbo = memberDboService.findMemberDboById(memberId);
		if (memberDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("memebr not found");
			return vo;
		}
		long accoutingTime = System.currentTimeMillis();
		try {
			AccountingRecord agentAr = agentChaguanYushiCmdService.withdraw(agentId, amount, "recharge member",
					accoutingTime);
			ChaguanYushiRecordDbo agentRecord = chaguanYushiService.withdraw(agentAr, agentId);
			// TODO Kafka发消息 推广员充值记录
			AccountingRecord memberAr = memberChaguanYushiCmdService.giveYushiToMemberByAgent(memberId, agentId, amount,
					"agent recharge", accoutingTime);
			MemberChaguanYushiRecordDbo memberRecord = memberChaguanYushiService.withdraw(memberAr, memberId, agentId);
			// TODO Kafka发消息 玩家充值记录
		} catch (AgentNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("AgentNotFoundException");
			return vo;
		} catch (InsufficientBalanceException e) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		}
		return vo;
	}
}
