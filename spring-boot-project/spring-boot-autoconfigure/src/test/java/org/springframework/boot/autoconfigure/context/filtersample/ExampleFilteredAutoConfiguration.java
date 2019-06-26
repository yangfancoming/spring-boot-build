

package org.springframework.boot.autoconfigure.context.filtersample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleFilteredAutoConfiguration {

	@Bean
	public String anotherExample() {
		return "fail";
	}

}
