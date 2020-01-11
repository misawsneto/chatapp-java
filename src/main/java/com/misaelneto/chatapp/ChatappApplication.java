package com.misaelneto.chatapp;

import org.apache.http.impl.client.HttpClientBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

@EnableCaching
@MapperScan( value = "com.misaelneto.chatapp.mappers" , sqlSessionFactoryRef = "wordpressSessionFactoryBean")
@SpringBootApplication
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

	@Value("${spring.redis.host}")
	private String redisHost;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.password:}")
	private String redisPassword;
	@Value("${spring.redis.cachedb:2}")
	private Integer redisDB;
	@Value("${spring.redis.expiration:60}")
	private Long expiration;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration configuration =
				new RedisStandaloneConfiguration(redisHost, redisPort);
		configuration.setDatabase(redisDB);
		if (redisPassword != null && !redisPassword.isEmpty()) {
			configuration.setPassword(redisPassword);
		}
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration);
		jedisConnectionFactory.afterPropertiesSet();
		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);

		return redisTemplate;
	}


	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) throws IOException {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(expiration))
				.disableCachingNullValues();

		RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
					.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory);

		return builder.cacheDefaults(config).build();
	}
}


