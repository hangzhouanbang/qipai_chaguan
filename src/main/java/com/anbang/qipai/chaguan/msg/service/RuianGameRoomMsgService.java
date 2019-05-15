package com.anbang.qipai.chaguan.msg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.msg.channel.source.RuianGameRoomSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;

@EnableBinding(RuianGameRoomSource.class)
public class RuianGameRoomMsgService {

	@Autowired
	private RuianGameRoomSource ruianGameRoomSource;

	public void removeGameRoom(List<String> gameIds) {
		CommonMO mo = new CommonMO();
		mo.setMsg("gameIds");
		mo.setData(gameIds);
		ruianGameRoomSource.ruianGameRoom().send(MessageBuilder.withPayload(mo).build());
	}
}
