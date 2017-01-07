package com.glarimy.jpa;

import javax.persistence.Entity;

@Entity
public class TextBook extends Book {
	protected String course;

	public TextBook() {

	}

	public TextBook(int isbn, String title, double price, boolean availability, Publisher publisher, String course) {
		super(isbn, title, price, availability, publisher);
		this.course = course;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return "TextBook [course=" + course + "]";
	}

}
