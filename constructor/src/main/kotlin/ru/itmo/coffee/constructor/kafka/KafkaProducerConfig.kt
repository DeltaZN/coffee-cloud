package ru.itmo.coffee.constructor.kafka

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
import ru.itmo.coffee.kafka.RecipeMessageKafkaDTO

@Configuration
open class KafkaProducerConfig {
    @Value("\${kafka.server}")
    private val kafkaServer: String? = null

    @Value("\${kafka.producer.id}")
    private val kafkaProducerId: String = ""

    @Bean
    open fun producerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        val jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        val jaasCfg = String.format(jaasTemplate, "constructor", "12345678")
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaServer!!
        props[ProducerConfig.ACKS_CONFIG] = "all"
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props["security.protocol"] = "SASL_SSL"
        props["sasl.mechanism"] = "SCRAM-SHA-512"
        props["sasl.jaas.config"] = jaasCfg
        props["ssl.truststore.location"] = "/home/gsavin/client.truststore.jks"
        props["ssl.truststore.password"] = "changeit"
        props[ProducerConfig.CLIENT_ID_CONFIG] = kafkaProducerId
        return props
    }

    @Bean
    open fun producerRecipeFactory(): ProducerFactory<Long, RecipeMessageKafkaDTO> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    open fun kafkaRecipesTemplate(): KafkaTemplate<Long, RecipeMessageKafkaDTO> {
        val template: KafkaTemplate<Long, RecipeMessageKafkaDTO> = KafkaTemplate(producerRecipeFactory())
        template.messageConverter = StringJsonMessageConverter()
        return template
    }
}