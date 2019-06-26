

package org.springframework.boot.actuate.audit.listener;

import java.util.Collections;

import org.junit.Test;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link AuditListener}.
 *
 * @author Phillip Webb
 */
public class AuditListenerTests {

	@Test
	public void testStoredEvents() {
		AuditEventRepository repository = mock(AuditEventRepository.class);
		AuditEvent event = new AuditEvent("principal", "type", Collections.emptyMap());
		AuditListener listener = new AuditListener(repository);
		listener.onApplicationEvent(new AuditApplicationEvent(event));
		verify(repository).add(event);
	}

}
