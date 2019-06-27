

package sample.session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

@RestController
public class HelloRestController {

	@GetMapping("/")
	String sessionId(WebSession session) {
		return session.getId();
	}

}
