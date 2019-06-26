

package org.springframework.boot.jta.narayana;

import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link NarayanaRecoveryManagerBean}.
 *
 * @author Gytis Trikleris
 */
public class NarayanaRecoveryManagerBeanTests {

	private RecoveryManagerService service;

	private NarayanaRecoveryManagerBean recoveryManager;

	@Before
	public void before() {
		this.service = mock(RecoveryManagerService.class);
		this.recoveryManager = new NarayanaRecoveryManagerBean(this.service);
	}

	@Test
	public void shouldCreateAndStartRecoveryManagerService() throws Exception {
		this.recoveryManager.afterPropertiesSet();
		verify(this.service, times(1)).create();
		verify(this.service, times(1)).start();
	}

	@Test
	public void shouldStopAndDestroyRecoveryManagerService() throws Exception {
		this.recoveryManager.destroy();
		verify(this.service, times(1)).stop();
		verify(this.service, times(1)).destroy();
	}

}
