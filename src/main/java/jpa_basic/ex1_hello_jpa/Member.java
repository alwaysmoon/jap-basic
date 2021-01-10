package jpa_basic.ex1_hello_jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//@Entity(name = "Member")
 @Table(name = "USER")
public class Member {
	
	@Id
	private Long id;
	
//	@Column(name = "username")
	private String name;
	
	public Member() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
