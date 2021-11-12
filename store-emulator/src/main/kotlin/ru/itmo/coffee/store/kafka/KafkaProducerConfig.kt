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

//    @Bean
//    open fun producerPaymentFactory(): ProducerFactory<Long, PaymentResultDto> {
//        return DefaultKafkaProducerFactory(producerConfigs())
//    }
//
//    @Bean
//    open fun kafkaPaymentResultTemplate(): KafkaTemplate<Long, PaymentResultDto> {
//        val template: KafkaTemplate<Long, PaymentResultDto> = KafkaTemplate(producerPaymentFactory())
//        template.setMessageConverter(StringJsonMessageConverter())
//        return template
//    }
//
//    @Bean
//    open fun producerLoanRequestFactory(): ProducerFactory<Long, LoanRequestAnswerDto> {
//        return DefaultKafkaProducerFactory(producerConfigs())
//    }
//
//    @Bean
//    open fun kafkaLoanProcessedTemplate(): KafkaTemplate<Long, LoanRequestAnswerDto> {
//        val template: KafkaTemplate<Long, LoanRequestAnswerDto> = KafkaTemplate(producerLoanRequestFactory())
//        template.setMessageConverter(StringJsonMessageConverter())
//        return template
//    }
//
//    @Bean
//    open fun producerLoanFactory(): ProducerFactory<Long, LoanDto> {
//        return DefaultKafkaProducerFactory(producerConfigs())
//    }
//
//    @Bean
//    open fun kafkaLoanTemplate(): KafkaTemplate<Long, LoanDto> {
//        val template: KafkaTemplate<Long, LoanDto> = KafkaTemplate(producerLoanFactory())
//        template.setMessageConverter(StringJsonMessageConverter())
//        return template
//    }
}