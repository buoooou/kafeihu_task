package com.kafeihu.task.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

/**
 * date: 2022/9/1 14:11
 * 版本更新标签
 *
 * @author kroulzhang
 */
public class TaskVersionTips implements ImportAware {

    private static final Logger logger = LoggerFactory.getLogger(TaskVersionTips.class);

    public TaskVersionTips() {
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        String logo = "\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n"
                + " Task v1.1.3 更新周知：\n"
                + " 1.handle模式废弃\n"
                + " 2.dependent废弃\n"
                + "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *";
        logger.info(logo);
    }

}
