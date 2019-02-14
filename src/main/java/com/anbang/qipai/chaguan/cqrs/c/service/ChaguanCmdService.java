package com.anbang.qipai.chaguan.cqrs.c.service;

public interface ChaguanCmdService {

	String createNewChaguanId(Long currentTime);

	String removeChaguan(String chaguanId);
}
