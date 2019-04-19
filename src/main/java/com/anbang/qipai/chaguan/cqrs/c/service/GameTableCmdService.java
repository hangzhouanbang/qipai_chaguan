package com.anbang.qipai.chaguan.cqrs.c.service;

public interface GameTableCmdService {

	String createTable(String memberId, Long createTime);

	String removeTable(String no);
}
