package com.anbang.qipai.chaguan.web.vo;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.game.Game;
import com.anbang.qipai.chaguan.plan.bean.game.GameLaw;

public class GameTableVO {
	private String no;// 房间6位编号,可循环使用
	private String chaguanId;// 茶馆id
	private Game game;
	private List<GameLaw> laws;
	private int playersCount;
	private String state;// 状态
	private List<PlayerVO> playerList;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getChaguanId() {
		return chaguanId;
	}

	public void setChaguanId(String chaguanId) {
		this.chaguanId = chaguanId;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<GameLaw> getLaws() {
		return laws;
	}

	public void setLaws(List<GameLaw> laws) {
		this.laws = laws;
	}

	public int getPlayersCount() {
		return playersCount;
	}

	public void setPlayersCount(int playersCount) {
		this.playersCount = playersCount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<PlayerVO> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<PlayerVO> playerList) {
		this.playerList = playerList;
	}

}
