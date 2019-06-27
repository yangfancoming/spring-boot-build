

package sample;

import org.junit.Test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Tests for {@link SampleGlassfishDeployApplication}.
 */
public class SampleGlassfishDeployApplicationIT {

	private int port = Integer.valueOf(System.getProperty("port"));

	@Test
	public void testHome() throws Exception {
		String url = "http://localhost:" + this.port + "/bootapp/";
		System.out.println(url);
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(url,
				String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isEqualTo("Hello World");
	}

	@Test
	public void testHealth() throws Exception {
		String url = "http://localhost:" + this.port + "/bootapp/actuator/health";
		System.out.println(url);
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(url,
				String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isEqualTo("{\"status\":\"UP\"}");
	}

}
