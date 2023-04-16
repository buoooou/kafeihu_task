package com.kafeihu.task.core.constant;


/**
 * date:  2022/5/30 22:04
 * Task常量
 *
 * @author kroulzhang
 */
public enum TaskErrorCode {
    /**
     * 运行job成功
     */
    RUN_SUCCESS(0),
    COMMON_RUN_ERROR(100001),
    TASK_PROCESS_ERROR(200001),
    TASK_WRITER_ERROR(200002),
    TASK_READER_ERROR(200003),
    TASK_HANDLE_ERROR(200003);

    /**
     * 错误码值
     */
    private int value;

    private TaskErrorCode(int value) {
        this.value = value;
    }

    public String getErrorCode() {
        return name();
    }

    public int getErrorValue() {
        return value;
    }

}
