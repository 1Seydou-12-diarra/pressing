package com.gestionPressing.demo.infrastructure.kafka.config

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.util.backoff.ExponentialBackOff

@EnableKafka
@Configuration
class KafkaTopicsConfig {

    // ─────────────── CONSTANTES POUR @KafkaListener ───────────────
    public static final String TOPIC_COMMANDE = "pressing.commande.events"
    public static final String TOPIC_NOTIFICATION = "pressing.notification.events"
    public static final String TOPIC_AUDIT = "pressing.audit.events"

    public static final String TOPIC_NOTIFICATION_DLT = "pressing.notification.events.DLT"
    public static final String TOPIC_AUDIT_DLT = "pressing.audit.events.DLT"

    public static final String GROUP_COMMANDE = "commande-group"
    public static final String GROUP_NOTIFICATION = "notification-group"
    public static final String GROUP_AUDIT = "audit-group"

    public static final String GROUP_NOTIFICATION_DLT = "notification-dlt-group"
    public static final String GROUP_AUDIT_DLT = "audit-dlt-group"

    // ─────────────── Kafka bootstrap server ───────────────
    @Bean
    String bootstrapServers() { "localhost:9092" }

    // ─────────────── Topics beans ───────────────
    @Bean
    NewTopic topicCommandeEvents() {
        TopicBuilder.name(TOPIC_COMMANDE).partitions(3).replicas(1).compact().build()
    }

    @Bean
    NewTopic topicNotificationEvents() {
        TopicBuilder.name(TOPIC_NOTIFICATION).partitions(3).replicas(1).build()
    }

    @Bean
    NewTopic topicAuditEvents() {
        TopicBuilder.name(TOPIC_AUDIT).partitions(3).replicas(1).build()
    }

    @Bean
    NewTopic topicNotificationDlt() {
        TopicBuilder.name(TOPIC_NOTIFICATION_DLT).partitions(1).replicas(1).build()
    }

    @Bean
    NewTopic topicAuditDlt() {
        TopicBuilder.name(TOPIC_AUDIT_DLT).partitions(1).replicas(1).build()
    }

    // ─────────────── Producer factory ───────────────
    @Bean
    ProducerFactory<String, Object> producerFactory() {
        new DefaultKafkaProducerFactory<>([
                (ProducerConfig.BOOTSTRAP_SERVERS_CONFIG)             : bootstrapServers(),
                (ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)          : StringSerializer,
                (ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)        : JsonSerializer,
                (ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG)            : true,
                (ProducerConfig.ACKS_CONFIG)                          : 'all',
                (ProducerConfig.RETRIES_CONFIG)                       : 3,
                (ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION): 1,
                (ProducerConfig.COMPRESSION_TYPE_CONFIG)              : 'snappy',
                (ProducerConfig.LINGER_MS_CONFIG)                     : 5,
                (ProducerConfig.BATCH_SIZE_CONFIG)                    : 32 * 1024
        ])
    }

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate() { new KafkaTemplate<>(producerFactory()) }

    // ─────────────── Consumer factory ───────────────
    private Map<String, Object> baseConsumerConfig(String groupId) {
        [
                (ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG)       : bootstrapServers(),
                (ConsumerConfig.GROUP_ID_CONFIG)                : groupId,
                (ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG)  : StringDeserializer,
                (ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG): JsonDeserializer,
                (JsonDeserializer.TRUSTED_PACKAGES)             : 'com.pressing.domain.events',
                (ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG)      : false,
                (ConsumerConfig.AUTO_OFFSET_RESET_CONFIG)       : 'earliest',
                (ConsumerConfig.MAX_POLL_RECORDS_CONFIG)        : 10
        ]
    }

    @Bean
    ConsumerFactory<String, Object> notificationConsumerFactory() {
        new DefaultKafkaConsumerFactory<>(baseConsumerConfig(GROUP_NOTIFICATION))
    }

    @Bean
    ConsumerFactory<String, Object> auditConsumerFactory() {
        new DefaultKafkaConsumerFactory<>(baseConsumerConfig(GROUP_AUDIT))
    }

    // ─────────────── Error handler ───────────────
    @Bean
    DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<String, Object> template) {
        new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template),
                new ExponentialBackOff(1000L, 2.0).tap { it.maxInterval = 10000L; it.maxElapsedTime = 30000L })
    }

    // ─────────────── Listener factories ───────────────
    @Bean('notificationListenerFactory')
    ConcurrentKafkaListenerContainerFactory<String, Object> notificationListenerFactory(DefaultErrorHandler kafkaErrorHandler) {
        def factory = new ConcurrentKafkaListenerContainerFactory<String, Object>()
        factory.consumerFactory = notificationConsumerFactory()
        factory.commonErrorHandler = kafkaErrorHandler
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        factory.concurrency = 3
        factory
    }

    @Bean('auditListenerFactory')
    ConcurrentKafkaListenerContainerFactory<String, Object> auditListenerFactory(DefaultErrorHandler kafkaErrorHandler) {
        def factory = new ConcurrentKafkaListenerContainerFactory<String, Object>()
        factory.consumerFactory = auditConsumerFactory()
        factory.commonErrorHandler = kafkaErrorHandler
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL_IMMEDIATE
        factory.concurrency = 2
        factory
    }
}