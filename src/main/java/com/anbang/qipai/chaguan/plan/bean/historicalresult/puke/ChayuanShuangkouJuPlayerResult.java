package com.anbang.qipai.chaguan.plan.bean.historicalresult.puke;

import java.util.Map;

import com.anbang.qipai.chaguan.plan.bean.historicalresult.GameJuPlayerResult;

public class ChayuanShuangkouJuPlayerResult implements GameJuPlayerResult {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private int shuangkouCount;
	private int dankouCount;
	private int pingkouCount;
	private int maxXianshu;
	private int totalScore;

	public ChayuanShuangkouJuPlayerResult(Map juPlayerResult) {
		this.playerId = (String) juPlayerResult.get("playerId");
		this.nickname = (String) juPlayerResult.get("nickname");
		this.headimgurl = (String) juPlayerResult.get("headimgurl");
		this.shuangkouCount = ((Double) juPlayerResult.get("shuangkouCount")).intValue();
		this.dankouCount = ((Double) juPlayerResult.get("dankouCount")).intValue();
		this.pingkouCount = ((Double) juPlayerResult.get("pingkouCount")).intValue();
		this.maxXianshu = ((Double) juPlayerResult.get("maxXianshu")).intValue();
		this.totalScore = ((Double) juPlayerResult.get("totalScore")).intValue();
	}

	public ChayuanShuangkouJuPlayerResult() {

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

	public int getShuangkouCount() {
		return shuangkouCount;
	}

	public void setShuangkouCount(int shuangkouCount) {
		this.shuangkouCount = shuangkouCount;
	}

	public int getDankouCount() {
		return dankouCount;
	}

	public void setDankouCount(int dankouCount) {
		this.dankouCount = dankouCount;
	}

	public int getPingkouCount() {
		return pingkouCount;
	}

	public void setPingkouCount(int pingkouCount) {
		this.pingkouCount = pingkouCount;
	}

	public int getMaxXianshu() {
		return maxXianshu;
	}

	public void setMaxXianshu(int maxXianshu) {
		this.maxXianshu = maxXianshu;
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
