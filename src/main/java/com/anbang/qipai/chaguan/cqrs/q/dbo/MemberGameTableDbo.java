package com.anbang.qipai.chaguan.cqrs.q.dbo;

/**
 * 玩家游戏桌子
 * 
 * @author lsc
 *
 */
public class MemberGameTableDbo {
	private String id;
	private String memberId;// 玩家id
	private GameTableDbo table;// 游戏桌子

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public GameTableDbo getTable() {
		return table;
	}

	public void setTable(GameTableDbo table) {
		this.table = table;
	}

}
