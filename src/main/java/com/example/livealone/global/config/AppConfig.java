package com.example.livealone.global.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate restTemplate() {
		PoolingHttpClientConnectionManager connManager = PoolingHttpClientConnectionManagerBuilder.create()
			.setMaxConnTotal(200)
			.setMaxConnPerRoute(20)
			.build();

		RequestConfig requestConfig = RequestConfig.custom()
			.setConnectTimeout(Timeout.ofMilliseconds(30000)) // 연결 타임아웃 설정 (30초)
			.setResponseTimeout(Timeout.ofMilliseconds(30000)) // 응답 타임아웃 설정 (30초)
			.build();

		CloseableHttpClient httpClient = HttpClients.custom()
			.setConnectionManager(connManager)
			.setDefaultRequestConfig(requestConfig)
			.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

		return new RestTemplate(factory);
	}
}
