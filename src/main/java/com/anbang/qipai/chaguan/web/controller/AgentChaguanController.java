package com.anbang.qipai.chaguan.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.ChaguanHasYushiAccountAlreadyException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.CreateChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.ChaguanCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanStatus;
import com.anbang.qipai.chaguan.cqrs.q.service.AgentDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanMemberDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.plan.bean.ChaguanApply;
import com.anbang.qipai.chaguan.plan.bean.ChaguanApplyStatus;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.plan.service.ChaguanApplyService;
import com.anbang.qipai.chaguan.web.vo.ChaguanVO;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.highto.framework.web.page.ListPage;

@RestController
@RequestMapping("/agentchaguan")
public class AgentChaguanController {

	@Autowired
	private ChaguanApplyService chaguanApplyService;

	@Autowired
	private AgentAuthService agentAuthService;

	@Autowired
	private AgentDboService agentDboService;

	@Autowired
	private ChaguanDboService chaguanDboService;

	@Autowired
	private ChaguanMemberDboService chaguanMemberDboService;

	@Autowired
	private ChaguanCmdService chaguanCmdService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

	@Autowired
	private AgentChaguanYushiCmdService agentChaguanYushiCmdService;

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
		ChaguanApply apply = chaguanApplyService.fingChaguanApplyByApplyId(applyId);
		// 创建茶馆玉石账户
		try {
			CreateChaguanYushiAccountResult result = agentChaguanYushiCmdService
					.createNewAccountForAgent(apply.getAgentId());
			chaguanYushiService.createYushiAccountForAgent(result);
		} catch (ChaguanHasYushiAccountAlreadyException e) {
			vo.setSuccess(false);
			vo.setMsg("ChaguanHasYushiAccountAlreadyException");
			return vo;
		}
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

	/**
	 * 查询推广员茶馆
	 */
	@RequestMapping("/chaguan_query")
	public CommonVO queryChaguan(String token, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		ListPage listPage = chaguanDboService.findChaguanDboByAgentId(page, size, agentId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 创建茶馆
	 */
	@RequestMapping("/chaguan_create")
	public CommonVO chaguan_create(String token, String name, String desc) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		AgentDbo agent = agentDboService.findAgentDboByAgentId(agentId);
		String newChaguanId = chaguanCmdService.createNewChaguanId(System.currentTimeMillis());
		ChaguanDbo dbo = new ChaguanDbo();
		dbo.setId(newChaguanId);
		dbo.setAgentId(agentId);
		dbo.setNickname(agent.getNickname());
		dbo.setHeadimgurl(agent.getHeadimgurl());
		dbo.setName(name);
		dbo.setDesc(desc);
		dbo.setMemberNum(0);
		dbo.setStatus(ChaguanStatus.NORMAL);
		chaguanDboService.addChaguanDbo(dbo);
		return vo;
	}

	/**
	 * 查询茶馆信息
	 */
	@RequestMapping("/chaguan_info")
	public CommonVO queryChaguanInfo(String token, String chaguanId) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		ChaguanDbo chaguanDbo = chaguanDboService.findChaguanDboById(chaguanId);
		if (chaguanDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("chaguan not found");
			return vo;
		}
		int onlineAmount = (int) chaguanMemberDboService.countOnlineMemberByChaguanId(chaguanId);
		ChaguanVO chaguan = new ChaguanVO();
		chaguan.setId(chaguanDbo.getId());
		chaguan.setName(chaguanDbo.getName());
		chaguan.setDesc(chaguanDbo.getDesc());
		chaguan.setOnlineAmount(onlineAmount);
		chaguan.setMemberNum(chaguanDbo.getMemberNum());
		vo.setMsg("chaguan info");
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("chaguan", chaguan);
		return vo;
	}

	/**
	 * 查询茶馆成员信息
	 */
	@RequestMapping("/chaguan_member_info")
	public CommonVO queryMemberChaguanInfo(String token, String chaguanId, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		ListPage listPage = chaguanMemberDboService.findChaguanMemberDboByChaguanId(page, size, chaguanId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 修改茶馆基本信息
	 */
	@RequestMapping("/chaguan_update")
	public CommonVO chaguan_update(String token, String chaguanId, String name, String desc) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		chaguanDboService.updateChaguanBaseInfo(chaguanId, name, desc);
		return vo;
	}
}
