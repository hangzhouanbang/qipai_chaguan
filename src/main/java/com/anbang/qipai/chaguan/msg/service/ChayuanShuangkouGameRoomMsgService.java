package com.anbang.qipai.chaguan.msg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.msg.channel.source.ChayuanShuangkouGameRoomSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;

@EnableBinding(ChayuanShuangkouGameRoomSource.class)
public class ChayuanShuangkouGameRoomMsgService {
	@Autowired
	private ChayuanShuangkouGameRoomSource chayuanShuangkouGameRoomSource;

	public void removeGameRoom(List<String> gameIds) {
		CommonMO mo = new CommonMO();
		mo.setMsg("gameIds");
		mo.setData(gameIds);
		chayuanShuangkouGameRoomSource.chayuanShuangkouGameRoom().send(MessageBuilder.withPayload(mo).build());
	}
}
