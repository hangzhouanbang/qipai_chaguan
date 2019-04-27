package com.anbang.qipai.chaguan.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.AgentNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.ChaguanHasYushiAccountAlreadyException;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.CreateChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.c.domain.member.MemberNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.ChaguanCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.MemberChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanMemberDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanStatus;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.AgentDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanMemberDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberChaguanYushiService;
import com.anbang.qipai.chaguan.msg.service.ChaguanApplyMsgService;
import com.anbang.qipai.chaguan.msg.service.ChaguanMemberMsgService;
import com.anbang.qipai.chaguan.msg.service.ChaguanMsgService;
import com.anbang.qipai.chaguan.msg.service.ChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.msg.service.MemberChaguanYushiRecordMsgService;
import com.anbang.qipai.chaguan.plan.bean.ChaguanApply;
import com.anbang.qipai.chaguan.plan.bean.ChaguanApplyStatus;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.plan.service.ChaguanApplyService;
import com.anbang.qipai.chaguan.plan.service.ChaguanMemberApplyService;
import com.anbang.qipai.chaguan.web.vo.ChaguanVO;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.web.page.ListPage;

/**
 * 推广员茶馆管理
 * 
 * @author lsc
 *
 */
@CrossOrigin
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

	@Autowired
	private ChaguanApplyMsgService chaguanApplyMsgService;

	@Autowired
	private ChaguanMsgService chaguanMsgService;

	@Autowired
	private MemberChaguanYushiCmdService memberChaguanYushiCmdService;

	@Autowired
	private MemberChaguanYushiService memberChaguanYushiService;

	@Autowired
	private ChaguanMemberApplyService chaguanMemberApplyService;

	@Autowired
	private ChaguanMemberMsgService chaguanMemberMsgService;

	@Autowired
	private MemberChaguanYushiRecordMsgService memberChaguanYushiRecordMsgService;

	@Autowired
	private ChaguanYushiRecordMsgService chaguanYushiRecordMsgService;

	/**
	 * 推广员是否开通茶馆
	 */
	@RequestMapping("/apply_info")
	public CommonVO agentApplyInfo(String token) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		AgentDbo agent = agentDboService.findAgentDboByAgentId(agentId);
		if (!agent.isAgentAuth()) {
			vo.setSuccess(false);
			vo.setMsg("not agent");
			return vo;
		}
		if (chaguanApplyService.fingChaguanApplyByAgentIdAndStatus(agentId, ChaguanApplyStatus.SUCCESS) == null) {
			vo.setSuccess(false);
			vo.setMsg("not apply");
			return vo;
		}
		return vo;
	}

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
		if (!agent.isAgentAuth()) {
			vo.setSuccess(false);
			vo.setMsg("not agent");
			return vo;
		}
		if (chaguanApplyService.fingChaguanApplyByAgentIdAndStatus(agentId, ChaguanApplyStatus.SUCCESS) != null
				|| chaguanApplyService.fingChaguanApplyByAgentIdAndStatus(agentId,
						ChaguanApplyStatus.APPLYING) != null) {
			vo.setSuccess(false);
			vo.setMsg("apply already");
			return vo;
		}
		ChaguanApply apply = new ChaguanApply();
		apply.setAgentId(agent.getId());
		apply.setNickname(agent.getNickname());
		apply.setHeadimgurl(agent.getHeadimgurl());
		apply.setInviteMemberNum(agent.getInviteMemberNum());
		apply.setAgentType(agent.getAgentType());
		apply.setCreateTime(System.currentTimeMillis());
		apply.setStatus(ChaguanApplyStatus.APPLYING);
		chaguanApplyService.addApply(apply);
		chaguanApplyMsgService.recordChaguanApply(apply);
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
		apply = chaguanApplyService.updateApplyStatus(applyId, ChaguanApplyStatus.SUCCESS);
		chaguanApplyMsgService.chaguanApplyPass(apply);
		return vo;
	}

	/**
	 * 拒绝茶馆申请
	 */
	@RequestMapping("/apply_refuse")
	public CommonVO applychaguan_refuse(String applyId) {
		CommonVO vo = new CommonVO();
		ChaguanApply apply = chaguanApplyService.updateApplyStatus(applyId, ChaguanApplyStatus.FAIL);
		chaguanApplyMsgService.chaguanApplyPass(apply);
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
		AgentDbo agentDbo = agentDboService.findAgentDboByAgentId(agentId);
		ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(agentId);
		ListPage listPage = chaguanDboService.findChaguanDboByAgentId(page, size, agentId);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("agent", agentDbo);
		data.put("balance", 0);
		if (account != null) {
			data.put("balance", account.getBalance());
		}
		data.put("listPage", listPage);
		data.put("num", listPage.getTotalItemsCount());
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
		if (chaguanApplyService.fingChaguanApplyByAgentIdAndStatus(agentId, ChaguanApplyStatus.SUCCESS) == null) {
			vo.setSuccess(false);
			vo.setMsg("can not create");
			return vo;
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
		chaguanMsgService.createChaguan(dbo);
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
		ChaguanYushiAccountDbo account = chaguanYushiService.findChaguanYushiAccountDboByAgentId(agentId);
		ChaguanVO chaguan = new ChaguanVO();
		chaguan.setId(chaguanDbo.getId());
		chaguan.setName(chaguanDbo.getName());
		chaguan.setDesc(chaguanDbo.getDesc());
		chaguan.setOnlineAmount(onlineAmount);
		chaguan.setMemberNum(chaguanDbo.getMemberNum());
		chaguan.setBalance(account.getBalance());
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
		ChaguanDbo dbo = chaguanDboService.updateChaguanBaseInfo(chaguanId, name, desc);
		chaguanMsgService.updateChaguan(dbo);
		return vo;
	}

	/**
	 * 修改茶馆基本信息，后台rpc
	 */
	@RequestMapping("/chaguan_update_rpc")
	public CommonVO chaguan_update_rpc(String chaguanId, String name, String desc) {
		CommonVO vo = new CommonVO();
		ChaguanDbo dbo = chaguanDboService.updateChaguanBaseInfo(chaguanId, name, desc);
		chaguanMsgService.updateChaguan(dbo);
		return vo;
	}

	/**
	 * 移除茶馆成员
	 */
	@RequestMapping("/chaguan_member_remove")
	public CommonVO chaguanMemberRemove(String token, String chaguanId, String memberId) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		ChaguanMemberDbo member = chaguanMemberDboService.updateChaguanMemberDboRemoveByMemberIdAndChaguanId(memberId,
				chaguanId, true);
		chaguanMemberMsgService.removeChaguanMember(member);
		ChaguanDbo dbo = chaguanDboService.updateChaguanDboMemberNum(chaguanId);
		chaguanMsgService.updateChaguan(dbo);
		return vo;
	}

	/**
	 * 移除茶馆成员，后台rpc
	 */
	@RequestMapping("/chaguan_member_remove_rpc")
	public CommonVO chaguan_member_remove_rpc(String chaguanId, String memberId) {
		CommonVO vo = new CommonVO();
		ChaguanMemberDbo member = chaguanMemberDboService.updateChaguanMemberDboRemoveByMemberIdAndChaguanId(memberId,
				chaguanId, true);
		chaguanMemberMsgService.removeChaguanMember(member);
		ChaguanDbo dbo = chaguanDboService.updateChaguanDboMemberNum(chaguanId);
		chaguanMsgService.updateChaguan(dbo);
		return vo;
	}

	/**
	 * 设置茶馆成员
	 */
	@RequestMapping("/chaguan_member_set")
	public CommonVO chaguanMemberSet(String token, String chaguanId, String memberId, String payType,
			String memberDesc) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		ChaguanMemberDbo member = chaguanMemberDboService.chaguanMemberSet(memberId, chaguanId, payType, memberDesc);
		chaguanMemberMsgService.setChaguanMember(member);
		return vo;
	}

	/**
	 * 查询茶馆申请
	 */
	@RequestMapping("/chaguan_member_apply")
	public CommonVO chaguanMemberApply(String token, String chaguanId, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		ListPage listPage = chaguanMemberApplyService.findChaguanMemberApply(chaguanId, page, size);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 查询茶馆免费报表
	 */
	@RequestMapping("/freereport")
	public CommonVO freereport(String token, String chaguanId, int page, int size) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = memberChaguanYushiService.findFreeReportVOByMemberId(agentId, chaguanId, page, size);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 清零玩家免费报表
	 */
	@RequestMapping("/freereport_clear")
	public CommonVO freereport_clear(String token, String accountId) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberChaguanYushiAccountDbo memberChaguanYushiAccountDbo = memberChaguanYushiService
				.findMemberChaguanYushiAccountDboByAccountId(accountId);
		try {
			AccountingRecord agentRecord = agentChaguanYushiCmdService.withdraw(agentId,
					memberChaguanYushiAccountDbo.getBalance(),
					"agent clear:" + memberChaguanYushiAccountDbo.getMemberId(), System.currentTimeMillis());
			ChaguanYushiRecordDbo cyrd = chaguanYushiService.withdraw(agentRecord, agentId);
			chaguanYushiRecordMsgService.recordChaguanYushiRecordDbo(cyrd);
			AccountingRecord memberRecord = memberChaguanYushiCmdService.giveYushiToMemberByAgent(
					memberChaguanYushiAccountDbo.getMemberId(), agentId, -memberChaguanYushiAccountDbo.getBalance(),
					"agent clear", System.currentTimeMillis());
			MemberChaguanYushiRecordDbo mcyrd = memberChaguanYushiService.withdraw(memberRecord,
					memberChaguanYushiAccountDbo.getMemberId(), agentId);
			memberChaguanYushiRecordMsgService.recordMemberChaguanYushiRecordDbo(mcyrd);
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		} catch (AgentNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("AgentNotFoundException");
			return vo;
		} catch (InsufficientBalanceException e) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		}
		return vo;
	}
}
