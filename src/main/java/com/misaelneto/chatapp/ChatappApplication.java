package com.misaelneto.chatapp;

import org.apache.http.impl.client.HttpClientBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication
@MapperScan( value = "com.misaelneto.chatapp.mappers" , sqlSessionFactoryRef = "wordpressSessionFactoryBean")
public class ChatappApplication {

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(ChatappApplication.class, args);
	}

	@Bean("wordpressSessionFactoryBean")
	public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setTypeAliasesPackage("trix2.wordpress.models");
		sessionFactory.setConfigurationProperties(getProperties());
		return sessionFactory;
	}

	@Bean("wordpressRestTemplate")
	public RestTemplate restTemplate() throws Exception {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
		return new RestTemplate(clientHttpRequestFactory);
	}


	private Properties getProperties() {
		Properties properties = new Properties();
		properties.put("mapUnderscoreToCamelCase", true);
		return properties;
	}
}
