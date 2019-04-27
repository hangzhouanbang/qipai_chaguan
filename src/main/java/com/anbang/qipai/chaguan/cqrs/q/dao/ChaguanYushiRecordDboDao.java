package com.anbang.qipai.chaguan.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;

public interface ChaguanYushiRecordDboDao {

	void insert(ChaguanYushiRecordDbo record);

	long countBySummary(String text);

	List<ChaguanYushiRecordDbo> findBySummary(int page, int size, String text);
}
