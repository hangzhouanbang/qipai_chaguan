package com.anbang.qipai.chaguan.cqrs.q.dao;

import com.anbang.qipai.chaguan.cqrs.q.dbo.MemberChaguanYushiRecordDbo;

public interface MemberChaguanYushiRecordDboDao {

	void insert(MemberChaguanYushiRecordDbo record);

	/**
	 * 统计玩家最近余额小于balanceAfter的交易总数
	 */
	long countByMemberIdAndLastestBalanceAfterLessThan(String memberId, int balanceAfter);
}
