package com.anbang.qipai.chaguan.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;
import com.anbang.qipai.chaguan.msg.channel.source.ChaguanSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;

@EnableBinding(ChaguanSource.class)
public class ChaguanMsgService {

	@Autowired
	private ChaguanSource chaguanSource;

	public void createChaguan(ChaguanDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("new chaguan");
		mo.setData(dbo);
		chaguanSource.chaguan().send(MessageBuilder.withPayload(mo).build());
	}

	public void updateChaguan(ChaguanDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("update chaguan");
		mo.setData(dbo);
		chaguanSource.chaguan().send(MessageBuilder.withPayload(mo).build());
	}
}
