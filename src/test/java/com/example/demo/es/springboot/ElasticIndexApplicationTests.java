package com.example.demo.es.springboot;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

@Slf4j
@SpringBootTest
class ElasticIndexApplicationTests {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private StudentDao studentDao;


    @Test
    public void index() {
        IndexCoordinates indexCoordinatesFor = elasticsearchTemplate.getIndexCoordinatesFor(Student.class);
        String indexName = indexCoordinatesFor.getIndexName();
        String[] indexNames = indexCoordinatesFor.getIndexNames();
        log.info("index name:{}", JSON.toJSONString(indexName));

        boolean exists = elasticsearchTemplate.indexOps(indexCoordinatesFor).exists();
        log.info("index name:{} exit:{}",indexNames,exists);

    }



    @Test
    public void deleteIndex() {
        IndexCoordinates indexCoordinates = elasticsearchTemplate.getIndexCoordinatesFor(Student.class);
        boolean deleteResult = elasticsearchTemplate.indexOps(indexCoordinates).delete();

//        IndexCoordinates indexCoordinates = IndexCoordinates.of("lx-sd2");
//        boolean deleteResult = elasticsearchTemplate.indexOps(indexCoordinates).delete();
        log.info("index delete result :{}", deleteResult);

        boolean exists = elasticsearchTemplate.indexOps(indexCoordinates).exists();
        log.info("index exit :{}", exists);

    }


}
