package com.anbang.qipai.chaguan.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.ChaguanIdManager;
import com.anbang.qipai.chaguan.cqrs.c.service.ChaguanCmdService;

@Component
public class ChaguanCmdServiceImpl extends CmdServiceBase implements ChaguanCmdService {

	@Override
	public String createNewChaguanId(Long currentTime) {
		ChaguanIdManager chaguanIdManager = singletonEntityRepository.getEntity(ChaguanIdManager.class);
		String chaguanId = chaguanIdManager.createChaguanId(currentTime);
		return chaguanId;
	}

	@Override
	public String removeChaguan(String chaguanId) {
		ChaguanIdManager chaguanIdManager = singletonEntityRepository.getEntity(ChaguanIdManager.class);
		chaguanIdManager.removeChaguanId(chaguanId);
		return chaguanId;
	}

}
