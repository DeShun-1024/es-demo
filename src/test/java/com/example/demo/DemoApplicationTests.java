package com.example.demo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	ElasticsearchOperations operations;

	@Autowired
	ElasticsearchClient elasticsearchClient;

	@Autowired
	RestClient restClient;

	@Autowired
	JsonpMapper jsonpMapper;

	@Test
	void contextLoads() {

		System.out.println(elasticsearchClient.toString());

	}

}
