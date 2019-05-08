package com.anbang.qipai.chaguan.web.vo;

public class MemberHistoricalResultVO {

	private String playerId;// 玩家id
	private String nickname;// 昵称
	private String headimgurl;// 头像
	private int dayingjiaCount;// 大赢家次数
	private int chaguanYushiCost;// 茶馆玉石消耗
	private int totalScore;// 总得分

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

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

	public int getDayingjiaCount() {
		return dayingjiaCount;
	}

	public void setDayingjiaCount(int dayingjiaCount) {
		this.dayingjiaCount = dayingjiaCount;
	}

	public int getChaguanYushiCost() {
		return chaguanYushiCost;
	}

	public void setChaguanYushiCost(int chaguanYushiCost) {
		this.chaguanYushiCost = chaguanYushiCost;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
