

package org.springframework.boot.autoconfigure.jms;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerConfigUtils;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Configuration for Spring 4.1 annotation driven JMS.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @since 1.2.0
 */
@Configuration
@ConditionalOnClass(EnableJms.class)
class JmsAnnotationDrivenConfiguration {

	private final ObjectProvider<DestinationResolver> destinationResolver;

	private final ObjectProvider<JtaTransactionManager> transactionManager;

	private final ObjectProvider<MessageConverter> messageConverter;

	private final JmsProperties properties;

	JmsAnnotationDrivenConfiguration(
			ObjectProvider<DestinationResolver> destinationResolver,
			ObjectProvider<JtaTransactionManager> transactionManager,
			ObjectProvider<MessageConverter> messageConverter, JmsProperties properties) {
		this.destinationResolver = destinationResolver;
		this.transactionManager = transactionManager;
		this.messageConverter = messageConverter;
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultJmsListenerContainerFactoryConfigurer jmsListenerContainerFactoryConfigurer() {
		DefaultJmsListenerContainerFactoryConfigurer configurer = new DefaultJmsListenerContainerFactoryConfigurer();
		configurer.setDestinationResolver(this.destinationResolver.getIfUnique());
		configurer.setTransactionManager(this.transactionManager.getIfUnique());
		configurer.setMessageConverter(this.messageConverter.getIfUnique());
		configurer.setJmsProperties(this.properties);
		return configurer;
	}

	@Bean
	@ConditionalOnMissingBean(name = "jmsListenerContainerFactory")
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
			DefaultJmsListenerContainerFactoryConfigurer configurer,
			ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Configuration
	@EnableJms
	@ConditionalOnMissingBean(name = JmsListenerConfigUtils.JMS_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
	protected static class EnableJmsConfiguration {

	}

	@Configuration
	@ConditionalOnJndi
	protected static class JndiConfiguration {

		@Bean
		@ConditionalOnMissingBean(DestinationResolver.class)
		public JndiDestinationResolver destinationResolver() {
			JndiDestinationResolver resolver = new JndiDestinationResolver();
			resolver.setFallbackToDynamicDestination(true);
			return resolver;
		}

	}

}
