package com.shyam.order.config;

import com.shyam.avro.order.Order;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.confluent.kafka.schemaregistry.client.SchemaRegistryClientConfig.BASIC_AUTH_CREDENTIALS_SOURCE;
import static io.confluent.kafka.schemaregistry.client.SchemaRegistryClientConfig.USER_INFO_CONFIG;

/**
 * @author SVadikari on 07/29/21
 */
@Configuration
@Log4j2
@Setter
@EnableKafka
public class KafkaConfig {

    @Value("classpath:${kafka.properties.ssl.truststore.location}")
    Resource trustStoreFileLocation;

    @Value("classpath:${kafka.properties.ssl.keystore.location}")
    Resource keyStoreFileLocation;

    @Value("${kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${kafka.properties.schema.registry.ssl.truststore.password}")
    private String schemaRegistrySslTrustStorePassword;

    @Value("${kafka.properties.schema.registry.ssl.keystore.password}")
    private String schemaRegistrySslKeyStorePassword;

    @Value("${kafka.properties.security.protocol}")
    private String securityProtocol;

    @Value("${kafka.properties.ssl.truststore.password}")
    private String sslTruststorePassword;

    @Value("${kafka.properties.ssl.keystore.password}")
    private String sslKeyStorePassword;

    @Value("${kafka.properties.ssl.key.password}")
    private String sslKeyPassword;

    @Value("${kafka.properties.ssl.protocols}")
    private String sslEnabledProtocols;

    @Value("${kafka.properties.ssl.truststore.type}")
    private String sslTrustStoreType;

    @Value("${kafka.properties.basic.auth.credentials.source:USER_INFO}")
    private String basicAuthSource;

    @Value("${kafka.properties.basic.auth.credentials.user}")
    private String basicAuthUser;

    @Value("${kafka.properties.basic.auth.credentials.pass}")
    private String basicAuthPwd;

    @Value("${kafka.properties.ssl.enabled}")
    private boolean sslEnabled;

    @Value("${kafka.properties.enableAutoRegister}")
    private boolean enableAutoRegister;

    @Value("${kafka.properties.consumer.bootstrap-servers}")
    private String consumerBootstrapServers;

    @Value("${kafka.properties.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${kafka.properties.consumer.key-deserializer}")
    private String consumerKeyDeserializer;

    @Value("${kafka.properties.consumer.value-deserializer}")
    private String consumerValueDeserializer;

    @Value("${kafka.properties.consumer.max-message-size}")
    private Integer maxMessageSize;


    private Map<String, Object> kafkaProps() throws IOException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("auto.register.schemas", enableAutoRegister);
        properties.put("schema.registry.url", schemaRegistryUrl);
        properties.put("message.max.bytes", maxMessageSize);
        if (sslEnabled) {
            properties.put("schema.registry.ssl.truststore.location",
                    trustStoreFileLocation.getFile().getAbsolutePath());
            properties.put("schema.registry.ssl.truststore.password", schemaRegistrySslTrustStorePassword);
            properties.put("schema.registry.ssl.keystore.location", keyStoreFileLocation.getFile().getAbsolutePath());
            properties.put("schema.registry.ssl.keystore.password", schemaRegistrySslKeyStorePassword);
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG,
                    trustStoreFileLocation.getFile().getAbsolutePath());
            properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTruststorePassword);
            properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStoreFileLocation.getFile().getAbsolutePath());
            properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, sslKeyStorePassword);
            properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, sslKeyPassword);
            properties.put(SslConfigs.DEFAULT_SSL_ENABLED_PROTOCOLS, sslEnabledProtocols);
            properties.put(SslConfigs.DEFAULT_SSL_TRUSTSTORE_TYPE, sslTrustStoreType);
        }

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerKeyDeserializer);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerValueDeserializer);
        properties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxMessageSize);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        properties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        properties.put(BASIC_AUTH_CREDENTIALS_SOURCE, basicAuthSource);
        properties.put(USER_INFO_CONFIG, basicAuthUser + ":" + basicAuthPwd);
        return properties;
    }

    @Bean
    public ConsumerFactory<String, Order> consumerFactory() throws IOException {
        return new DefaultKafkaConsumerFactory<>(kafkaProps());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Order>>
    kafkaListenerContainerFactory() throws IOException {
        ConcurrentKafkaListenerContainerFactory<String, Order> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
