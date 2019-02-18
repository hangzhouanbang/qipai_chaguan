package com.anbang.qipai.chaguan.msg.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ChaguanSource {

	@Output
	MessageChannel chaguan();

}
