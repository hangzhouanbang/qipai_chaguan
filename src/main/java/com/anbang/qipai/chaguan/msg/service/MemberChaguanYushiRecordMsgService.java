package com.anbang.qipai.chaguan.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.msg.channel.source.MemberChaguanYushiRecordSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;

@EnableBinding(MemberChaguanYushiRecordSource.class)
public class MemberChaguanYushiRecordMsgService {

	@Autowired
	private MemberChaguanYushiRecordSource memberChaguanYushiRecordSource;

	public void recordMemberChaguanYushiRecordDbo(MemberChaguanYushiRecordDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("accounting");
		mo.setData(dbo);
		memberChaguanYushiRecordSource.memberChaguanYushiRecord().send(MessageBuilder.withPayload(mo).build());
	}
}
