package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.AuthorizationDbo;

public interface AuthorizationDboDao {

	AuthorizationDbo findAuthorizationDboByPublisherAndUuid(String publisher, String uuid);

	AuthorizationDbo findAuthorizationDboByAgentId(String agentId);

	void addAuthorizationDbo(AuthorizationDbo authDbo);
}
