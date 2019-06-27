

package org.springframework.boot.actuate.metrics.export.prometheus;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;

/**
 * {@link Endpoint} that outputs metrics in a format that can be scraped by the Prometheus
 * server.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
@WebEndpoint(id = "prometheus")
public class PrometheusScrapeEndpoint {

	private final CollectorRegistry collectorRegistry;

	public PrometheusScrapeEndpoint(CollectorRegistry collectorRegistry) {
		this.collectorRegistry = collectorRegistry;
	}

	@ReadOperation(produces = TextFormat.CONTENT_TYPE_004)
	public String scrape() {
		try {
			Writer writer = new StringWriter();
			TextFormat.write004(writer, this.collectorRegistry.metricFamilySamples());
			return writer.toString();
		}
		catch (IOException ex) {
			// This actually never happens since StringWriter::write() doesn't throw any
			// IOException
			throw new RuntimeException("Writing metrics failed", ex);
		}
	}

}
