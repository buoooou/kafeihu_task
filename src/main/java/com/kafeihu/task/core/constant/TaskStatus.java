package com.kafeihu.task.core.constant;


/**
 * date:  2022/5/22 15:16
 * Task状态
 *
 * @author kroulzhang
 */
public enum TaskStatus {

    COMPLETED,
    STARTING,
    STARTED,
    STOPPING,
    STOPPED,
    FAILED,
    UNKNOWN;


    public TaskStatus upgradeTo(TaskStatus other) {
        if (isGreaterThan(STARTED) || other.isGreaterThan(STARTED)) {
            return max(this, other);
        }
        // Both less than or equal to STARTED
        if (this == COMPLETED || other == COMPLETED) {
            return COMPLETED;
        }
        return max(this, other);
    }

    public boolean isGreaterThan(TaskStatus other) {

        return this.compareTo(other) > 0;
    }

    public static TaskStatus max(TaskStatus status1, TaskStatus status2) {
        
        return status1.isGreaterThan(status2) ? status1 : status2;
    }

}
