

package org.springframework.boot.test.autoconfigure.web.servlet;

import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContextBootstrapper;
import org.springframework.test.context.web.WebMergedContextConfiguration;

/**
 * {@link TestContextBootstrapper} for {@link WebMvcTest @WebMvcTest} support.
 *
 * @author Phillip Webb
 */
class WebMvcTestContextBootstrapper extends SpringBootTestContextBootstrapper {

	@Override
	protected MergedContextConfiguration processMergedContextConfiguration(MergedContextConfiguration mergedConfig) {
		return new WebMergedContextConfiguration(super.processMergedContextConfiguration(mergedConfig), "");
	}

}
