

package org.springframework.boot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sampleconfig.MyComponentInPackageWithoutDot;

import org.springframework.boot.sampleconfig.MyComponent;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link BeanDefinitionLoader}.
 *
 * @author Phillip Webb
 */
public class BeanDefinitionLoaderTests {

	private StaticApplicationContext registry;

	@Before
	public void setup() {
		this.registry = new StaticApplicationContext();
	}

	@After
	public void cleanUp() {
		this.registry.close();
	}

	@Test
	public void loadClass() {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

	@Test
	public void loadXmlResource() {
		ClassPathResource resource = new ClassPathResource("sample-beans.xml",
				getClass());
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry, resource);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myXmlComponent")).isTrue();

	}

	@Test
	public void loadGroovyResource() {
		ClassPathResource resource = new ClassPathResource("sample-beans.groovy",
				getClass());
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry, resource);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myGroovyComponent")).isTrue();

	}

	@Test
	public void loadGroovyResourceWithNamespace() {
		ClassPathResource resource = new ClassPathResource("sample-namespace.groovy",
				getClass());
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry, resource);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myGroovyComponent")).isTrue();

	}

	@Test
	public void loadPackage() {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class.getPackage());
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

	@Test
	public void loadClassName() {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class.getName());
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

	@Test
	public void loadResourceName() {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				"classpath:org/springframework/boot/sample-beans.xml");
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myXmlComponent")).isTrue();
	}

	@Test
	public void loadGroovyName() {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				"classpath:org/springframework/boot/sample-beans.groovy");
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myGroovyComponent")).isTrue();
	}

	@Test
	public void loadPackageName() {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class.getPackage().getName());
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

	@Test
	public void loadPackageNameWithoutDot() {
		// See gh-6126
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponentInPackageWithoutDot.class.getPackage().getName());
		int loaded = loader.load();
		assertThat(loaded).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponentInPackageWithoutDot")).isTrue();
	}

	@Test
	public void loadPackageAndClassDoesNotDoubleAdd() {
		BeanDefinitionLoader loader = new BeanDefinitionLoader(this.registry,
				MyComponent.class.getPackage(), MyComponent.class);
		assertThat(loader.load()).isEqualTo(1);
		assertThat(this.registry.containsBean("myComponent")).isTrue();
	}

}
