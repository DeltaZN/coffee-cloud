package ru.itmo.coffee.store.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.LongSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.converter.StringJsonMessageConverter
import org.springframework.kafka.support.serializer.JsonSerializer
import ru.itmo.coffee.kafka.IngredientMessageKafkaDTO

@Configuration
open class KafkaProducerConfig {
    @Value("\${kafka.server}")
    private val kafkaServer: String? = null

    @Value("\${kafka.producer.id}")
    private val kafkaProducerId: String = ""

    @Bean
    open fun producerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServer!!
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props[ProducerConfig.CLIENT_ID_CONFIG] = kafkaProducerId
        return props
    }

    @Bean
    open fun producerIngredientFactory(): ProducerFactory<Long, IngredientMessageKafkaDTO> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    open fun kafkaIngredientTemplate(): KafkaTemplate<Long, IngredientMessageKafkaDTO> {
        val template: KafkaTemplate<Long, IngredientMessageKafkaDTO> = KafkaTemplate(producerIngredientFactory())
        template.messageConverter = StringJsonMessageConverter()
        return template
    }
}