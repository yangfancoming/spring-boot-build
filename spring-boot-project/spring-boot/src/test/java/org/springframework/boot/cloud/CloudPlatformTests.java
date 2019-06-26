

package org.springframework.boot.cloud;

import org.junit.Test;

import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CloudPlatform}.
 *
 * @author Phillip Webb
 */
public class CloudPlatformTests {

	@Test
	public void getActiveWhenEnvironmentIsNullShouldReturnNull() {
		CloudPlatform platform = CloudPlatform.getActive(null);
		assertThat(platform).isNull();
	}

	@Test
	public void getActiveWhenNotInCloudShouldReturnNull() {
		Environment environment = new MockEnvironment();
		CloudPlatform platform = CloudPlatform.getActive(environment);
		assertThat(platform).isNull();

	}

	@Test
	public void getActiveWhenHasVcapApplicationShouldReturnCloudFoundry() {
		Environment environment = new MockEnvironment().withProperty("VCAP_APPLICATION",
				"---");
		CloudPlatform platform = CloudPlatform.getActive(environment);
		assertThat(platform).isEqualTo(CloudPlatform.CLOUD_FOUNDRY);
		assertThat(platform.isActive(environment)).isTrue();
	}

	@Test
	public void getActiveWhenHasVcapServicesShouldReturnCloudFoundry() {
		Environment environment = new MockEnvironment().withProperty("VCAP_SERVICES",
				"---");
		CloudPlatform platform = CloudPlatform.getActive(environment);
		assertThat(platform).isEqualTo(CloudPlatform.CLOUD_FOUNDRY);
		assertThat(platform.isActive(environment)).isTrue();
	}

	@Test
	public void getActiveWhenHasDynoShouldReturnHeroku() {
		Environment environment = new MockEnvironment().withProperty("DYNO", "---");
		CloudPlatform platform = CloudPlatform.getActive(environment);
		assertThat(platform).isEqualTo(CloudPlatform.HEROKU);
		assertThat(platform.isActive(environment)).isTrue();
	}

	@Test
	public void getActiveWhenHasHcLandscapeShouldReturnSap() {
		Environment environment = new MockEnvironment().withProperty("HC_LANDSCAPE",
				"---");
		CloudPlatform platform = CloudPlatform.getActive(environment);
		assertThat(platform).isEqualTo(CloudPlatform.SAP);
		assertThat(platform.isActive(environment)).isTrue();
	}

}
