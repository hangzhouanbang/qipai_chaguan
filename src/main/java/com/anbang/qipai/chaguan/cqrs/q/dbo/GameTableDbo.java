package com.anbang.qipai.chaguan.cqrs.q.dbo;

import java.util.List;

import com.anbang.qipai.chaguan.plan.bean.Game;
import com.anbang.qipai.chaguan.plan.bean.GameLaw;
import com.anbang.qipai.chaguan.plan.bean.GameServer;

/**
 * 游戏桌子
 * 
 * @author lsc
 *
 */
public class GameTableDbo {
	private String id;
	private String no;// 桌号
	private Game game;// 游戏
	private List<GameLaw> laws;// 玩法
	private int playersCount;// 玩家数
	private int panCountPerJu;// 一局的盘数
	private GameServer server;// 服务器
	private int currentPanNum;// 当前盘
	private String createMemberId;// 创建者
	private long createTime;// 创建时间
	private long deadlineTime;// 有效时间
	private String state;// 桌子状态

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
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

	public int getPanCountPerJu() {
		return panCountPerJu;
	}

	public void setPanCountPerJu(int panCountPerJu) {
		this.panCountPerJu = panCountPerJu;
	}

	public GameServer getServer() {
		return server;
	}

	public void setServer(GameServer server) {
		this.server = server;
	}

	public int getCurrentPanNum() {
		return currentPanNum;
	}

	public void setCurrentPanNum(int currentPanNum) {
		this.currentPanNum = currentPanNum;
	}

	public String getCreateMemberId() {
		return createMemberId;
	}

	public void setCreateMemberId(String createMemberId) {
		this.createMemberId = createMemberId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getDeadlineTime() {
		return deadlineTime;
	}

	public void setDeadlineTime(long deadlineTime) {
		this.deadlineTime = deadlineTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
