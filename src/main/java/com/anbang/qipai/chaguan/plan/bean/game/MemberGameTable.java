package com.anbang.qipai.chaguan.plan.bean.game;

/**
 * 会员加入的游戏房间
 * 
 * @author Neo
 *
 */
public class MemberGameTable {
	private String id;
	private String memberId;
	private String nickname;
	private String headimgurl;// 头像url
	private GameTable gameTable;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

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

	public GameTable getGameTable() {
		return gameTable;
	}

	public void setGameTable(GameTable gameTable) {
		this.gameTable = gameTable;
	}

}
