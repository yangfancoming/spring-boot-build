

package org.springframework.boot.actuate.autoconfigure.metrics.export.graphite;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import io.micrometer.graphite.GraphiteProtocol;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} for configuring Graphite metrics export.
 *
 * @author Jon Schneider
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@ConfigurationProperties(prefix = "management.metrics.export.graphite")
public class GraphiteProperties {

	/**
	 * Whether exporting of metrics to Graphite is enabled.
	 */
	private boolean enabled = true;

	/**
	 * Step size (i.e. reporting frequency) to use.
	 */
	private Duration step = Duration.ofMinutes(1);

	/**
	 * Base time unit used to report rates.
	 */
	private TimeUnit rateUnits = TimeUnit.SECONDS;

	/**
	 * Base time unit used to report durations.
	 */
	private TimeUnit durationUnits = TimeUnit.MILLISECONDS;

	/**
	 * Host of the Graphite server to receive exported metrics.
	 */
	private String host = "localhost";

	/**
	 * Port of the Graphite server to receive exported metrics.
	 */
	private Integer port = 2004;

	/**
	 * Protocol to use while shipping data to Graphite.
	 */
	private GraphiteProtocol protocol = GraphiteProtocol.PICKLED;

	/**
	 * For the default naming convention, turn the specified tag keys into part of the
	 * metric prefix.
	 */
	private String[] tagsAsPrefix = new String[0];

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Duration getStep() {
		return this.step;
	}

	public void setStep(Duration step) {
		this.step = step;
	}

	public TimeUnit getRateUnits() {
		return this.rateUnits;
	}

	public void setRateUnits(TimeUnit rateUnits) {
		this.rateUnits = rateUnits;
	}

	public TimeUnit getDurationUnits() {
		return this.durationUnits;
	}

	public void setDurationUnits(TimeUnit durationUnits) {
		this.durationUnits = durationUnits;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public GraphiteProtocol getProtocol() {
		return this.protocol;
	}

	public void setProtocol(GraphiteProtocol protocol) {
		this.protocol = protocol;
	}

	public String[] getTagsAsPrefix() {
		return this.tagsAsPrefix;
	}

	public void setTagsAsPrefix(String[] tagsAsPrefix) {
		this.tagsAsPrefix = tagsAsPrefix;
	}

}
