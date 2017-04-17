package kr.co.poscoict.sample.user.api;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.poscoict.sample.user.model.User;

/**
 * REST API Test case - User
 * @author libedi
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles(profiles = {"local"})
public class UserApiServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(UserApiServiceTest.class);

	private final String REST_SERVICE_URL = "http://localhost:8882";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void test1Now() throws Exception {
		String now = this.restTemplate.getForObject(this.REST_SERVICE_URL + "/now", String.class);
		assertNotNull(now);
	}
	
	@Test
	public void test2CreateUser() throws Exception {
		assertNotNull(restTemplate);
		
		User user = new User();
		user.setId("A1234");
		user.setName("테스터");
		user.setTelNum("01012345678");
		URI newUri = this.restTemplate.postForLocation(this.REST_SERVICE_URL + "/users", user);
		assertNotNull(newUri);
		logger.info("NEW URI : {}", newUri.toASCIIString());
	}
	
	@Test
	public void test3GetUsers() throws Exception {
		@SuppressWarnings("unchecked")
		List<LinkedHashMap<String, Object>> userList = this.restTemplate.getForObject(this.REST_SERVICE_URL + "/users", List.class);
		assertNotNull(userList);
		for(Map<String, Object> userMap : userList) {
			User user = this.objectMapper.convertValue(userMap, User.class);
			logger.info("Current User : {}", user.toString());
		}
	}
	
	@Test
	public void test4GetUser() throws Exception {
		User user = this.restTemplate.getForObject(this.REST_SERVICE_URL + "/users/A1234", User.class);
		assertNotNull(user);
		logger.info("Current User : {}", user.toString());
	}
	
	@Test
	public void test5UpdateUserByPut() throws Exception {
		User user = new User();
		user.setName("테스트1");
		user.setTelNum("01012345679");
		// 1. put() 메소드 사용 - 반환값을 알 수가 없다.
//		this.restTemplate.put(this.REST_SERVICE_URL + "/users/A1234", user);
		// 2. exchange() 메서드 사용 - 반환값을 받을 수 있다.
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		ResponseEntity<User> response = this.restTemplate.exchange(REST_SERVICE_URL + "/users/A1234", HttpMethod.PUT, new HttpEntity<User>(user, headers), User.class);
		assertNotNull(response);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		logger.info("Update User : {}", response.getBody().toString());
	}
	
	@Test
	public void test6UpdateUserByPatch() throws Exception {
		User user = new User();
		user.setName("테스트2");
		user.setTelNum("01012345670");
		// RestTemplate 에서는 기본적으로 PATCH 메서드를 지원하지 않는다. 따라서 exchange() 메서드를 사용.
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		ResponseEntity<User> response = this.restTemplate.exchange(REST_SERVICE_URL + "/users/A1234", HttpMethod.PATCH, new HttpEntity<User>(user, headers), User.class);
		assertNotNull(response);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		logger.info("Update User : {}", response.getBody().toString());
	}
}