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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import kr.co.poscoict.sample.common.exception.ResourceConflictException;
import kr.co.poscoict.sample.common.exception.ResourceNotFoundException;
import kr.co.poscoict.sample.common.util.MessageSourceUtil;
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
	
	@Autowired
	private MessageSourceUtil messageSource;
	
	/**
	 * 현재 시간 테스트
	 * @return
	 */
	@GetMapping(path = "/now")
	@ResponseStatus(HttpStatus.OK)
	public String getCurrentTime() {
		return this.userService.getCurrentTime();
	}
	
	/**
	 * 사용자 전체 조회
	 * @return
	 */
	@GetMapping(path = "/user")
	@ResponseStatus(HttpStatus.OK)
	public List<User> getUsers() {
		logger.info("사용자 전체 조회");
		
		List<User> userList = this.userService.findUsers();
		if(userList.isEmpty()) {
			throw new ResourceNotFoundException(this.messageSource.getMessage("user.noExists.all"));
		}
		return userList;
	}
	
	/**
	 * 사용자 조회
	 * @param id
	 * @return
	 */
	@GetMapping(path = "/user/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User getUser(@PathVariable(value = "id") String id) {
		logger.info("ID로 사용자 조회: {}", id);
		
		User user = this.userService.findUserById(id);
		if(user == null) {
			throw new ResourceNotFoundException(this.messageSource.getMessage("user.noExists.id", id));
		}
		return user;
	}
	
	/**
	 * 사용자 등록
	 * @param user
	 * @return
	 * @throws MethodArgumentNotValidException 
	 */
	@PostMapping(path = "/user")
	public ResponseEntity<Void> createUser(@RequestBody @Validated(Create.class) User user, UriComponentsBuilder uriBuilder) {
		logger.info("사용자 등록: {}", user);
		
		User currentUser = this.userService.findUserById(user.getId());
		if(currentUser != null) {
			throw new ResourceConflictException(this.messageSource.getMessage("user.conflict", currentUser.getName()));
		}
		this.userService.saveUser(user);
		// 등록된 사용자 ID를 HTTP Header의 Location 필드에 전송
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * 사용자 수정
	 * @return
	 */
	@RequestMapping(value = "/user/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
	@ResponseStatus(HttpStatus.OK)
	public User updateUser(@PathVariable String id, @RequestBody @Validated(Update.class) User user) {
		logger.info("사용자 수정. ID: {}", id);
		
		User currentUser = this.userService.findUserById(id);
		if(currentUser == null) {
			throw new ResourceNotFoundException(this.messageSource.getMessage("user.noExists.id", id));
		}
		currentUser.setName(user.getName());
		currentUser.setTelNum(user.getTelNum());
		this.userService.updateUser(currentUser);
		return currentUser;
	}
	
	/**
	 * 사용자 전체 삭제
	 * @return
	 */
	@DeleteMapping(path = "/user")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUsers() {
		logger.info("사용자 전체 삭제");
		
		this.userService.deleteUsers();
	}
	
	/**
	 * 사용자 삭제
	 * @param id
	 * @return
	 */
	@DeleteMapping(path = "/user/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable String id) {
		logger.info("사용자  삭제. id : {}", id);
		
		User currentUser = this.userService.findUserById(id);
		if(currentUser == null) {
			throw new ResourceNotFoundException(this.messageSource.getMessage("user.noExists.id", id));
		}
		this.userService.deleteUser(id);
	}
}