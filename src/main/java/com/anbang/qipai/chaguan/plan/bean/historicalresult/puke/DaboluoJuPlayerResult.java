package com.anbang.qipai.chaguan.plan.bean.historicalresult.puke;

import java.util.Map;

import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameJuPlayerResult;

public class DaboluoJuPlayerResult implements GameJuPlayerResult {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private int tspx;// 特殊牌型
	private int qld;// 全垒打
	private int totalScore;

	public DaboluoJuPlayerResult(Map juPlayerResult) {
		this.playerId = (String) juPlayerResult.get("playerId");
		this.nickname = (String) juPlayerResult.get("nickname");
		this.headimgurl = (String) juPlayerResult.get("headimgurl");
		this.tspx = ((Double) juPlayerResult.get("tspx")).intValue();
		this.qld = ((Double) juPlayerResult.get("qld")).intValue();
		this.totalScore = ((Double) juPlayerResult.get("totalScore")).intValue();
	}

	public DaboluoJuPlayerResult() {

	}

	public int getTspx() {
		return tspx;
	}

	public void setTspx(int tspx) {
		this.tspx = tspx;
	}

	public int getQld() {
		return qld;
	}

	public void setQld(int qld) {
		this.qld = qld;
	}

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

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	@Override
	public String playerId() {
		// TODO Auto-generated method stub
		return playerId;
	}

	@Override
	public String nickname() {
		// TODO Auto-generated method stub
		return nickname;
	}

	@Override
	public String headimgurl() {
		// TODO Auto-generated method stub
		return headimgurl;
	}

	@Override
	public int totalScore() {
		// TODO Auto-generated method stub
		return totalScore;
	}

}
