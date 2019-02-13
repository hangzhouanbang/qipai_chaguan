package com.anbang.qipai.chaguan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.conf.ChaguanApplyStatus;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.AgentDboService;
import com.anbang.qipai.chaguan.plan.bean.ChaguanApply;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.plan.service.ChaguanApplyService;
import com.anbang.qipai.chaguan.web.vo.CommonVO;

@RestController
@RequestMapping("/agentchaguan")
public class AgentChaguanController {

	@Autowired
	private ChaguanApplyService chaguanApplyService;

	@Autowired
	private AgentAuthService agentAuthService;

	@Autowired
	private AgentDboService agentDboService;

	/**
	 * 推广员申请开通茶馆
	 */
	@RequestMapping("/apply")
	public CommonVO agentApplyChaguan(String token) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		AgentDbo agent = agentDboService.findAgentDboByAgentId(agentId);
		ChaguanApply apply = new ChaguanApply();
		apply.setAgentId(agent.getId());
		apply.setNickname(agent.getNickname());
		apply.setHeadimgurl(agent.getHeadimgurl());
		apply.setInviteMemberNum(agent.getInviteMemberNum());
		apply.setAgentType(agent.getAgentType());
		apply.setCreateTime(System.currentTimeMillis());
		apply.setStatus(ChaguanApplyStatus.APPLYING);
		chaguanApplyService.addApply(apply);
		return vo;
	}

	/**
	 * 通过茶馆申请
	 */
	@RequestMapping("/apply_pass")
	public CommonVO applychaguan_pass(String applyId) {
		CommonVO vo = new CommonVO();
		chaguanApplyService.updateApplyStatus(applyId, ChaguanApplyStatus.SUCCESS);
		return vo;
	}

	/**
	 * 拒绝茶馆申请
	 */
	@RequestMapping("/apply_refuse")
	public CommonVO applychaguan_refuse(String applyId) {
		CommonVO vo = new CommonVO();
		chaguanApplyService.updateApplyStatus(applyId, ChaguanApplyStatus.FAIL);
		return vo;
	}
}
