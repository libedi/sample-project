package kr.co.poscoict.sample.user.model;

import org.hibernate.validator.constraints.NotEmpty;

import kr.co.poscoict.sample.framework.model.ValidMarker;

/**
 * User
 * @author libedi
 *
 */
public class User extends ValidMarker {
	@NotEmpty(groups = {Create.class, Retrieve.class, Delete.class})
	private String id;
	@NotEmpty(groups = {Create.class})
	private String name;
	@NotEmpty(groups = {Create.class})
	private String telNum;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [");
		if (id != null)
			builder.append("id=").append(id).append(", ");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (telNum != null)
			builder.append("telNum=").append(telNum);
		builder.append("]");
		return builder.toString();
	}
}
