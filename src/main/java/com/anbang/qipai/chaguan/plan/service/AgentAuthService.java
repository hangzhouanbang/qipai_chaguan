package com.anbang.qipai.chaguan.plan.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.remote.service.QipaiAgentRemoteService;
import com.anbang.qipai.chaguan.remote.vo.CommonRemoteVO;
import com.dml.users.UserSessionsManager;

@Component
public class AgentAuthService {

	@Resource(name = "agentSessionsManager")
	private UserSessionsManager userSessionsManager;

	@Autowired
	private QipaiAgentRemoteService qipaiAgentRemoteService;

	public String getAgentIdBySessionId(String sessionId) {
		String memberId = userSessionsManager.getUserIdBySessionId(sessionId);
		if (memberId == null) {
			CommonRemoteVO rvo = qipaiAgentRemoteService.auth_trytoken(sessionId);
			if (rvo.isSuccess()) {
				Map data = (Map) rvo.getData();
				memberId = (String) data.get("agentId");
				userSessionsManager.createEngrossSessionForUser(memberId, sessionId, System.currentTimeMillis(), 0);
			}
		}
		return memberId;
	}

	public void removeSessionByAgentId(String agentId) {
		userSessionsManager.removeSessionsForUser(agentId);
	}
}
