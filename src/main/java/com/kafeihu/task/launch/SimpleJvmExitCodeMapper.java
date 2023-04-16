package com.kafeihu.task.launch;

import com.kafeihu.task.core.constant.ExitStatus;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * date: 2022/11/3 16:33
 *
 * @author kroulzhang
 */
public class SimpleJvmExitCodeMapper implements ExitCodeMapper {

    protected Log logger = LogFactory.getLog(getClass());

    private Map<String, Integer> mapping;

    public SimpleJvmExitCodeMapper() {
        mapping = new HashMap<>();
        mapping.put(ExitStatus.FINISHED.name(), JVM_EXITCODE_COMPLETED);
        mapping.put(ExitStatus.EXCEPTION.name(), JVM_EXITCODE_GENERIC_ERROR);
        mapping.put(ExitCodeMapper.TASK_NOT_PROVIDED, JVM_EXITCODE_TASK_ERROR);
        mapping.put(ExitCodeMapper.NO_SUCH_TASK, JVM_EXITCODE_TASK_ERROR);
    }

    public Map<String, Integer> getMapping() {
        return mapping;
    }

    /***
     * 设置mapping
     * @param exitCodeMap
     */
    public void setMapping(Map<String, Integer> exitCodeMap) {
        mapping.putAll(exitCodeMap);
    }

    /***
     * 转int value
     * @param exitCode
     * @return
     */
    @Override
    public int intValue(String exitCode) {

        Integer statusCode = null;

        try {
            statusCode = mapping.get(exitCode);
        } catch (RuntimeException ex) {
            // We still need to return an exit code, even if there is an issue
            // with
            // the mapper.
            logger.fatal("Error mapping exit code, generic exit status returned.", ex);
        }

        return (statusCode != null) ? statusCode.intValue() : JVM_EXITCODE_GENERIC_ERROR;
    }
}
