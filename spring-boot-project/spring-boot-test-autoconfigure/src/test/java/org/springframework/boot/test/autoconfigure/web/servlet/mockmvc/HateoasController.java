

package org.springframework.boot.test.autoconfigure.web.servlet.mockmvc;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link RestController} used by {@link WebMvcTestHateoasIntegrationTests}.
 *
 * @author Andy Wilkinson
 */
@RestController
@RequestMapping("/hateoas")
class HateoasController {

	@RequestMapping("/resource")
	public Resource<Map<String, String>> resource() {
		return new Resource<>(new HashMap<String, String>(),
				new Link("self", "http://api.example.com"));
	}

	@RequestMapping("/plain")
	public Map<String, String> plain() {
		return new HashMap<>();
	}

}
