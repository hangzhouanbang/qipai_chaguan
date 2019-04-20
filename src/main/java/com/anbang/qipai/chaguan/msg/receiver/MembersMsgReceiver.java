package com.anbang.qipai.chaguan.msg.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.MemberDboService;
import com.anbang.qipai.chaguan.msg.channel.sink.MembersSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.google.gson.Gson;

@EnableBinding(MembersSink.class)
public class MembersMsgReceiver {

	@Autowired
	private MemberDboService memberService;

	private Gson gson = new Gson();

	@StreamListener(MembersSink.MEMBERS)
	public void recordMember(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		MemberDbo member = gson.fromJson(json, MemberDbo.class);
		if ("newMember".equals(msg)) {
			memberService.insertMemberDbo(member);
		}
		if ("update member info".equals(msg)) {
			memberService.updateMemberBaseInfo(member.getId(), member.getNickname(), member.getHeadimgurl(),
					member.getGender());
		}
	}

}
