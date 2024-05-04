package com.example.demo.es.java;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSON;
import com.example.demo.es.springboot.Student;
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
        propertyMap.put("interest", new Property(new TextProperty.Builder().build()));

        return propertyMap;
    }

    /**
     * <a href= "https://blog.csdn.net/qq_42511655/article/details/125798023 "/ >
     *
     * @throws IOException
     */
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
    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder()
                .index("index_order_student_2020")
                .build();
        DeleteIndexResponse delete = esClient.indices().delete(deleteIndexRequest);
        log.info("delete index result :{}", delete.acknowledged());
    }

    @Test
    public void addDoc() throws IOException {

        Student student = new Student();
        student.setId(2);
        student.setName("李四");
        student.setAge(234);
        student.setInterest("游戏，篮球，音乐，都市");

        IndexRequest<Student> request = new IndexRequest.Builder<Student>()
                .document(student)
//                .id("21")
                .index("index_order_student_2020")
                .build();
        IndexResponse index = esClient.index(request);

        log.info("doc add index:{} ,result:{}", index.index(), index.result());
    }

    @Test
    public void findById() throws IOException {
        GetRequest request = new GetRequest.Builder()
                .index("index_order_student_2020")
                .id("1")
                .build();

        GetResponse<Student> studentGetResponse = esClient.get(request, Student.class);
        Student source = studentGetResponse.source();

        log.info("find by id result : {}", JSON.toJSONString(source));
    }

    @Test
    public void matchAll() throws IOException {
        Query query = new Query.Builder()
                .matchAll(QueryBuilders.matchAll().build())
                .build();

        SearchRequest request = new SearchRequest.Builder()
                .index("index_order_student_2020")
                .query(query)
                .build();

        SearchResponse<Student> search = esClient.search(request, Student.class);
        for (Hit<Student> hit : search.hits().hits()) {
            Student source = hit.source();
            log.info("student :{}", source);
        }
    }

    @Test
    public void match() throws IOException {
        Query query = new Query.Builder()
                .match(QueryBuilders.match()
                        .field("name")
                        .query("张")
                        .build())
                .build();

        SearchRequest request = new SearchRequest.Builder()
                .index("index_order_student_2020")
                .query(query)
                .build();

        SearchResponse<Student> search = esClient.search(request, Student.class);
        for (Hit<Student> hit : search.hits().hits()) {
            Student source = hit.source();
            log.info("student :{}", source);
        }
    }


    @Test
    public void booleanQuery() throws IOException {
        BoolQuery boolQuery = new BoolQuery.Builder()
                .should(MatchQuery.of(s->s.field("name").query("李四"))._toQuery())
                .filter(RangeQuery.of(s->s.field("age").gt(JsonData.of(12)))._toQuery())
                .build();

        // 排序
        SortOptions sortOption1 = new SortOptions.Builder()
                .field(k->k.field("age").order(SortOrder.Asc))
                .build();

        SortOptions sortOption2 = SortOptions
                .of(s -> s.field(FieldSort.of(f -> f.field("age").order(SortOrder.Desc))));

        SearchRequest request = new SearchRequest.Builder()
                .index("index_order_student_2020")
                .query(boolQuery._toQuery())
                .sort(sortOption2)
                .build();

        SearchResponse<Student> search = esClient.search(request, Student.class);
        for (Hit<Student> hit : search.hits().hits()) {
            Student source = hit.source();
            log.info("student :{}", source);
        }
    }


    @Test
    public void updateById() throws IOException {
        Student student = new Student();
        student.setId(2);
        student.setName("张三");
        student.setAge(12);
        student.setInterest("游戏，篮球，音乐，都市 更新过的");

        UpdateRequest<Student, Student> updateRequest = new UpdateRequest.Builder<Student, Student>()
                .index("index_order_student_2020")
                .id("2")
                .doc(student)
                .build();

        UpdateResponse<Student> update = esClient.update(updateRequest, Student.class);
        log.info("index update response :{}", update);
    }
}
