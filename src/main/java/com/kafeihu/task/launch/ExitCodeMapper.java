package com.kafeihu.task.launch;

/**
 * date: 2022/11/3 16:32
 *
 * @author kroulzhang
 */
public interface ExitCodeMapper {

    static int JVM_EXITCODE_COMPLETED = 0;

    static int JVM_EXITCODE_GENERIC_ERROR = 1;

    static int JVM_EXITCODE_TASK_ERROR = 2;

    public static final String NO_SUCH_TASK = "NO_SUCH_TASK";

    public static final String TASK_NOT_PROVIDED = "TASK_NOT_PROVIDED";


    public int intValue(String exitCode);
}
