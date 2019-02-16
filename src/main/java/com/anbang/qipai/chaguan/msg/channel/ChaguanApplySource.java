package com.anbang.qipai.chaguan.msg.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ChaguanApplySource {

	@Output
	MessageChannel chaguanApply();

}
