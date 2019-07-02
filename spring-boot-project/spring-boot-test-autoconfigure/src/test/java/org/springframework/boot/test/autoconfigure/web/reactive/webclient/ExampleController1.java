

package org.springframework.boot.test.autoconfigure.web.reactive.webclient;

import reactor.core.publisher.Mono;

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Example {@link Controller} used with {@link WebFluxTest} tests.
 *
 * @author Stephane Nicoll
 */
@RestController
public class ExampleController1 {

	@GetMapping("/one")
	public Mono<String> one() {
		return Mono.just("one");
	}

}
