package com.anbang.qipai.chaguan;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.anbang.qipai.chaguan.conf.FilePathConfig;
import com.anbang.qipai.chaguan.cqrs.c.repository.SingletonEntityFactoryImpl;
import com.anbang.qipai.chaguan.cqrs.c.service.disruptor.CoreSnapshotFactory;
import com.anbang.qipai.chaguan.cqrs.c.service.disruptor.ProcessCoreCommandEventHandler;
import com.anbang.qipai.chaguan.cqrs.c.service.disruptor.SnapshotJsonUtil;
import com.anbang.qipai.chaguan.init.InitProcessor;
import com.dml.users.UserSessionsManager;
import com.highto.framework.ddd.SingletonEntityRepository;

@EnableEurekaClient
@SpringBootApplication
@EnableScheduling
public class QipaiChaguanApplication {
	@Autowired
	private SnapshotJsonUtil snapshotJsonUtil;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private CoreSnapshotFactory coreSnapshotFactory;

	@Autowired
	private FilePathConfig filePathConfig;

	@Bean
	public HttpClient httpClient() {
		HttpClient client = new HttpClient();
		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	@Bean
	public HttpClient sslHttpClient() {

		HttpClient client = new HttpClient(new SslContextFactory());
		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;

	}

	@Bean(autowire = Autowire.BY_NAME, value = "memberSessionsManager")
	public UserSessionsManager memberSessionsManager() {
		return new UserSessionsManager();
	}

	@Bean(autowire = Autowire.BY_NAME, value = "agentSessionsManager")
	public UserSessionsManager agentSessionsManager() {
		return new UserSessionsManager();
	}

	@Bean
	public SingletonEntityRepository singletonEntityRepository() {
		SingletonEntityRepository singletonEntityRepository = new SingletonEntityRepository();
		singletonEntityRepository.setEntityFactory(new SingletonEntityFactoryImpl());
		return singletonEntityRepository;
	}

	@Bean
	public ProcessCoreCommandEventHandler processCoreCommandEventHandler() {
		return new ProcessCoreCommandEventHandler(coreSnapshotFactory, snapshotJsonUtil, filePathConfig);
	}

	@Bean
	public InitProcessor initProcessor() {
		InitProcessor initProcessor = new InitProcessor(httpClient(), sslHttpClient(), snapshotJsonUtil,
				processCoreCommandEventHandler(), singletonEntityRepository(), applicationContext);
		initProcessor.init();
		return initProcessor;
	}

	public static void main(String[] args) {
		SpringApplication.run(QipaiChaguanApplication.class, args);
	}

}