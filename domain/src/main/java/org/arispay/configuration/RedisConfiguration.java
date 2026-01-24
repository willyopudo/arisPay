package org.arispay.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
@Log4j2
public class RedisConfiguration {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);


        // Debug logging
        String passwordPreview = "";
        if (redisPassword != null && redisPassword.length() > 8) {
            passwordPreview = redisPassword.substring(0, 4) + "..." + redisPassword.substring(redisPassword.length() - 4);
        } else if (redisPassword != null && !redisPassword.isEmpty()) {
            passwordPreview = "***";
        }
        log.info("Redis Configuration - Host: {}, Port: {}, Password Length: {}, Password: {}",
                 redisHost, redisPort, redisPassword != null ? redisPassword.length() : 0, passwordPreview);

        // Set password
        if (StringUtils.hasText(redisPassword)) {
            redisConfig.setPassword(RedisPassword.of(redisPassword));
            log.info("Redis password set successfully");
        } else {
            log.warn("Redis password is empty or null!");
        }

        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
