package com.kafeihu.task.core.statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;


/**
 * date:  2022/8/16 14:55
 * 数据统计：组件的总执行数目，执行成功数目，失败数，每个执行器的耗时统计，整个组件的总耗时
 *
 * @author kroulzhang
 */
public class BaseStatistic implements IStatistic {

    private static final Logger logger = LoggerFactory.getLogger(BaseStatistic.class);
    private volatile int sum = 0;
    private volatile int success = 0;
    private volatile int fail = 0;
    private Map<String, Long> executeTime = new ConcurrentHashMap<>();

    public void setExecuteBeginTime(@NonNull String name, long time) {

        if (executeTime.get(name) == null) {
            executeTime.put(name, time);
        } else {
            logger.warn(name + " is already exist");
        }
    }

    public void setExecuteEndTime(@NonNull String name, long time) {

        if (executeTime.get(name) != null) {
            long start = executeTime.get(name);
            executeTime.put(name, time - start);
        } else {
            logger.warn(name + " is not exist");
        }

    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public int getSumStep() {
        return sum;
    }

    @Override
    public int getRunSuccessStep() {
        return success;
    }

    public void addSuccess(int success) {
        this.success += success;
    }

    @Override
    public int getRunFailStep() {
        return fail;
    }

    public void addFail(int fail) {
        this.fail += fail;
    }

    @Override
    public Map<String, Long> eachRunStepExecuteTime() {
        return executeTime;
    }

    @Override
    public long totalExecuteTime() {
        return executeTime.values().stream().mapToLong(time -> time.longValue()).sum();
    }

    @Override
    public String toString() {
        return "BaseStatistic{"
                + "SumStep=" + getSumStep()
                + ", RunStep=" + getRunStep()
                + ", SuccessRunStep=" + getRunSuccessStep()
                + ", FailRunStep=" + getRunFailStep()
                + ", TotalExecuteTime(ms)=" + totalExecuteTime()
                + ", EachRunStepExecuteTime(ms)=" + eachRunStepExecuteTime()
                + '}';
    }
}
