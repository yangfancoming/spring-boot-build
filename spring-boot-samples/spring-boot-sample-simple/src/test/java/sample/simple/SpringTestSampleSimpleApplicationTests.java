

package sample.simple;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SampleSimpleApplication}.
 *
 * @author Dave Syer
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTestSampleSimpleApplicationTests {

	@Autowired
	ApplicationContext ctx;

	@Test
	public void testContextLoads() {
		assertThat(this.ctx).isNotNull();
		assertThat(this.ctx.containsBean("helloWorldService")).isTrue();
		assertThat(this.ctx.containsBean("sampleSimpleApplication")).isTrue();
	}

}
