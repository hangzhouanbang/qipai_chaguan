package com.anbang.qipai.chaguan.web.vo;

public enum RecordSummaryTexts {
	购买茶馆玉石, 玩家充值, 馆主充值, 游戏结算;

	public static String getSummaryText(String text) {
		switch (text) {
		case "BUY CHAGUANYUSHI":
			return 购买茶馆玉石.name();
		case "recharge member":
			return 玩家充值.name();
		case "agent recharge":
			return 馆主充值.name();
		case "game ju finish":
			return 游戏结算.name();
		default:
			return text;
		}
	}
}
