package com.example.demo.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class ElasticDemoApplicationTests {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private StudentDao studentDao;

	@Test
	public void save() {
		Student student = new Student();
		student.setId(1);
		student.setAge(10);
		student.setName("张三");

		Student save = studentDao.save(student);

		System.out.println(JSON.toJSONString(save));
	}

	@Test
	public void findById(){
		Optional<Student> byId = studentDao.findById(1);
		if (byId.isPresent()){
			System.out.println(JSON.toJSONString(byId));
		}
	}

	@Test
	public void findAll(){
		Iterable<Student> all = studentDao.findAll();
		all.forEach(student -> System.out.println(JSON.toJSONString(student)));
	}

	@Test
	public void deleteById(){
		studentDao.deleteById(1);
	}

	@Test
	public void deleteByEntity(){
		Student student = new Student();
		student.setName("张三");

		studentDao.delete(student);
	}

	@Test
	public void deleteAll(){
		studentDao.deleteAll();
	}

	@Test
	public void findByName(){
		List<Student> name = studentDao.findByName("张三");

		name.stream().forEach(student -> System.out.println(JSON.toJSONString(student)));
	}
}
