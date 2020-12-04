package com.spring.social.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.spring.social.model.Flow;

@Configuration
@EnableRedisRepositories
@EnableCaching
public class RedisConfig {
	
	@Autowired
	RedisProperties redisProperties;
	
    @Bean
    public JedisConnectionFactory connectionFactory()
    {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setPassword(redisProperties.getPassword());
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Flow> template() {
        RedisTemplate<String, Flow> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        /*template.setHashKeySerializer(new GenericToStringSerializer<Flow>(Flow.class));
        template.setValueSerializer(new GenericToStringSerializer<Flow>(Flow.class));*/
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
}
