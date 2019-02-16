package com.anbang.qipai.chaguan.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.chaguan.conf.PayTypeConfig;
import com.anbang.qipai.chaguan.cqrs.c.domain.chaguan.AgentNotFoundException;
import com.anbang.qipai.chaguan.cqrs.c.service.AgentChaguanYushiCmdService;
import com.anbang.qipai.chaguan.cqrs.c.service.ChaguanShopOrderCmdService;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AgentDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.chaguan.cqrs.q.dbo.ChaguanYushiRecordDbo;
import com.anbang.qipai.chaguan.cqrs.q.service.AgentDboService;
import com.anbang.qipai.chaguan.cqrs.q.service.ChaguanYushiService;
import com.anbang.qipai.chaguan.plan.bean.ChaguanShopOrder;
import com.anbang.qipai.chaguan.plan.bean.ChaguanShopProduct;
import com.anbang.qipai.chaguan.plan.bean.RewardType;
import com.anbang.qipai.chaguan.plan.service.AgentAuthService;
import com.anbang.qipai.chaguan.plan.service.ChaguanShopOrderService;
import com.anbang.qipai.chaguan.plan.service.ChaguanShopProductService;
import com.anbang.qipai.chaguan.plan.service.WXpayService;
import com.anbang.qipai.chaguan.util.IPUtil;
import com.anbang.qipai.chaguan.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
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

	@Autowired
	private AgentChaguanYushiCmdService agentChaguanYushiCmdService;

	@Autowired
	private ChaguanYushiService chaguanYushiService;

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
		// TODO Kafka消息 创建订单
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

	/**
	 * 微信通知回调
	 */
	@RequestMapping("/wxnotify")
	public String wxNotify(HttpServletRequest request) {
		SortedMap<String, String> resultMap = null;
		try {
			resultMap = wxpayService.receiveNotify(request);
		} catch (IOException e) {
			return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[验签失败]]></return_msg></xml>";
		}
		if (resultMap != null) {
			String transaction_id = resultMap.get("transaction_id");
			SortedMap<String, String> responseMap = null;
			try {
				responseMap = wxpayService.queryOrderResult(transaction_id, null);
			} catch (Exception e) {
				return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>";
			}
			if (responseMap != null && "SUCCESS".equals(responseMap.get("result_code"))) {
				String out_trade_no = responseMap.get("out_trade_no");
				String trade_state = responseMap.get("trade_state");
				ChaguanShopOrder order = chaguanShopOrderService.findChaguanShopOrderById(out_trade_no);
				if ("SUCCESS".equals(order.getStatus()) || "CLOSED".equals(order.getStatus())
						|| "PAYERROR".equals(order.getStatus())) {
					return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
				}
				ChaguanShopOrder finishedOrder = chaguanShopOrderService.orderFinished(out_trade_no, transaction_id,
						trade_state, System.currentTimeMillis());
				// TODO Kafka消息 订单完成
				try {
					// 交易成功
					if ("SUCCESS".equals(trade_state)) {
						// TODO Kafka消息 推广员消费
						if (finishedOrder.getRewardType().equals(RewardType.chaguanyushi)) {
							AccountingRecord rcd = agentChaguanYushiCmdService.giveChaguanyushiToAgent(
									finishedOrder.getReceiverId(), (int) finishedOrder.getRewardNum(),
									"BUY CHAGUANYUSHI", System.currentTimeMillis());
							ChaguanYushiRecordDbo dbo = chaguanYushiService.withdraw(rcd,
									finishedOrder.getReceiverId());
							// TODO Kafka消息 流水记录
						}
					} else {

					}
				} catch (AgentNotFoundException e) {
					e.printStackTrace();
				}
				return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
			}
		}
		return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[验签失败]]></return_msg></xml>";
	}

	/**
	 * 微信支付查询
	 */
	@RequestMapping("/wxquery")
	public CommonVO wxQuery(String out_trade_no) {
		CommonVO vo = new CommonVO();
		SortedMap<String, String> responseMap = null;
		try {
			responseMap = wxpayService.queryOrderResult(null, out_trade_no);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		if (responseMap != null && "SUCCESS".equals(responseMap.get("result_code"))) {
			String transaction_id = responseMap.get("transaction_id");
			String trade_state = responseMap.get("trade_state");
			ChaguanShopOrder order = chaguanShopOrderService.findChaguanShopOrderById(out_trade_no);
			if ("SUCCESS".equals(order.getStatus()) || "CLOSED".equals(order.getStatus())
					|| "PAYERROR".equals(order.getStatus())) {
				vo.setSuccess(true);
				return vo;
			}
			ChaguanShopOrder finishedOrder = chaguanShopOrderService.orderFinished(out_trade_no, transaction_id,
					trade_state, System.currentTimeMillis());
			// TODO Kafka消息 订单完成
			try {
				// 交易成功
				if ("SUCCESS".equals(trade_state)) {
					// TODO Kafka消息 推广员消费
					if (finishedOrder.getRewardType().equals(RewardType.chaguanyushi)) {
						AccountingRecord rcd = agentChaguanYushiCmdService.giveChaguanyushiToAgent(
								finishedOrder.getReceiverId(), (int) finishedOrder.getRewardNum(), "BUY CHAGUANYUSHI",
								System.currentTimeMillis());
						ChaguanYushiRecordDbo dbo = chaguanYushiService.withdraw(rcd, finishedOrder.getReceiverId());
						// TODO Kafka消息 流水记录
					}
				} else {

				}
			} catch (AgentNotFoundException e) {
				e.printStackTrace();
			}
			vo.setSuccess(true);
			return vo;
		}
		vo.setSuccess(false);
		return vo;
	}
}
