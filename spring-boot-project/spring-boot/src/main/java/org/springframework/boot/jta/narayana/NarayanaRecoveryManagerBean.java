

package org.springframework.boot.jta.narayana;

import com.arjuna.ats.internal.jta.recovery.arjunacore.XARecoveryModule;
import com.arjuna.ats.jbossatx.jta.RecoveryManagerService;
import com.arjuna.ats.jta.recovery.XAResourceRecoveryHelper;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Bean to set up Narayana recovery manager.
 *
 * @author Gytis Trikleris
 * @since 1.4.0
 */
public class NarayanaRecoveryManagerBean implements InitializingBean, DisposableBean {

	private final RecoveryManagerService recoveryManagerService;

	public NarayanaRecoveryManagerBean(RecoveryManagerService recoveryManagerService) {
		Assert.notNull(recoveryManagerService, "RecoveryManagerService must not be null");
		this.recoveryManagerService = recoveryManagerService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.recoveryManagerService.create();
		this.recoveryManagerService.start();
	}

	@Override
	public void destroy() throws Exception {
		this.recoveryManagerService.stop();
		this.recoveryManagerService.destroy();
	}

	void registerXAResourceRecoveryHelper(
			XAResourceRecoveryHelper xaResourceRecoveryHelper) {
		getXARecoveryModule().addXAResourceRecoveryHelper(xaResourceRecoveryHelper);
	}

	private XARecoveryModule getXARecoveryModule() {
		XARecoveryModule xaRecoveryModule = XARecoveryModule
				.getRegisteredXARecoveryModule();
		if (xaRecoveryModule != null) {
			return xaRecoveryModule;
		}
		throw new IllegalStateException(
				"XARecoveryModule is not registered with recovery manager");
	}

}
