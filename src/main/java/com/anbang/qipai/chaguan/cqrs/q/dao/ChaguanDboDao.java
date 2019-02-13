package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanDbo;

public interface ChaguanDboDao {

	void addChaguanDbo(ChaguanDbo dbo);

	void updateChaguanDboMemberNum(String chaguanId, int memberNum);
}
