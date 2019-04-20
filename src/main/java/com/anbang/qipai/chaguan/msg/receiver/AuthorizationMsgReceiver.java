package com.anbang.qipai.chaguan.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.chaguan.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.AgentDboService;
import com.anbang.qipai.chaguan.msg.channel.sink.AuthorizationSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.google.gson.Gson;

@EnableBinding(AuthorizationSink.class)
public class AuthorizationMsgReceiver {

	@Autowired
	private AgentDboService agentDboService;

	private Gson gson = new Gson();

	@StreamListener(AuthorizationSink.AUTHORIZATION)
	public void authorization(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("new authorization".equals(msg)) {
			AuthorizationDbo authDbo = gson.fromJson(json, AuthorizationDbo.class);
			agentDboService.addAuthorizationDbo(authDbo);
		}
	}
}
