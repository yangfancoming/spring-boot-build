

package org.springframework.boot.actuate.audit;

import java.util.Collections;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AuditEvent}.
 *
 * @author Dave Syer
 * @author Vedran Pavic
 */
public class AuditEventTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void nowEvent() {
		AuditEvent event = new AuditEvent("phil", "UNKNOWN",
				Collections.singletonMap("a", (Object) "b"));
		assertThat(event.getData().get("a")).isEqualTo("b");
		assertThat(event.getType()).isEqualTo("UNKNOWN");
		assertThat(event.getPrincipal()).isEqualTo("phil");
		assertThat(event.getTimestamp()).isNotNull();
	}

	@Test
	public void convertStringsToData() {
		AuditEvent event = new AuditEvent("phil", "UNKNOWN", "a=b", "c=d");
		assertThat(event.getData().get("a")).isEqualTo("b");
		assertThat(event.getData().get("c")).isEqualTo("d");
	}

	@Test
	public void nullPrincipalIsMappedToEmptyString() {
		AuditEvent auditEvent = new AuditEvent(null, "UNKNOWN",
				Collections.singletonMap("a", (Object) "b"));
		assertThat(auditEvent.getPrincipal()).isEmpty();
	}

	@Test
	public void nullTimestamp() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Timestamp must not be null");
		new AuditEvent(null, "phil", "UNKNOWN",
				Collections.singletonMap("a", (Object) "b"));
	}

	@Test
	public void nullType() {
		this.thrown.expect(IllegalArgumentException.class);
		this.thrown.expectMessage("Type must not be null");
		new AuditEvent("phil", null, Collections.singletonMap("a", (Object) "b"));
	}

	@Test
	public void jsonFormat() throws Exception {
		AuditEvent event = new AuditEvent("johannes", "UNKNOWN",
				Collections.singletonMap("type", (Object) "BadCredentials"));
		String json = Jackson2ObjectMapperBuilder.json().build()
				.writeValueAsString(event);
		JSONObject jsonObject = new JSONObject(json);
		assertThat(jsonObject.getString("type")).isEqualTo("UNKNOWN");
		assertThat(jsonObject.getJSONObject("data").getString("type"))
				.isEqualTo("BadCredentials");
	}

}
