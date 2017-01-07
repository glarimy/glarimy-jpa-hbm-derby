package com.glarimy.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NamedQueries({
		@NamedQuery(name = "bookcount", query = "select count(b) from Book b where b.publisher.name=:publisher") })
@NamedNativeQueries({
		@NamedNativeQuery(name = "patternsbooks", query = "select * from book b where b.title like ?", resultClass = Book.class) })
public class Book {
	@Id
	protected int isbn;
	protected String title;
	protected double price;
	protected boolean availability;
	@OneToOne(cascade = CascadeType.ALL)
	protected Publisher publisher;
	@OneToMany(cascade = CascadeType.ALL)
	protected List<Author> authors = new ArrayList<>();
	@Version
	protected int version;

	public Book() {

	}

	public Book(int isbn, String title, double price, boolean availability, Publisher publisher) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.price = price;
		this.availability = availability;
		this.publisher = publisher;
	}

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + "]";
	}

}