

package org.springframework.boot.test.autoconfigure.orm.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link TestEntityManager}.
 *
 * @author Phillip Webb
 */
public class TestEntityManagerTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private EntityManagerFactory entityManagerFactory;

	@Mock
	private EntityManager entityManager;

	@Mock
	private PersistenceUnitUtil persistenceUnitUtil;

	private TestEntityManager testEntityManager;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.testEntityManager = new TestEntityManager(this.entityManagerFactory);
		given(this.entityManagerFactory.getPersistenceUnitUtil())
				.willReturn(this.persistenceUnitUtil);
	}

	@Test
	public void createWhenEntityManagerIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("EntityManagerFactory must not be null");
		new TestEntityManager(null);
	}

	@Test
	public void persistAndGetIdShouldPersistAndGetId() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		given(this.persistenceUnitUtil.getIdentifier(entity)).willReturn(123);
		Object result = this.testEntityManager.persistAndGetId(entity);
		verify(this.entityManager).persist(entity);
		assertThat(result).isEqualTo(123);
	}

	@Test
	public void persistAndGetIdForTypeShouldPersistAndGetId() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		given(this.persistenceUnitUtil.getIdentifier(entity)).willReturn(123);
		Integer result = this.testEntityManager.persistAndGetId(entity, Integer.class);
		verify(this.entityManager).persist(entity);
		assertThat(result).isEqualTo(123);
	}

	@Test
	public void persistShouldPersist() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		TestEntity result = this.testEntityManager.persist(entity);
		verify(this.entityManager).persist(entity);
		assertThat(result).isSameAs(entity);
	}

	@Test
	public void persistAndFlushShouldPersistAndFlush() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		TestEntity result = this.testEntityManager.persistAndFlush(entity);
		verify(this.entityManager).persist(entity);
		verify(this.entityManager).flush();
		assertThat(result).isSameAs(entity);
	}

	@Test
	public void persistFlushFindShouldPersistAndFlushAndFind() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		TestEntity found = new TestEntity();
		given(this.persistenceUnitUtil.getIdentifier(entity)).willReturn(123);
		given(this.entityManager.find(TestEntity.class, 123)).willReturn(found);
		TestEntity result = this.testEntityManager.persistFlushFind(entity);
		verify(this.entityManager).persist(entity);
		verify(this.entityManager).flush();
		assertThat(result).isSameAs(found);
	}

	@Test
	public void mergeShouldMerge() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		given(this.entityManager.merge(entity)).willReturn(entity);
		TestEntity result = this.testEntityManager.merge(entity);
		verify(this.entityManager).merge(entity);
		assertThat(result).isSameAs(entity);
	}

	@Test
	public void removeShouldRemove() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		this.testEntityManager.remove(entity);
		verify(this.entityManager).remove(entity);
	}

	@Test
	public void findShouldFind() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		given(this.entityManager.find(TestEntity.class, 123)).willReturn(entity);
		TestEntity result = this.testEntityManager.find(TestEntity.class, 123);
		assertThat(result).isSameAs(entity);
	}

	@Test
	public void flushShouldFlush() {
		bindEntityManager();
		this.testEntityManager.flush();
		verify(this.entityManager).flush();
	}

	@Test
	public void refreshShouldRefresh() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		this.testEntityManager.refresh(entity);
		verify(this.entityManager).refresh(entity);
	}

	@Test
	public void clearShouldClear() {
		bindEntityManager();
		this.testEntityManager.clear();
		verify(this.entityManager).clear();
	}

	@Test
	public void detachShouldDetach() {
		bindEntityManager();
		TestEntity entity = new TestEntity();
		this.testEntityManager.detach(entity);
		verify(this.entityManager).detach(entity);
	}

	@Test
	public void getIdForTypeShouldGetId() {
		TestEntity entity = new TestEntity();
		given(this.persistenceUnitUtil.getIdentifier(entity)).willReturn(123);
		Integer result = this.testEntityManager.getId(entity, Integer.class);
		assertThat(result).isEqualTo(123);
	}

	@Test
	public void getIdForTypeWhenTypeIsWrongShouldThrowException() {
		TestEntity entity = new TestEntity();
		given(this.persistenceUnitUtil.getIdentifier(entity)).willReturn(123);
		this.thrown.expectMessage("ID mismatch: Object of class [java.lang.Integer] "
				+ "must be an instance of class java.lang.Long");
		this.testEntityManager.getId(entity, Long.class);
	}

	@Test
	public void getIdShouldGetId() {
		TestEntity entity = new TestEntity();
		given(this.persistenceUnitUtil.getIdentifier(entity)).willReturn(123);
		Object result = this.testEntityManager.getId(entity);
		assertThat(result).isEqualTo(123);
	}

	@Test
	public void getEntityManagerShouldGetEntityManager() {
		bindEntityManager();
		assertThat(this.testEntityManager.getEntityManager())
				.isEqualTo(this.entityManager);
	}

	@Test
	public void getEntityManagerWhenNotSetShouldThrowException() {
		this.thrown.expect(IllegalStateException.class);
		this.thrown.expectMessage("No transactional EntityManager found");
		this.testEntityManager.getEntityManager();
	}

	private void bindEntityManager() {
		EntityManagerHolder holder = new EntityManagerHolder(this.entityManager);
		TransactionSynchronizationManager.bindResource(this.entityManagerFactory, holder);
	}

	static class TestEntity {

	}

}
