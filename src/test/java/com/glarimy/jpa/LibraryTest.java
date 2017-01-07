package com.glarimy.jpa;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;

import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LibraryTest {
	static EntityManagerFactory factory;
	EntityManager em;

	@Before
	public void before() {
		em = factory.createEntityManager();
		em.getTransaction().begin();
	}

	@After
	public void after() {
		if (em.isOpen() && em.getTransaction().isActive())
			em.getTransaction().commit();
		if (em.isOpen())
			em.close();
	}

	@BeforeClass
	public static void init() {
		factory = Persistence.createEntityManagerFactory("lib");
		Author krishna = new Author(new Name("Krishna", "Koyya"), "krishna@glarimy.com");
		Author mohan = new Author(new Name("Mohan", "Koyya"), "krishna@glarimy.com");
		Book book = new Book(1972, "Java Design Patterns", 250, true,
				new Publisher("GTS", new Address("Ayodhya Ramayya Nagar", "Tadepalligudem", 534101)));
		book.authors.add(krishna);
		TextBook textbook = new TextBook(1987, "Object Oriented Design Patterns", 200, true,
				new Publisher("GTS", new Address("Ayodhya Ramayya Nagar", "Tadepalligudem", 534101)),
				"Design Patterns");
		textbook.authors.add(mohan);

		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em.persist(book);
		em.persist(textbook);
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testFindingBooks() {
		Book book = em.find(Book.class, 1972);
		assertTrue(book != null);
	}

	@Test
	public void testUsingCompoundKey() {
		Author krishna = em.find(Author.class, new Name("Krishna", "Koyya"));
		assertTrue(krishna.getEmail().equals("krishna@glarimy.com"));
	}

	@Test
	public void testLazyFetch() {
		try {
			TextBook textbook = em.find(TextBook.class, 1987);
			em.close();
			textbook.getAuthors().get(0).getName();
			fail("it shouldn't have executed");
		} catch (LazyInitializationException lie) {
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUsingQueryAndPolymorphism() {
		List<Book> books = em.createQuery("from Book").getResultList();
		assertTrue(books.size() == 2);
		assertTrue(books.get(0).publisher != books.get(1).publisher);
		books.stream().forEach((b) -> {
			if (b.isbn == 1972)
				assertTrue(b instanceof Book);
			else
				assertTrue(b instanceof TextBook);
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUsingRestrictions() {
		List<Book> books = em
				.createQuery(
						"from Book b where b.price > ? and b.publisher.name=:name and b.availability=:availability")
				.setParameter("name", "GTS").setParameter("availability", true).setParameter(0, 225.0).getResultList();
		assertTrue(books.size() == 1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUsingProjections() {
		List<Object[]> records = em.createQuery("select p.name, p.address.city, p.address.pincode from Publisher p")
				.getResultList();
		assertTrue(records.size() == 2);
		assertTrue(records.get(0).length == 3);
		records.stream().forEach((r) -> {
			assertTrue(((String) r[1]).equals("Tadepalligudem"));
			assertTrue(((int) r[2]) == 534101);
		});
	}

	@Test
	public void testUsingJoinQuery() {
		Name name = (Name) em.createQuery("select a.name from Book b join b.authors a where b.isbn = :isbn")
				.setParameter("isbn", 1972).getSingleResult();
		assertTrue(name.getFirstName().equals("Krishna"));
	}

	@Test
	public void testUsingNamedQuery() {
		long count = (long) em.createNamedQuery("bookcount").setParameter("publisher", "GTS").getSingleResult();
		assertTrue(count == 2);
	}

	@Test
	public void testUsingNamedNativeQuery() {
		@SuppressWarnings("unchecked")
		List<Book> books = em.createNamedQuery("patternsbooks").setParameter(0, "%Patterns%").getResultList();
		assertTrue(books.size() == 2);
	}

	@Test
	public void testUsingLocks() {
		Book book = em.find(Book.class, 1972, LockModeType.OPTIMISTIC);

		EntityManager otherem = factory.createEntityManager();
		otherem.getTransaction().begin();
		Book otherbook = otherem.find(Book.class, 1972);
		otherbook.price += 100;
		otherem.getTransaction().commit();

		book.price += 100;
		try {
			em.getTransaction().commit();
			fail("Commit shouldn't have succeeded");
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}
}