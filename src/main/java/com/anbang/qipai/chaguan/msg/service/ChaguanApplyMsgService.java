package com.anbang.qipai.chaguan.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.msg.channel.source.ChaguanApplySource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.plan.bean.ChaguanApply;

@EnableBinding(ChaguanApplySource.class)
public class ChaguanApplyMsgService {

	@Autowired
	private ChaguanApplySource chaguanApplySource;

	public void recordChaguanApply(ChaguanApply apply) {
		CommonMO mo = new CommonMO();
		mo.setMsg("new apply");
		mo.setData(apply);
		chaguanApplySource.chaguanApply().send(MessageBuilder.withPayload(mo).build());
	}
}
