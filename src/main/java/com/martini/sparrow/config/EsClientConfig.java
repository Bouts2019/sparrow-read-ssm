package com.martini.sparrow.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author martini at 2020/12/14 7:09
 */
@Configuration
@PropertySource("classpath:prop/elasticsearch.properties")
@EnableElasticsearchRepositories(basePackages = "com.martini.sparrow.elastic.repository")
public class EsClientConfig extends AbstractElasticsearchConfiguration {
    @Value("${es.server.uri}")
    private String serverUri;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(serverUri)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
