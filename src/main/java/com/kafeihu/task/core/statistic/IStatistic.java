package com.kafeihu.task.core.statistic;

import java.util.Map;


/**
 * date:  2022/8/16 14:30
 * 数据统计：组件的总执行数目，执行成功数目，失败数，每个执行器的耗时统计，整个组件的总耗时
 *
 * @author kroulzhang
 */
public interface IStatistic {

    /***
     * 总组件数目
     * @return
     */
    int getSumStep();

    /***
     * 总运行组件数目
     * @return
     */
    default int getRunStep() {
        return getSumStep();
    }

    /***
     * 总成功组件数目
     * @return
     */
    int getRunSuccessStep();

    /***
     * 总运行
     * @return
     */
    int getRunFailStep();

    /***
     * 每个组件耗时
     * @return
     */
    Map<String, Long> eachRunStepExecuteTime();

    /***
     * 总耗时
     * @return
     */
    long totalExecuteTime();

    /***
     * 统计
     * @return
     */
    default String statistic() {
        StringBuilder statistic = new StringBuilder();
        statistic
                .append("{\"IStatistic\":{")
                .append("\"SumStep\":").append(getSumStep())
                .append(",\"RunStep\":").append(getRunStep())
                .append(",\"SuccessRunStep\":").append(getRunSuccessStep())
                .append(",\"FailRunStep\":").append(getRunFailStep())
                .append(",\"EachRunStepExecuteTime(ms)\":[");

        if (eachRunStepExecuteTime().size() == 0) {
            statistic.append("{},");
        } else {
            for (String key : eachRunStepExecuteTime().keySet()) {
                statistic.append("{\"").append(key).append("\":").append(eachRunStepExecuteTime().get(key)).append("}")
                        .append(",");
            }
        }
        statistic.deleteCharAt(statistic.lastIndexOf(","));
        statistic.append("],\"TotalExecuteTime(ms)\":").append(totalExecuteTime()).append("}}");

        return statistic.toString();
    }

}
