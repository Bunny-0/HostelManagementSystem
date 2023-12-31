package com.example.hostelManagementTool;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Properties;

@Configuration
public class Config {

//    @Bean
//    LettuceConnectionFactory getConnection(){
//        //Configuration class used for setting up RedisConnection via RedisConnectionFactory using connecting to a single node Redis installation.
//        RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration();
//        LettuceConnectionFactory lettuceConnectionFactory=new LettuceConnectionFactory(redisStandaloneConfiguration);
//        return lettuceConnectionFactory;
//
//    }
//
//   @Bean
//    RedisTemplate<String,Object> redisTemplate(){
//        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
//       RedisSerializer<String> redisSerializer=new StringRedisSerializer();
//       redisTemplate.setKeySerializer(redisSerializer);
//       JdkSerializationRedisSerializer jdkSerializationRedisSerializer=new JdkSerializationRedisSerializer();
//       redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
//       redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
//       return  redisTemplate;
//
//   }

   @Bean
    Properties KafkaProperties(){
        Properties properties=new Properties();
       properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
       properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
       properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
       return properties;
   }
    @Bean
    ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    ProducerFactory<String,String> getProducerFactory(){
        return new DefaultKafkaProducerFactory(KafkaProperties());
    }

    KafkaTemplate<String, String> getKafkaTemplate(){
        return new KafkaTemplate<>(getProducerFactory());
    }
}
