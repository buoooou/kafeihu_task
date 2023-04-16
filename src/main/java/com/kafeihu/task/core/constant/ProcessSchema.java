package com.kafeihu.task.core.constant;


/**
 * date:  2022/8/6 16:16
 * process模式
 *
 * @author kroulzhang
 */
public enum ProcessSchema {

    ALL_BY_ONE_PROCESS("all_by_one"),
    ONE_BY_ONE_PROCESS("one_by_one");

    private String value;

    private ProcessSchema(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
