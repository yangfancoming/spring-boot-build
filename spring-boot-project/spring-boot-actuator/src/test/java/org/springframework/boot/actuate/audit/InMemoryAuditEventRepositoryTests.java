

package org.springframework.boot.actuate.audit;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link InMemoryAuditEventRepository}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @author Vedran Pavic
 */
public class InMemoryAuditEventRepositoryTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void lessThanCapacity() {
		InMemoryAuditEventRepository repository = new InMemoryAuditEventRepository();
		repository.add(new AuditEvent("dave", "a"));
		repository.add(new AuditEvent("dave", "b"));
		List<AuditEvent> events = repository.find("dave", null, null);
		assertThat(events.size()).isEqualTo(2);
		assertThat(events.get(0).getType()).isEqualTo("a");
		assertThat(events.get(1).getType()).isEqualTo("b");
	}

	@Test
	public void capacity() {
		InMemoryAuditEventRepository repository = new InMemoryAuditEventRepository(2);
		repository.add(new AuditEvent("dave", "a"));
		repository.add(new AuditEvent("dave", "b"));
		repository.add(new AuditEvent("dave", "c"));
		List<AuditEvent> events = repository.find("dave", null, null);
		assertThat(events.size()).isEqualTo(2);
		assertThat(events.get(0).getType()).isEqualTo("b");
		assertThat(events.get(1).getType()).isEqualTo("c");
	}

	@Test
	public void addNullAuditEvent() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("AuditEvent must not be null");
		InMemoryAuditEventRepository repository = new InMemoryAuditEventRepository();
		repository.add(null);
	}

	@Test
	public void findByPrincipal() {
		InMemoryAuditEventRepository repository = new InMemoryAuditEventRepository();
		repository.add(new AuditEvent("dave", "a"));
		repository.add(new AuditEvent("phil", "b"));
		repository.add(new AuditEvent("dave", "c"));
		repository.add(new AuditEvent("phil", "d"));
		List<AuditEvent> events = repository.find("dave", null, null);
		assertThat(events.size()).isEqualTo(2);
		assertThat(events.get(0).getType()).isEqualTo("a");
		assertThat(events.get(1).getType()).isEqualTo("c");
	}

	@Test
	public void findByPrincipalAndType() {
		InMemoryAuditEventRepository repository = new InMemoryAuditEventRepository();
		repository.add(new AuditEvent("dave", "a"));
		repository.add(new AuditEvent("phil", "b"));
		repository.add(new AuditEvent("dave", "c"));
		repository.add(new AuditEvent("phil", "d"));
		List<AuditEvent> events = repository.find("dave", null, "a");
		assertThat(events.size()).isEqualTo(1);
		assertThat(events.get(0).getPrincipal()).isEqualTo("dave");
		assertThat(events.get(0).getType()).isEqualTo("a");
	}

	@Test
	public void findByDate() {
		Instant instant = Instant.now();
		Map<String, Object> data = new HashMap<>();
		InMemoryAuditEventRepository repository = new InMemoryAuditEventRepository();
		repository.add(new AuditEvent(instant, "dave", "a", data));
		repository
				.add(new AuditEvent(instant.plus(1, ChronoUnit.DAYS), "phil", "b", data));
		repository
				.add(new AuditEvent(instant.plus(2, ChronoUnit.DAYS), "dave", "c", data));
		repository
				.add(new AuditEvent(instant.plus(3, ChronoUnit.DAYS), "phil", "d", data));
		Instant after = instant.plus(1, ChronoUnit.DAYS);
		List<AuditEvent> events = repository.find(null, after, null);
		assertThat(events.size()).isEqualTo(2);
		assertThat(events.get(0).getType()).isEqualTo("c");
		assertThat(events.get(1).getType()).isEqualTo("d");
		events = repository.find("dave", after, null);
		assertThat(events.size()).isEqualTo(1);
		assertThat(events.get(0).getType()).isEqualTo("c");
	}

}
