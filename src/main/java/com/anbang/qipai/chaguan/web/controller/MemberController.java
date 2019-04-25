package com.anbang.qipai.chaguan.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.CreateMemberChaguanYushiAccountResult;
import com.anbang.qipai.chaguan.cqrs.c.domain.yushi.MemberHasYushiAccountAlreadyException;
import com.anbang.qipai.chaguan.cqrs.c.service.MemberChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanMemberDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiAccountDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.AgentDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanMemberDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberChaguanYushiService;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberDboService;
import com.anbang.qipai.chaguan.msg.service.ChaguanMemberMsgService;
import com.anbang.qipai.chaguan.msg.service.ChaguanMsgService;
import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApply;
import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberApplyState;
import com.anbang.qipai.chaguan.plan.bean.ChaguanMemberPayType;
import com.anbang.qipai.chaguan.plan.service.ChaguanMemberApplyService;
import com.anbang.qipai.chaguan.plan.service.MemberAuthService;
import com.anbang.qipai.chaguan.web.vo.ChaguanVO;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.highto.framework.web.page.ListPage;

/**
 * 玩家管理
 * 
 * @author lsc
 *
 */
@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private ChaguanDboService chaguanDboService;

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private MemberDboService memberDboService;

	@Autowired
	private AgentDboService agentDboService;

	@Autowired
	private ChaguanMemberApplyService chaguanMemberApplyService;

	@Autowired
	private ChaguanMemberDboService chaguanMemberDboService;

	@Autowired
	private MemberChaguanYushiCmdService memberChaguanYushiCmdService;

	@Autowired
	private MemberChaguanYushiService memberChaguanYushiService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

	@Autowired
	private ChaguanMemberMsgService chaguanMemberMsgService;

	@Autowired
	private ChaguanMsgService chaguanMsgService;

	/**
	 * 查找玩家茶馆
	 */
	@RequestMapping("/query_chaguan")
	public CommonVO queryChaguan(String token, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		ListPage listPage = chaguanMemberDboService.findChaguanMemberDboByMemberId(page, size, memberId);
		vo.setMsg("chaguan info");
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	/**
	 * 根据茶馆id查找茶馆
	 */
	@RequestMapping("/query_chaguan_by_id")
	public CommonVO queryChaguanById(String token, String chaguanId) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ChaguanDbo chaguanDbo = chaguanDboService.findChaguanDboById(chaguanId);
		if (chaguanDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("chaguan not found");
			return vo;
		}
		int onlineAmount = (int) chaguanMemberDboService.countOnlineMemberByChaguanId(chaguanId);
		MemberChaguanYushiAccountDbo account = memberChaguanYushiService
				.findMemberChaguanYushiAccountDboByChaguanIdAndMemebrId(chaguanId, memberId);
		ChaguanVO chaguan = new ChaguanVO();
		chaguan.setId(chaguanDbo.getId());
		chaguan.setHeadimgurl(chaguanDbo.getHeadimgurl());
		chaguan.setName(chaguanDbo.getName());
		chaguan.setDesc(chaguanDbo.getDesc());
		chaguan.setOnlineAmount(onlineAmount);
		chaguan.setMemberNum(chaguanDbo.getMemberNum());
		chaguan.setBalance(account.getBalance());
		chaguan.setJoin(
				chaguanMemberDboService.findChaguanMemberDboByMemberIdAndChaguanId(memberId, chaguanId, false) != null);
		vo.setMsg("chaguan info");
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("chaguan", chaguan);
		return vo;
	}

	/**
	 * 申请加入该茶馆
	 */
	@RequestMapping("/join_chaguan_apply")
	public CommonVO joinChaguanApply(String token, String chaguanId) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ChaguanDbo chaguanDbo = chaguanDboService.findChaguanDboById(chaguanId);
		if (chaguanDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("chaguan not found");
			return vo;
		}
		MemberDbo member = memberDboService.findMemberDboById(memberId);
		if (chaguanMemberApplyService.findChaguanMemberApplyByMemberIdAndChaguanIdAndState(memberId, chaguanId,
				ChaguanMemberApplyState.APPLYING) != null) {
			vo.setSuccess(false);
			vo.setMsg("applying");
			return vo;
		}
		ChaguanMemberApply apply = new ChaguanMemberApply();
		apply.setChaguanId(chaguanId);
		apply.setMemberId(memberId);
		apply.setNickname(member.getNickname());
		apply.setHeadimgurl(member.getHeadimgurl());
		apply.setCreateTime(System.currentTimeMillis());
		apply.setState(ChaguanMemberApplyState.APPLYING);
		chaguanMemberApplyService.addChaguanMemberApply(apply);
		return vo;
	}

	/**
	 * 通过加入茶馆的申请
	 */
	@RequestMapping("/pass_chaguan_apply")
	public CommonVO passChaguanApply(String applyId) {
		CommonVO vo = new CommonVO();
		ChaguanMemberApply apply = chaguanMemberApplyService.findChaguanMemberApplyById(applyId);
		if (apply == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid applyId");
			return vo;
		}
		ChaguanDbo chaguanDbo = chaguanDboService.findChaguanDboById(apply.getChaguanId());
		if (chaguanDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("chaguan not found");
			return vo;
		}
		AgentDbo agent = agentDboService.findAgentDboByAgentId(chaguanDbo.getAgentId());
		MemberDbo member = memberDboService.findMemberDboById(apply.getMemberId());
		// 创建玩家在该茶馆的玉石账户
		try {
			CreateMemberChaguanYushiAccountResult result = memberChaguanYushiCmdService
					.createYushiAccountForNewMember(member.getId(), agent.getId());
			memberChaguanYushiService.createYushiAccountForNewMember(result, chaguanDbo.getId());
		} catch (MemberHasYushiAccountAlreadyException e) {
			chaguanMemberDboService.updateChaguanMemberDboRemoveByMemberIdAndChaguanId(member.getId(),
					chaguanDbo.getId(), false);
			// 更新茶馆玩家人数
			chaguanDboService.updateChaguanDboMemberNum(chaguanDbo.getId());
			// 修改申请状态
			chaguanMemberApplyService.chaguanMemberApplyPass(applyId);
			return vo;
		}
		// 填充茶馆信息
		ChaguanMemberDbo dbo = new ChaguanMemberDbo();
		dbo.setAgentId(agent.getId());
		dbo.setAgentNickname(agent.getNickname());
		dbo.setAgentHeadimgurl(agent.getHeadimgurl());
		dbo.setChaguanId(chaguanDbo.getId());
		dbo.setChaguanName(chaguanDbo.getName());
		dbo.setChaguanDesc(chaguanDbo.getDesc());
		dbo.setMemberId(member.getId());
		dbo.setMemberNickname(member.getNickname());
		dbo.setHeadimgurl(member.getHeadimgurl());
		dbo.setOnlineStatus(member.getOnlineStatus());
		dbo.setChaguanYushi(0);
		dbo.setJoinTime(System.currentTimeMillis());
		dbo.setPayType(ChaguanMemberPayType.SELF);
		chaguanMemberDboService.addChaguanMemberDbo(dbo);
		chaguanMemberMsgService.recordChaguanMember(dbo);
		// 更新茶馆玩家人数
		chaguanDbo = chaguanDboService.updateChaguanDboMemberNum(chaguanDbo.getId());
		chaguanMsgService.updateChaguan(chaguanDbo);
		// 修改申请状态
		chaguanMemberApplyService.chaguanMemberApplyPass(applyId);
		return vo;
	}

	/**
	 * 拒绝加入茶馆的申请
	 */
	@RequestMapping("/refuse_chaguan_apply")
	public CommonVO refuseChaguanApply(String applyId) {
		CommonVO vo = new CommonVO();
		// 修改申请状态
		chaguanMemberApplyService.chaguanMemberApplyRefuse(applyId);
		return vo;
	}
}
