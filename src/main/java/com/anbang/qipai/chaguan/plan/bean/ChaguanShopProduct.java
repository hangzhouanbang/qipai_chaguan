package com.anbang.qipai.chaguan.plan.bean;

public class ChaguanShopProduct {

	private String id;
	private String name;
	private RewardType rewardType;// 奖励类型
	private double rewardNum;// 奖励数量
	private double price;
	private double firstDiscount;// 首次折扣
	private double firstDiscountPrice;// 首次折扣后价格

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RewardType getRewardType() {
		return rewardType;
	}

	public void setRewardType(RewardType rewardType) {
		this.rewardType = rewardType;
	}

	public double getRewardNum() {
		return rewardNum;
	}

	public void setRewardNum(double rewardNum) {
		this.rewardNum = rewardNum;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getFirstDiscount() {
		return firstDiscount;
	}

	public void setFirstDiscount(double firstDiscount) {
		this.firstDiscount = firstDiscount;
	}

	public double getFirstDiscountPrice() {
		return firstDiscountPrice;
	}

	public void setFirstDiscountPrice(double firstDiscountPrice) {
		this.firstDiscountPrice = firstDiscountPrice;
	}

}
