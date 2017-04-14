package kr.co.poscoict.sample.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.poscoict.sample.user.mapper.UserMapper;
import kr.co.poscoict.sample.user.model.User;

/**
 * User Service
 * @author libedi
 *
 */
@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;

	public String getCurrentTime() {
		return this.userMapper.selectNow();
	}

	public List<User> findUsers() {
		return this.userMapper.selectUser(null);
	}

	public User findUserById(String id) {
		List<User> userList = this.userMapper.selectUser(id);
		return userList.isEmpty() ? null : userList.get(0); 
	}
	
	public void saveUser(User user) {
		this.userMapper.insertUser(user);
	}

	public void updateUser(User user) {
		this.userMapper.updateUser(user);
	}
}