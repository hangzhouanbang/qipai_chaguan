package com.anbang.qipai.chaguan.plan.bean.game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameTable {
	private String id;
	private String no;// 房间6位编号,可循环使用
	private String chaguanId;// 茶馆id
	private Game game;
	private List<GameLaw> laws;
	private int playersCount;
	private int panCountPerJu;
	private ServerGame serverGame;
	private long createTime;
	private String state;// 状态

	public boolean validateLaws() {
		if (laws != null) {
			Set<String> groupIdSet = new HashSet<>();
			for (GameLaw law : laws) {
				String groupId = law.getMutexGroupId();
				if (groupId != null) {
					// contain this element,return false
					if (!groupIdSet.add(groupId)) {
						return false;
					}
				}
			}
		}
		return true;
	}

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

	public ServerGame getServerGame() {
		return serverGame;
	}

	public void setServerGame(ServerGame serverGame) {
		this.serverGame = serverGame;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getChaguanId() {
		return chaguanId;
	}

	public void setChaguanId(String chaguanId) {
		this.chaguanId = chaguanId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
