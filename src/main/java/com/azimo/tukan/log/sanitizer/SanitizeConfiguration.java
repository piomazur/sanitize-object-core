package com.azimo.tukan.log.sanitizer;

import com.azimo.tukan.log.sanitizer.core.SanitizeAspect;
import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class SanitizeConfiguration {

    @Bean
    public SanitizeAspect sanitizeAspect() {
        return Aspects.aspectOf(SanitizeAspect.class);
    }
}
