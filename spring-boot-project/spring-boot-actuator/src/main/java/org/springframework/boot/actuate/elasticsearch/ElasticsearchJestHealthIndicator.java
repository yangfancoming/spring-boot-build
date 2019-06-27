

package org.springframework.boot.actuate.elasticsearch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.Stats;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * {@link HealthIndicator} for Elasticsearch using a {@link JestClient}.
 *
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class ElasticsearchJestHealthIndicator extends AbstractHealthIndicator {

	private final JestClient jestClient;

	private final JsonParser jsonParser = new JsonParser();

	public ElasticsearchJestHealthIndicator(JestClient jestClient) {
		super("Elasticsearch health check failed");
		this.jestClient = jestClient;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		JestResult aliases = this.jestClient.execute(new Stats.Builder().build());
		JsonElement root = this.jsonParser.parse(aliases.getJsonString());
		JsonObject shards = root.getAsJsonObject().get("_shards").getAsJsonObject();
		int failedShards = shards.get("failed").getAsInt();
		if (failedShards != 0) {
			builder.outOfService();
		}
		else {
			builder.up();
		}
	}

}
