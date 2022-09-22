package com.microservice.authservice.configs;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JConfig {
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                                                                        .failureRateThreshold(4) //kích hoạt bộ ngắt khi request lần thứ 4 mà vẫn lỗi
                                                                        .waitDurationInOpenState(Duration.ofMillis(1000)) // thời gian ngắt với request lỗi. ví dụ như request liên tục 1 service lỗi trong 1s thì nó đều trả về kết quả như nháu, tránh lạm dụng request
                                                                        .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                                                                        .slidingWindowSize(2)
                                                                        .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                                                               .timeoutDuration(Duration.ofSeconds(4)) // cài đặt timeout, nếu k có response trong 4s thì tự kích hoạt ngắt
                                                               .build();

        return factory -> factory.configureDefault(
            id -> new Resilience4JConfigBuilder(id).timeLimiterConfig(timeLimiterConfig)
                                                   .circuitBreakerConfig(circuitBreakerConfig)
                                                   .build()
        );
    }
}