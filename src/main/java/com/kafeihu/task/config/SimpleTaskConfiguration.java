package com.kafeihu.task.config;

import com.kafeihu.task.core.UniDependentTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * date: 2022/8/10 18:25
 * task框架配置
 *
 * @author kroulzhang
 */
@Configuration
public class SimpleTaskConfiguration {


    @Bean
    public UniDependentTask uniDependentTask() {
        return new UniDependentTask();
    }

}
