

package org.springframework.boot.autoconfigure.kafka;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerConfigUtils;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.RecordMessageConverter;

/**
 * Configuration for Kafka annotation-driven support.
 *
 * @author Gary Russell
 * @author Eddú Meléndez
 * @since 1.5.0
 */
@Configuration
@ConditionalOnClass(EnableKafka.class)
class KafkaAnnotationDrivenConfiguration {

	private final KafkaProperties properties;

	private final RecordMessageConverter messageConverter;

	private final KafkaTemplate<Object, Object> kafkaTemplate;

	KafkaAnnotationDrivenConfiguration(KafkaProperties properties,
			ObjectProvider<RecordMessageConverter> messageConverter,
			ObjectProvider<KafkaTemplate<Object, Object>> kafkaTemplate) {
		this.properties = properties;
		this.messageConverter = messageConverter.getIfUnique();
		this.kafkaTemplate = kafkaTemplate.getIfUnique();
	}

	@Bean
	@ConditionalOnMissingBean
	public ConcurrentKafkaListenerContainerFactoryConfigurer kafkaListenerContainerFactoryConfigurer() {
		ConcurrentKafkaListenerContainerFactoryConfigurer configurer = new ConcurrentKafkaListenerContainerFactoryConfigurer();
		configurer.setKafkaProperties(this.properties);
		configurer.setMessageConverter(this.messageConverter);
		configurer.setReplyTemplate(this.kafkaTemplate);
		return configurer;
	}

	@Bean
	@ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
	public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ConsumerFactory<Object, Object> kafkaConsumerFactory) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		configurer.configure(factory, kafkaConsumerFactory);
		return factory;
	}

	@Configuration
	@EnableKafka
	@ConditionalOnMissingBean(name = KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
	protected static class EnableKafkaConfiguration {

	}

}
