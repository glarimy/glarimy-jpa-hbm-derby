package com.glarimy.jpa;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Author {
	@EmbeddedId
	private Name name;
	private String email;

	public Author() {
	}

	public Author(Name name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "Author [name=" + name + ", email=" + email + "]";
	}

}
