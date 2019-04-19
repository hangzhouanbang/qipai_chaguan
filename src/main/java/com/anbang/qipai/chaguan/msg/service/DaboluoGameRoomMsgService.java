package com.anbang.qipai.chaguan.msg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.msg.channel.source.DaboluoGameRoomSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;

@EnableBinding(DaboluoGameRoomSource.class)
public class DaboluoGameRoomMsgService {

	@Autowired
	private DaboluoGameRoomSource daboluoGameRoomSource;

	public void removeGameRoom(List<String> gameIds) {
		CommonMO mo = new CommonMO();
		mo.setMsg("gameIds");
		mo.setData(gameIds);
		daboluoGameRoomSource.daboluoGameRoom().send(MessageBuilder.withPayload(mo).build());
	}
}
