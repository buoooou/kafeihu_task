package com.kafeihu.task.builder;

import com.kafeihu.task.core.ITask;
import com.kafeihu.task.core.TaskParameters;
import com.kafeihu.task.launch.TaskCommandParameters;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.util.Assert;

/**
 * date:  2022/8/24 17:44
 * Task参数生成器
 *
 * @author kroulzhang
 */
public class TaskParametersBuilder {

    private Map<String, Object> parameterMap;
    private Map<String, Object> commondParameterMap;

    public TaskParametersBuilder() {
        this.parameterMap = new LinkedHashMap<>();
        this.commondParameterMap = new LinkedHashMap<>();
    }

    public TaskParametersBuilder addString(String key, String parameter) {
        this.parameterMap.put(key, parameter);
        return this;
    }

    public TaskParametersBuilder addLong(String key, Long parameter) {
        this.parameterMap.put(key, parameter);
        return this;
    }

    public TaskParametersBuilder addDouble(String key, Double parameter) {
        this.parameterMap.put(key, parameter);
        return this;
    }

    public TaskParametersBuilder addLocalDateTime(String key, LocalDateTime parameter) {
        this.parameterMap.put(key, parameter);
        return this;
    }

    public TaskParametersBuilder addCommondParameters(TaskCommandParameters taskCommandParameters) {
        Assert.notNull(taskCommandParameters, "taskCommandParameters must not be null");

        this.parameterMap.putAll(taskCommandParameters.getParameters());
        this.commondParameterMap.putAll(taskCommandParameters.getParameters());
        return this;
    }

    public TaskParametersBuilder getTaskParameters(ITask task) {
        Assert.notNull(task, "task must not be null");
        if (task.getTaskParameters() == null) {
            return this;
        }
        TaskParameters taskParameters = task.getTaskParameters();
        // start with parameters from the incrementer
        Map<String, Object> nextParametersMap = new HashMap<>(taskParameters.getParameters());
        // append new parameters (overriding those with the same key)
        nextParametersMap.putAll(this.parameterMap);
        this.parameterMap = nextParametersMap;
        return this;
    }

    public TaskParameters toTaskParameters() {
        TaskParameters parameters = new TaskParameters(this.parameterMap);
        parameters.setTaskCommondParameters(commondParameterMap);
        return parameters;
    }
}
