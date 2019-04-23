package com.anbang.qipai.chaguan.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanMemberDboService;
import com.anbang.qipai.chaguan.msg.channel.sink.MemberLoginRecordSink;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.plan.bean.MemberLoginRecord;
import com.google.gson.Gson;

@EnableBinding(MemberLoginRecordSink.class)
public class MemberLoginRecordMsgReceiver {

	@Autowired
	private ChaguanMemberDboService chaguanMemberDboService;

	private Gson gson = new Gson();

	@StreamListener(MemberLoginRecordSink.MEMBERLOGINRECORD)
	public void memberLoginRecord(CommonMO mo) {
		String msg = mo.getMsg();
		Map map = (Map) mo.getData();
		if ("member login".equals(msg)) {
			String json = gson.toJson(map.get("record"));
			MemberLoginRecord record = gson.fromJson(json, MemberLoginRecord.class);
			String onlineState = (String) map.get("onlineState");
			chaguanMemberDboService.updateMemberOnlineStatus(record.getMemberId(), onlineState);
		}
		if ("update member onlineTime".equals(msg)) {
			String json = gson.toJson(mo.getData());
			// MemberLoginRecord record = gson.fromJson(json, MemberLoginRecord.class);
		}
		if ("member logout".equals(msg)) {
			String memberId = (String) map.get("memberId");
			String onlineState = (String) map.get("onlineState");
			chaguanMemberDboService.updateMemberOnlineStatus(memberId, onlineState);
		}
	}
}
