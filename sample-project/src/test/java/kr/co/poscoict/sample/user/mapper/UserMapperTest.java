package kr.co.poscoict.sample.user.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import kr.co.poscoict.sample.user.model.User;

/**
 * UserMapper Test case
 * @author libedi
 *
 */
@RunWith(SpringRunner.class)
@MybatisTest
public class UserMapperTest {
	@Autowired
	private UserMapper userMapper;
	
	@Before
	public void testInsertUser() throws Exception {
		User user = new User();
		user.setId("id1234");
		user.setName("테스트");
		user.setTelNum("01012345678");
		this.userMapper.insertUser(user);
	}
	
	@Test
	public void testGetUser() throws Exception {
		String id = "id1234";
		
		List<User> actual = this.userMapper.selectUser(id);
		assertNotNull(actual);
		assertEquals(1, actual.size());
		assertEquals("테스트", actual.get(0).getName());
		assertEquals("01012345678", actual.get(0).getTelNum());
	}
}
