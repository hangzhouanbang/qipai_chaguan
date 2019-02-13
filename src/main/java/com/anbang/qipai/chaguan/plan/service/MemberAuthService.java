package com.anbang.qipai.chaguan.plan.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.remote.service.QipaiMembersRemoteService;
import com.anbang.qipai.chaguan.remote.vo.CommonRemoteVO;
import com.dml.users.UserSessionsManager;

@Component
public class MemberAuthService {

	@Resource(name = "memberSessionsManager")
	private UserSessionsManager userSessionsManager;

	@Autowired
	private QipaiMembersRemoteService qipaiMembersRomoteService;

	public String getMemberIdBySessionId(String sessionId) {
		String memberId = userSessionsManager.getUserIdBySessionId(sessionId);
		if (memberId == null) {
			CommonRemoteVO rvo = qipaiMembersRomoteService.auth_trytoken(sessionId);
			if (rvo.isSuccess()) {
				Map data = (Map) rvo.getData();
				memberId = (String) data.get("memberId");
				userSessionsManager.createEngrossSessionForUser(memberId, sessionId, System.currentTimeMillis(), 0);
			}
		}
		return memberId;
	}

	public void removeSessionByMemberId(String memberId) {
		userSessionsManager.removeSessionsForUser(memberId);
	}

}
