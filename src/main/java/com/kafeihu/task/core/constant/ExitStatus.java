package com.kafeihu.task.core.constant;


/**
 * date:  2022/5/6 16:34
 * 任务状态，已结束，或者异常
 *
 * @author kroulzhang
 */
public enum ExitStatus {

    INIT(false),
    EXCEPTION(false),
    DEPENDENT_FAIL(false),
    UNKNOWN(false),
    FINISHED(true);


    private final boolean continuable;

    private ExitStatus(boolean continuable) {
        this.continuable = continuable;
    }

    public boolean isFinished() {
        return this == FINISHED;
    }
}
