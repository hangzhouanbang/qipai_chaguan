package com.anbang.qipai.chaguan.web.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.anbang.qipai.chaguan.msg.service.ChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.msg.service.MemberChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

/**
 * 玩家茶馆玉石管理
 * 
 * @author lsc
 *
 */
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

	@Autowired
	private MemberChaguanYushiRecordMsgService memberChaguanYushiRecordMsgService;

	@Autowired
	private ChaguanYushiRecordMsgService chaguanYushiRecordMsgService;

	/**
	 * 玩家充值确认
	 */
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
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("member info", memberDbo);
		return vo;
	}

	/**
	 * 给玩家充值茶馆玉石
	 */
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
			AccountingRecord agentAr = agentChaguanYushiCmdService.withdraw(agentId, amount,
					"recharge member:" + memberId, accoutingTime);
			ChaguanYushiRecordDbo agentRecord = chaguanYushiService.withdraw(agentAr, agentId);
			chaguanYushiRecordMsgService.recordChaguanYushiRecordDbo(agentRecord);
			AccountingRecord memberAr = memberChaguanYushiCmdService.giveYushiToMemberByAgent(memberId, agentId, amount,
					"agent recharge", accoutingTime);
			MemberChaguanYushiRecordDbo memberRecord = memberChaguanYushiService.withdraw(memberAr, memberId, agentId);
			memberChaguanYushiRecordMsgService.recordMemberChaguanYushiRecordDbo(memberRecord);
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
