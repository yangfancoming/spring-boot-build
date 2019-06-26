

package org.springframework.boot.autoconfigure.context.filtersample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleConfiguration {

	@Bean
	public String example() {
		return "test";
	}

}
