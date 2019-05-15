package com.anbang.qipai.chaguan.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.chaguan.msg.channel.source.ChaguanDataReportSource;
import com.anbang.qipai.chaguan.msg.msjobs.CommonMO;
import com.anbang.qipai.chaguan.web.vo.GameDataReportVO;

/**
 * @author YaphetS
 * @date 2018/11/30
 */
@EnableBinding(ChaguanDataReportSource.class)
public class GameDataReportMsgService {
	@Autowired
	private ChaguanDataReportSource chaguanDataReportSource;

	public void recordGameDataReport(GameDataReportVO report) {
		CommonMO mo = new CommonMO();
		mo.setMsg("record chaguanDataReport");
		mo.setData(report);
		chaguanDataReportSource.chaguanDataReport().send(MessageBuilder.withPayload(mo).build());
	}
}
