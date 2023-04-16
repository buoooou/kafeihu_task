package com.kafeihu.task.core.constant;


/**
 * date:  2022/5/22 15:16
 * Task版本
 *
 * @author kroulzhang
 */
public class Version {

    public static final int MAJOR_VERSION = 1;
    public static final int MINOR_VERSION = 1;
    public static final int PATCH_VERSION = 3;
    public static final String VERSION_SUFFIX = "-SNAPSHOT";

    public Version() {
    }

    public static String version() {
        return String.format("v%s.%s.%s", MAJOR_VERSION, MINOR_VERSION, PATCH_VERSION);
    }
}
