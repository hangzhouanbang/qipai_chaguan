package com.anbang.qipai.chaguan.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.conf.PayTypeConfig;
import com.anbang.qipai.chaguan.cqrs.c.service.ChaguanShopOrderCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.AgentDboService;
import com.anbang.qipai.chaguan.plan.bean.ChaguanShopOrder;
import com.anbang.qipai.chaguan.plan.bean.ChaguanShopProduct;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.plan.service.ChaguanShopOrderService;
import com.anbang.qipai.chaguan.plan.service.ChaguanShopProductService;
import com.anbang.qipai.chaguan.plan.service.WXpayService;
import com.anbang.qipai.chaguan.util.IPUtil;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.highto.framework.web.page.ListPage;

@RestController
@RequestMapping("/shop")
public class ChaguanShopController {

	@Autowired
	private ChaguanShopProductService chaguanShopProductService;

	@Autowired
	private AgentAuthService agentAuthService;

	@Autowired
	private AgentDboService agentDboService;

	@Autowired
	private WXpayService wxpayService;

	@Autowired
	private ChaguanShopOrderService chaguanShopOrderService;

	@Autowired
	private ChaguanShopOrderCmdService chaguanShopOrderCmdService;

	/**
	 * 添加商品
	 */
	@RequestMapping("/product_add")
	public CommonVO addProduct(ChaguanShopProduct product) {
		CommonVO vo = new CommonVO();
		chaguanShopProductService.addChaguanShopProduct(product);
		return vo;
	}

	/**
	 * 修改商品
	 */
	@RequestMapping("/product_update")
	public CommonVO updateProduct(ChaguanShopProduct product) {
		CommonVO vo = new CommonVO();
		chaguanShopProductService.updateChaguanShopProduct(product);
		return vo;
	}

	/**
	 * 删除商品
	 */
	@RequestMapping("/product_remove")
	public CommonVO removeProduct(String[] productIds) {
		CommonVO vo = new CommonVO();
		chaguanShopProductService.removeChaguanShopProducts(productIds);
		return vo;
	}

	/**
	 * 查询商品
	 */
	@RequestMapping("/product_query")
	public CommonVO queryProduct(String token, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = chaguanShopProductService.findChaguanShopProduct(page, size);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("listPage", listPage);
		return vo;
	}

	@RequestMapping("/buy_product_wx")
	public CommonVO buyProduct_XW(String token, String productId, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		String agentId = agentAuthService.getAgentIdBySessionId(token);
		if (agentId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		AgentDbo agent = agentDboService.findAgentDboByAgentId(agentId);
		if (!agent.isAgentAuth() || !"正常".equals(agent.getState())) {
			vo.setSuccess(false);
			vo.setMsg("not agent");
			return vo;
		}
		ChaguanShopProduct product = chaguanShopProductService.findChaguanShopProductById(productId);
		if (product == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid productId");
			return vo;
		}
		AuthorizationDbo openidAuthDbo = agentDboService.findThirdAuthorizationDboByAgentId(agentId);
		if (openidAuthDbo == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid openId");
			return vo;
		}
		String reqIP = IPUtil.getRealIp(request);
		ChaguanShopOrder order = chaguanShopOrderService.addChaguanShopOrder(agentId, agent.getNickname(), agentId,
				agent.getNickname(), productId, product.getName(), product.getPrice(), product.getRewardType(),
				product.getRewardNum(), 1, PayTypeConfig.WECHATPAY, reqIP);
		try {
			chaguanShopOrderCmdService.createOrder(order.getId(), agentId, System.currentTimeMillis());
			Map<String, String> resultMap = wxpayService.createOrder(order, openidAuthDbo.getUuid());
			if (resultMap == null) {
				vo.setSuccess(false);
				vo.setMsg("order fail");
			} else {
				vo.setSuccess(true);
				vo.setMsg("orderinfo");
				vo.setData(resultMap);
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
		}
		return vo;
	}
}
