package ru.itmo.coffee.store.kafka
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter
import org.springframework.kafka.support.converter.StringJsonMessageConverter

@Configuration
open class KafkaConsumerConfig {
    @Value("\${kafka.server}")
    private val kafkaServer: String = ""

    @Value("\${kafka.group.id}")
    private val kafkaGroupId: String = ""

    @Bean
    open fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        return mapper
    }

    @Bean
    open fun batchFactory(
        consumerFactory: ConsumerFactory<Long, Any>,
        converter: StringJsonMessageConverter
    ): KafkaListenerContainerFactory<*> {
        val factory: ConcurrentKafkaListenerContainerFactory<Long, Any> =
            ConcurrentKafkaListenerContainerFactory<Long, Any>()
        factory.consumerFactory = consumerFactory
        factory.isBatchListener = true
        factory.setMessageConverter(BatchMessagingMessageConverter(converter))
        return factory
    }

    @Bean
    open fun singleFactory(consumerFactory: ConsumerFactory<Long, Any>): KafkaListenerContainerFactory<*> {
        val factory: ConcurrentKafkaListenerContainerFactory<Long, Any> =
            ConcurrentKafkaListenerContainerFactory<Long, Any>()
        factory.consumerFactory = consumerFactory
        factory.isBatchListener = false
        factory.setMessageConverter(StringJsonMessageConverter())
        return factory
    }

    @Bean
    open fun consumerFactory(consumerConfigs: Map<String, Any>): ConsumerFactory<Long, Any> =
        DefaultKafkaConsumerFactory(consumerConfigs)

    @Bean
    open fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<*> =
        ConcurrentKafkaListenerContainerFactory<Any, Any>()

    @Bean
    open fun consumerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        val jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        val jaasCfg = String.format(jaasTemplate, "store", "12345678")
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServer
        props["auto.offset.reset"] = "earliest"
        props[ConsumerConfig.GROUP_ID_CONFIG] = kafkaGroupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = LongDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props["security.protocol"] = "SASL_SSL"
        props["sasl.mechanism"] = "SCRAM-SHA-512"
        props["sasl.jaas.config"] = jaasCfg
        props["ssl.truststore.location"] = "/home/gsavin/client.truststore.jks"
        props["ssl.truststore.password"] = "changeit"
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
        return props
    }

    @Bean
    open fun converter() = StringJsonMessageConverter()
}