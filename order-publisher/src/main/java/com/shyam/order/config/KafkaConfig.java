package com.shyam.order.config;

import com.shyam.avro.order.Order;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.confluent.kafka.schemaregistry.client.SchemaRegistryClientConfig.BASIC_AUTH_CREDENTIALS_SOURCE;
import static io.confluent.kafka.schemaregistry.client.SchemaRegistryClientConfig.USER_INFO_CONFIG;

/**
 * @author SVadikari on 2/21/21
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

    @Value("${kafka.properties.producer.bootstrap-servers}")
    private String producerBootstrapServers;

    @Value("${kafka.properties.producer.key-serializer}")
    private String producerKeySerializer;

    @Value("${kafka.properties.producer.value-serializer}")
    private String producerValueSerializer;

    @Value("${kafka.properties.producer.compression-type}")
    private String compressionType;

    @Value("${kafka.properties.producer.max-request-size}")
    private Integer maxRequestSize;

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

    private Map<String, Object> kafkaProps() throws IOException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("auto.register.schemas", enableAutoRegister);
        properties.put("schema.registry.url", schemaRegistryUrl);
        properties.put("message.max.bytes", maxRequestSize);
        if (sslEnabled) {
            properties.put("schema.registry.ssl.truststore.location", trustStoreFileLocation.getFile().getAbsolutePath());
            properties.put("schema.registry.ssl.truststore.password", schemaRegistrySslTrustStorePassword);
            properties.put("schema.registry.ssl.keystore.location", keyStoreFileLocation.getFile().getAbsolutePath());
            properties.put("schema.registry.ssl.keystore.password", schemaRegistrySslKeyStorePassword);
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreFileLocation.getFile().getAbsolutePath());
            properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, sslTruststorePassword);
            properties.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStoreFileLocation.getFile().getAbsolutePath());
            properties.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, sslKeyStorePassword);
            properties.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, sslKeyPassword);
            properties.put(SslConfigs.DEFAULT_SSL_ENABLED_PROTOCOLS, sslEnabledProtocols);
            properties.put(SslConfigs.DEFAULT_SSL_TRUSTSTORE_TYPE, sslTrustStoreType);
        }
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerBootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producerKeySerializer);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producerValueSerializer);
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxRequestSize);
        if (StringUtils.hasLength(compressionType)) {
            properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        }
        properties.put(BASIC_AUTH_CREDENTIALS_SOURCE, basicAuthSource);
        properties.put(USER_INFO_CONFIG, basicAuthUser + ":" + basicAuthPwd);
        return properties;
    }

    @Bean
    public KafkaTemplate<String, Order> orderKafkaTemplate() throws IOException {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(kafkaProps()));
    }
}
