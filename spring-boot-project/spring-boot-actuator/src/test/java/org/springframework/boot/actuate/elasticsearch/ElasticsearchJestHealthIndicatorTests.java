

package org.springframework.boot.actuate.elasticsearch;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.exception.CouldNotConnectException;
import io.searchbox.core.SearchResult;
import org.junit.Test;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ElasticsearchJestHealthIndicator}.
 *
 * @author Stephane Nicoll
 */
public class ElasticsearchJestHealthIndicatorTests {

	private final JestClient jestClient = mock(JestClient.class);

	private final ElasticsearchJestHealthIndicator healthIndicator = new ElasticsearchJestHealthIndicator(
			this.jestClient);

	@SuppressWarnings("unchecked")
	@Test
	public void elasticsearchIsUp() throws IOException {
		given(this.jestClient.execute(any(Action.class)))
				.willReturn(createJestResult(4, 0));
		Health health = this.healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.UP);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void elasticsearchIsDown() throws IOException {
		given(this.jestClient.execute(any(Action.class))).willThrow(
				new CouldNotConnectException("http://localhost:9200", new IOException()));
		Health health = this.healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.DOWN);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void elasticsearchIsOutOfService() throws IOException {
		given(this.jestClient.execute(any(Action.class)))
				.willReturn(createJestResult(4, 1));
		Health health = this.healthIndicator.health();
		assertThat(health.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
	}

	private static JestResult createJestResult(int shards, int failedShards) {
		String json = String.format("{_shards: {\n" + "total: %s,\n" + "successful: %s,\n"
				+ "failed: %s\n" + "}}", shards, shards - failedShards, failedShards);
		SearchResult searchResult = new SearchResult(new Gson());
		searchResult.setJsonString(json);
		searchResult.setJsonObject(new JsonParser().parse(json).getAsJsonObject());
		return searchResult;
	}

}
