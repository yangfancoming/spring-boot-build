

package org.springframework.boot.actuate.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

/**
 * {@link HealthIndicator} for Apache Solr.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @since 2.0.0
 */
public class SolrHealthIndicator extends AbstractHealthIndicator {

	private final SolrClient solrClient;

	public SolrHealthIndicator(SolrClient solrClient) {
		super("Solr health check failed");
		this.solrClient = solrClient;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminParams.CoreAdminAction.STATUS);
		CoreAdminResponse response = request.process(this.solrClient);
		int statusCode = response.getStatus();
		Status status = (statusCode != 0) ? Status.DOWN : Status.UP;
		builder.status(status).withDetail("status", statusCode);
	}

}
