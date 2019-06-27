

package sample;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

	@RequestMapping("/")
	public String hello() {
		return "Hello World";
	}

	@RequestMapping("/send-error")
	public void sendError(HttpServletResponse response) throws IOException {
		response.sendError(500);
	}

	@RequestMapping("/exception")
	public void exception() {
		throw new RuntimeException();
	}

}
