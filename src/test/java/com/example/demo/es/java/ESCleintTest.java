package com.example.demo.es.java;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
@Slf4j
class ESCleintTest {


    private ElasticsearchClient esClient;

    @BeforeEach
    public void before() {
        // URL and API key
        String serverUrl = "http://localhost:9200";

        // Create the low-level client
        RestClient restClient = RestClient.builder(HttpHost
                .create(serverUrl)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // And create the API client
        esClient = new ElasticsearchClient(transport);

        log.info("esClient:{}", esClient);
    }


    public static Map<String, Property> mapping() {
        // 文档属性
        Map<String, Property> propertyMap = new HashMap<>();
        propertyMap.put("id", new Property(new KeywordProperty.Builder().build()));
        propertyMap.put("name", new Property(new TextProperty.Builder().build()));
        propertyMap.put("age", new Property(new KeywordProperty.Builder().build()));

        return propertyMap;
    }

    @Test
    void createdIndex() throws IOException {
        TypeMapping typeMapping = new TypeMapping.Builder()
                .properties(mapping())
                .build();

        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
                .index("index_order_student_2020")
                .mappings(typeMapping).build();

        CreateIndexResponse createIndexResponse = esClient.indices().create(createIndexRequest);

        log.info("create index result:{}", JSON.toJSONString(createIndexResponse));
    }


    @Test
    public void saveDoc() {

    }
}
