

package org.springframework.boot.actuate.system;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Tests for {@link DiskSpaceHealthIndicator}.
 *
 * @author Mattias Severson
 */
public class DiskSpaceHealthIndicatorTests {

	static final long THRESHOLD_BYTES = 1024;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private File fileMock;

	private HealthIndicator healthIndicator;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		given(this.fileMock.exists()).willReturn(true);
		given(this.fileMock.canRead()).willReturn(true);
		this.healthIndicator = new DiskSpaceHealthIndicator(this.fileMock,
				THRESHOLD_BYTES);
	}

	@Test
	public void diskSpaceIsUp() {
		given(this.fileMock.getUsableSpace()).willReturn(THRESHOLD_BYTES + 10);
		given(this.fileMock.getTotalSpace()).willReturn(THRESHOLD_BYTES * 10);
		Health health = this.healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
		assertThat(health.getDetails().get("threshold")).isEqualTo(THRESHOLD_BYTES);
		assertThat(health.getDetails().get("free")).isEqualTo(THRESHOLD_BYTES + 10);
		assertThat(health.getDetails().get("total")).isEqualTo(THRESHOLD_BYTES * 10);
	}

	@Test
	public void diskSpaceIsDown() {
		given(this.fileMock.getUsableSpace()).willReturn(THRESHOLD_BYTES - 10);
		given(this.fileMock.getTotalSpace()).willReturn(THRESHOLD_BYTES * 10);
		Health health = this.healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
		assertThat(health.getDetails().get("threshold")).isEqualTo(THRESHOLD_BYTES);
		assertThat(health.getDetails().get("free")).isEqualTo(THRESHOLD_BYTES - 10);
		assertThat(health.getDetails().get("total")).isEqualTo(THRESHOLD_BYTES * 10);
	}

}
