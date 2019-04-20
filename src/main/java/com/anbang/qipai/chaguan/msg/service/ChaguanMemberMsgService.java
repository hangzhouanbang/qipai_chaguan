package com.anbang.qipai.chaguan.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanMemberDbo;
import com.anbang.qipai.chaguan.msg.channel.source.ChaguanMemberSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;

@EnableBinding(ChaguanMemberSource.class)
public class ChaguanMemberMsgService {
	@Autowired
	private ChaguanMemberSource chaguanMemberSource;

	public void recordChaguanMember(ChaguanMemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("new chaguan member");
		mo.setData(member);
		chaguanMemberSource.chaguanMember().send(MessageBuilder.withPayload(mo).build());
	}

	public void removeChaguanMember(ChaguanMemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("remove chaguan member");
		mo.setData(member);
		chaguanMemberSource.chaguanMember().send(MessageBuilder.withPayload(mo).build());
	}

	public void setChaguanMember(ChaguanMemberDbo member) {
		CommonMO mo = new CommonMO();
		mo.setMsg("set chaguan member");
		mo.setData(member);
		chaguanMemberSource.chaguanMember().send(MessageBuilder.withPayload(mo).build());
	}
}
