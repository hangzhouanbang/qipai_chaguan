package com.anbang.qipai.chaguan.remote.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anbang.qipai.chaguan.remote.vo.CommonRemoteVO;

@FeignClient("qipai-agents")
public interface QipaiAgentRemoteService {

	@RequestMapping(value = "/auth/trytoken")
	public CommonRemoteVO auth_trytoken(@RequestParam("token") String token);

}
