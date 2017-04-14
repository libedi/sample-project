package kr.co.poscoict.sample.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import kr.co.poscoict.sample.common.exception.ResourceConflictException;
import kr.co.poscoict.sample.common.exception.ResourceNotFoundException;
import kr.co.poscoict.sample.framework.model.ValidMarker.Create;
import kr.co.poscoict.sample.framework.model.ValidMarker.Update;
import kr.co.poscoict.sample.user.model.User;
import kr.co.poscoict.sample.user.service.UserService;

/**
 * User Controller
 * @author libedi
 *
 */
@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * 현재 시간 테스트
	 * @return
	 */
	@RequestMapping(value = "/now", method = RequestMethod.GET)
	public String getCurrentTime() {
		return this.userService.getCurrentTime();
	}
	
	/**
	 * 사용자 전체 조회
	 * @return
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() {
		logger.info("사용자 전체 조회");
		List<User> userList = this.userService.findUsers();
		if(userList.isEmpty()) {
			throw new ResourceNotFoundException();
		}
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
	}
	
	/**
	 * 사용자 조회
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable(value = "id") String id) {
		logger.info("ID로 사용자 조회: {}", id);
		
		User user = this.userService.findUserById(id);
		if(user == null) {
			throw new ResourceNotFoundException();
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	/**
	 * 사용자 등록
	 * @param user
	 * @return
	 * @throws MethodArgumentNotValidException 
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody @Validated(Create.class) User user, UriComponentsBuilder uriBuilder) {
		logger.info("사용자 등록: {}", user);
		
		User currentUser = this.userService.findUserById(user.getId());
		if(currentUser != null) {
			logger.error("해당 ID를 사용하는 사용자가 존재하여 등록할 수 없습니다. 해당 사용자명: {}", currentUser.getName());
			throw new ResourceConflictException();
		}
		this.userService.saveUser(user);
		// 등록된 사용자 ID를 HTTP Header에 전송
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * 사용자 수정
	 * @return
	 */
	@RequestMapping(value = "/user/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
	public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody @Validated(Update.class) User user) {
		logger.info("사용자 수정. ID: {}", id);
		
		User currentUser = this.userService.findUserById(id);
		if(currentUser == null) {
			logger.error("해당 ID를 사용하는 사용자가 존재하지 않습니다. ID: {}", id);
			throw new ResourceNotFoundException();
		}
		currentUser.setName(user.getName());
		currentUser.setTelNum(user.getTelNum());
		this.userService.updateUser(currentUser);
		return new ResponseEntity<User>(currentUser, HttpStatus.OK);
	}
}