package com.anbang.qipai.chaguan.msg.channel.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface FangpaoGameRoomSource {
	@Output
	MessageChannel fangpaoGameRoom();
}
