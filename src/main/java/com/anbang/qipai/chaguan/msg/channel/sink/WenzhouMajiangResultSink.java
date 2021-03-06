package com.anbang.qipai.chaguan.msg.channel.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface WenzhouMajiangResultSink {
	String WENZHOUMAJIANGRESULT = "wenzhouMajiangResult";

	@Input
	SubscribableChannel wenzhouMajiangResult();
}
