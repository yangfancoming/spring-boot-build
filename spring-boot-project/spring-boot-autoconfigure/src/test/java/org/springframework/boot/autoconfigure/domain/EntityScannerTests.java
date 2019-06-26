

package org.springframework.boot.autoconfigure.domain;

import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.boot.autoconfigure.domain.scan.a.EmbeddableA;
import org.springframework.boot.autoconfigure.domain.scan.a.EntityA;
import org.springframework.boot.autoconfigure.domain.scan.b.EmbeddableB;
import org.springframework.boot.autoconfigure.domain.scan.b.EntityB;
import org.springframework.boot.autoconfigure.domain.scan.c.EmbeddableC;
import org.springframework.boot.autoconfigure.domain.scan.c.EntityC;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EntityScanner}.
 *
 * @author Phillip Webb
 */
public class EntityScannerTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void createWhenContextIsNullShouldThrowException() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Context must not be null");
		new EntityScanner(null);
	}

	@Test
	public void scanShouldScanFromSinglePackage() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ScanConfig.class);
		EntityScanner scanner = new EntityScanner(context);
		Set<Class<?>> scanned = scanner.scan(Entity.class);
		assertThat(scanned).containsOnly(EntityA.class, EntityB.class, EntityC.class);
		context.close();
	}

	@Test
	public void scanShouldScanFromMultiplePackages() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ScanAConfig.class, ScanBConfig.class);
		EntityScanner scanner = new EntityScanner(context);
		Set<Class<?>> scanned = scanner.scan(Entity.class);
		assertThat(scanned).containsOnly(EntityA.class, EntityB.class);
		context.close();
	}

	@Test
	public void scanShouldFilterOnAnnotation() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ScanConfig.class);
		EntityScanner scanner = new EntityScanner(context);
		assertThat(scanner.scan(Entity.class)).containsOnly(EntityA.class, EntityB.class,
				EntityC.class);
		assertThat(scanner.scan(Embeddable.class)).containsOnly(EmbeddableA.class,
				EmbeddableB.class, EmbeddableC.class);
		assertThat(scanner.scan(Entity.class, Embeddable.class)).containsOnly(
				EntityA.class, EntityB.class, EntityC.class, EmbeddableA.class,
				EmbeddableB.class, EmbeddableC.class);
		context.close();
	}

	@Configuration
	@EntityScan("org.springframework.boot.autoconfigure.domain.scan")
	static class ScanConfig {

	}

	@Configuration
	@EntityScan(basePackageClasses = EntityA.class)
	static class ScanAConfig {

	}

	@Configuration
	@EntityScan(basePackageClasses = EntityB.class)
	static class ScanBConfig {

	}

}
