

package org.springframework.boot.test.autoconfigure.web.reactive;

import org.springframework.boot.test.context.ReactiveWebMergedContextConfiguration;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContextBootstrapper;

/**
 * {@link TestContextBootstrapper} for {@link WebFluxTest @WebFluxTest} support.
 */
class WebFluxTestContextBootstrapper extends SpringBootTestContextBootstrapper {

	@Override
	protected MergedContextConfiguration processMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
		return new ReactiveWebMergedContextConfiguration(super.processMergedContextConfiguration(mergedConfig));
	}

}
