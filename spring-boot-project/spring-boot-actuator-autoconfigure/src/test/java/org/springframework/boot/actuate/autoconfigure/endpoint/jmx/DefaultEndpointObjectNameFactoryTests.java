

package org.springframework.boot.actuate.autoconfigure.endpoint.jmx;

import java.util.Collections;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.junit.Test;

import org.springframework.boot.actuate.endpoint.jmx.ExposableJmxEndpoint;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.util.ObjectUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link DefaultEndpointObjectNameFactory}.
 *
 * @author Stephane Nicoll
 */
public class DefaultEndpointObjectNameFactoryTests {

	private final MockEnvironment environment = new MockEnvironment();

	private final JmxEndpointProperties properties = new JmxEndpointProperties(
			this.environment);

	private final MBeanServer mBeanServer = mock(MBeanServer.class);

	private String contextId;

	@Test
	public void generateObjectName() {
		ObjectName objectName = generateObjectName(endpoint("Test"));
		assertThat(objectName.toString())
				.isEqualTo("org.springframework.boot:type=Endpoint,name=Test");
	}

	@Test
	public void generateObjectNameWithCapitalizedId() {
		ObjectName objectName = generateObjectName(endpoint("test"));
		assertThat(objectName.toString())
				.isEqualTo("org.springframework.boot:type=Endpoint,name=Test");
	}

	@Test
	public void generateObjectNameWithCustomDomain() {
		this.properties.setDomain("com.example.acme");
		ObjectName objectName = generateObjectName(endpoint("test"));
		assertThat(objectName.toString())
				.isEqualTo("com.example.acme:type=Endpoint,name=Test");
	}

	@Test
	public void generateObjectNameWithUniqueNames() {
		this.properties.setUniqueNames(true);
		ExposableJmxEndpoint endpoint = endpoint("test");
		String id = ObjectUtils.getIdentityHexString(endpoint);
		ObjectName objectName = generateObjectName(endpoint);
		assertThat(objectName.toString()).isEqualTo(
				"org.springframework.boot:type=Endpoint,name=Test,identity=" + id);
	}

	@Test
	public void generateObjectNameWithStaticNames() {
		this.properties.getStaticNames().setProperty("counter", "42");
		this.properties.getStaticNames().setProperty("foo", "bar");
		ObjectName objectName = generateObjectName(endpoint("test"));
		assertThat(objectName.getKeyProperty("counter")).isEqualTo("42");
		assertThat(objectName.getKeyProperty("foo")).isEqualTo("bar");
		assertThat(objectName.toString())
				.startsWith("org.springframework.boot:type=Endpoint,name=Test,");
	}

	@Test
	public void generateObjectNameWithDuplicate() throws MalformedObjectNameException {
		this.contextId = "testContext";
		given(this.mBeanServer.queryNames(
				new ObjectName("org.springframework.boot:type=Endpoint,name=Test,*"),
				null)).willReturn(
						Collections.singleton(new ObjectName(
								"org.springframework.boot:type=Endpoint,name=Test")));
		ObjectName objectName = generateObjectName(endpoint("test"));
		assertThat(objectName.toString()).isEqualTo(
				"org.springframework.boot:type=Endpoint,name=Test,context=testContext");

	}

	private ObjectName generateObjectName(ExposableJmxEndpoint endpoint) {
		try {
			return new DefaultEndpointObjectNameFactory(this.properties, this.mBeanServer,
					this.contextId).getObjectName(endpoint);
		}
		catch (MalformedObjectNameException ex) {
			throw new AssertionError("Invalid object name", ex);
		}
	}

	private ExposableJmxEndpoint endpoint(String id) {
		ExposableJmxEndpoint endpoint = mock(ExposableJmxEndpoint.class);
		given(endpoint.getId()).willReturn(id);
		return endpoint;
	}

}
