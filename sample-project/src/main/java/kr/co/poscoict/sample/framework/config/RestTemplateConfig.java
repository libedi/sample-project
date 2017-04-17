package kr.co.poscoict.sample.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate Configuration
 * @author libedi
 *
 */
@Configuration
public class RestTemplateConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(clientHttpRequestFactory());
	}
	
	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory() {
//		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		/*
		 * SimpleClientHttpRequestFactory 는 기본적으로 PATCH 메서드를 지원하지 않는다.
		 * PATCH 메서드를 사용하기 위해서는 아래 HttpComponentsClientHttpRequestFactory 를 사용한다. - httpclient 추가
		 */
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setReadTimeout(1000 * 2);	// 2초
		clientHttpRequestFactory.setConnectTimeout(1000 * 2);
		return clientHttpRequestFactory;
	}
}