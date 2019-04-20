package com.anbang.qipai.chaguan.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.msg.channel.source.ChaguanYushiRecordSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;

@EnableBinding(ChaguanYushiRecordSource.class)
public class ChaguanYushiRecordMsgService {

	@Autowired
	private ChaguanYushiRecordSource chaguanYushiRecordSource;

	public void recordChaguanYushiRecordDbo(ChaguanYushiRecordDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("accounting");
		mo.setData(dbo);
		chaguanYushiRecordSource.chaguanYushiRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
