

package org.springframework.boot.autoconfigure.jms;

import java.time.Duration;

import javax.jms.ConnectionFactory;
import javax.jms.Message;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jms.JmsProperties.DeliveryMode;
import org.springframework.boot.autoconfigure.jms.JmsProperties.Template;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring JMS.
 *
 * @author Greg Turnquist
 * @author Stephane Nicoll
 */
@Configuration
@ConditionalOnClass({ Message.class, JmsTemplate.class })
@ConditionalOnBean(ConnectionFactory.class)
@EnableConfigurationProperties(JmsProperties.class)
@Import(JmsAnnotationDrivenConfiguration.class)
public class JmsAutoConfiguration {

	@Configuration
	protected static class JmsTemplateConfiguration {

		private final JmsProperties properties;

		private final ObjectProvider<DestinationResolver> destinationResolver;

		private final ObjectProvider<MessageConverter> messageConverter;

		public JmsTemplateConfiguration(JmsProperties properties,
				ObjectProvider<DestinationResolver> destinationResolver,
				ObjectProvider<MessageConverter> messageConverter) {
			this.properties = properties;
			this.destinationResolver = destinationResolver;
			this.messageConverter = messageConverter;
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnSingleCandidate(ConnectionFactory.class)
		public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
			PropertyMapper map = PropertyMapper.get();
			JmsTemplate template = new JmsTemplate(connectionFactory);
			template.setPubSubDomain(this.properties.isPubSubDomain());
			map.from(this.destinationResolver::getIfUnique).whenNonNull()
					.to(template::setDestinationResolver);
			map.from(this.messageConverter::getIfUnique).whenNonNull()
					.to(template::setMessageConverter);
			mapTemplateProperties(this.properties.getTemplate(), template);
			return template;
		}

		private void mapTemplateProperties(Template properties, JmsTemplate template) {
			PropertyMapper map = PropertyMapper.get();
			map.from(properties::getDefaultDestination).whenNonNull()
					.to(template::setDefaultDestinationName);
			map.from(properties::getDeliveryDelay).whenNonNull().as(Duration::toMillis)
					.to(template::setDeliveryDelay);
			map.from(properties::determineQosEnabled).to(template::setExplicitQosEnabled);
			map.from(properties::getDeliveryMode).whenNonNull().as(DeliveryMode::getValue)
					.to(template::setDeliveryMode);
			map.from(properties::getPriority).whenNonNull().to(template::setPriority);
			map.from(properties::getTimeToLive).whenNonNull().as(Duration::toMillis)
					.to(template::setTimeToLive);
			map.from(properties::getReceiveTimeout).whenNonNull().as(Duration::toMillis)
					.to(template::setReceiveTimeout);
		}

	}

	@Configuration
	@ConditionalOnClass(JmsMessagingTemplate.class)
	@Import(JmsTemplateConfiguration.class)
	protected static class MessagingTemplateConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnSingleCandidate(JmsTemplate.class)
		public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
			return new JmsMessagingTemplate(jmsTemplate);
		}

	}

}
