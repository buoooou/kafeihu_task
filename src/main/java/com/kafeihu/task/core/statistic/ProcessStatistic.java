package com.kafeihu.task.core.statistic;


/**
 * date:  2022/8/20 00:10
 * process 数据统计
 *
 * @author kroulzhang
 */
public class ProcessStatistic extends BaseStatistic {

    private volatile int run = 0;

    public void addRun(int num) {
        this.run += num;
    }

    @Override
    public int getRunStep() {
        return run;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
