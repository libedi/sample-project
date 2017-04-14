package kr.co.poscoict.sample.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.poscoict.sample.user.model.User;

/**
 * User Mapper
 * @author libedi
 *
 */
@Mapper
public interface UserMapper {
	String selectNow();

	void insertUser(User user);

	List<User> selectUser(String id);

	void updateUser(User user);
	
	void deleteUser(User user);
}
