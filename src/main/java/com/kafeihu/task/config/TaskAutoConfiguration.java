package com.kafeihu.task.config;

import com.kafeihu.task.launch.TaskCommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * date: 2022/6/13 17:21
 * task框架auto config
 *
 * @author kroulzhang
 */
@Configuration
public class TaskAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "fitboot.task.runner", name = "enabled", havingValue = "true",
            matchIfMissing = true)
    public TaskCommandLineRunner taskCommandLineRunner() {
        return new TaskCommandLineRunner();
    }
}
