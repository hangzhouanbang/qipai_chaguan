package com.anbang.qipai.chaguan.web.fb;

import java.util.List;

/**
 * 温州双扣玩法
 * 
 * @author lsc
 *
 */
public class WzskLawsFB {
	private String panshu;
	private String renshu;
	private String bx;
	private String chapai;
	private String fapai;
	private String chaodi = "false";
	private String shuangming = "false";
	private String bxfd = "false";
	private String jxfd = "false";
	private String sxfd = "false";
	private String gxjb = "false";

	public WzskLawsFB(List<String> lawNames) {
		lawNames.forEach((lawName) -> {
			if (lawName.equals("bj")) {// 八局
				panshu = "8";
			} else if (lawName.equals("ssj")) {// 三十局
				panshu = "30";
			} else if (lawName.equals("sj")) {// 四局
				panshu = "4";
			} else if (lawName.equals("slj")) {// 十六局
				panshu = "16";
			} else if (lawName.equals("er")) {// 二人
				renshu = "2";
			} else if (lawName.equals("sir")) {// 四人
				renshu = "4";
			} else if (lawName.equals("qianbian")) {// 千变
				bx = "qianbian";
			} else if (lawName.equals("banqianbian")) {// 半千变
				bx = "banqianbian";
			} else if (lawName.equals("baibian")) {// 百变
				bx = "baibian";
			} else if (lawName.equals("erliuhun")) {// 2-6混
				chapai = "erliuhun";
			} else if (lawName.equals("sanwu")) {// 3/5张
				chapai = "sanwu";
			} else if (lawName.equals("ba")) {// 8张
				chapai = "ba";
			} else if (lawName.equals("wuxu")) {// 无序
				chapai = "wuxu";
			} else if (lawName.equals("san")) {// 3
				fapai = "san";
			} else if (lawName.equals("sanliu")) {// 3/6
				fapai = "sanliu";
			} else if (lawName.equals("liuqi")) {// 6/7
				fapai = "liuqi";
			} else if (lawName.equals("jiu")) {// 9
				fapai = "jiu";
			} else if (lawName.equals("chaodi")) {// 抄底
				chaodi = "true";
			} else if (lawName.equals("shuangming")) {// 双明
				shuangming = "true";
			} else if (lawName.equals("bxfd")) {// 八线封顶
				bxfd = "true";
			} else if (lawName.equals("jxfd")) {// 九线封顶
				jxfd = "true";
			} else if (lawName.equals("sxfd")) {// 十线封顶
				sxfd = "true";
			} else if (lawName.equals("gxjb")) {// 贡献减半
				gxjb = "true";
			} else {

			}
		});
	}

	public int payForCreateRoom() {
		int gold = 100;
		if (panshu.equals("30")) {
			gold = 200;
		} else if (panshu.equals("16")) {
			gold = 100;
		} else if (panshu.equals("8")) {
			gold = 50;
		} else if (panshu.equals("4")) {
			gold = 100;
		} else {

		}
		return gold;
	}

	public int payForJoinRoom() {
		int gold = 100;
		if (panshu.equals("30")) {
			gold = 200;
		} else if (panshu.equals("16")) {
			gold = 100;
		} else if (panshu.equals("8")) {
			gold = 50;
		} else if (panshu.equals("4")) {
			gold = 100;
		} else {

		}
		return gold;
	}

	public String getPanshu() {
		return panshu;
	}

	public void setPanshu(String panshu) {
		this.panshu = panshu;
	}

	public String getRenshu() {
		return renshu;
	}

	public void setRenshu(String renshu) {
		this.renshu = renshu;
	}

	public String getBx() {
		return bx;
	}

	public void setBx(String bx) {
		this.bx = bx;
	}

	public String getChaodi() {
		return chaodi;
	}

	public void setChaodi(String chaodi) {
		this.chaodi = chaodi;
	}

	public String getShuangming() {
		return shuangming;
	}

	public void setShuangming(String shuangming) {
		this.shuangming = shuangming;
	}

	public String getBxfd() {
		return bxfd;
	}

	public void setBxfd(String bxfd) {
		this.bxfd = bxfd;
	}

	public String getJxfd() {
		return jxfd;
	}

	public void setJxfd(String jxfd) {
		this.jxfd = jxfd;
	}

	public String getSxfd() {
		return sxfd;
	}

	public void setSxfd(String sxfd) {
		this.sxfd = sxfd;
	}

	public String getChapai() {
		return chapai;
	}

	public void setChapai(String chapai) {
		this.chapai = chapai;
	}

	public String getFapai() {
		return fapai;
	}

	public void setFapai(String fapai) {
		this.fapai = fapai;
	}

	public String getGxjb() {
		return gxjb;
	}

	public void setGxjb(String gxjb) {
		this.gxjb = gxjb;
	}

}
