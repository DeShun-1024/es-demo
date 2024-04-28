package com.example.demo.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudentDao extends ElasticsearchRepository<Student,Integer> {

    List<Student> findByName(String name);
}
