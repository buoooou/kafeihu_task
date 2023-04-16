package com.kafeihu.task.core;


import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.constant.TaskConstant;
import com.kafeihu.task.dependent.IDependentEvaluator;
import com.kafeihu.task.listener.IListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * date:  2022/4/17 14:55
 *
 * TODO 添加chunk模式
 * 批处理支持，分批读取数据，分批处理后，写入
 *
 * @author kroulzhang
 */
@Component
public class UniDependentTask<R, T> extends AbstractTask<R, T> {

    //Task依赖执行器链
    private List<IDependentEvaluator> dependentEvaluators = new CopyOnWriteArrayList<>();
    private TaskParameters taskParameters;

    /**
     * 新增多个依赖条件评估器
     *
     * @param dependentEvaluator
     */
    public void addDependentEvaluators(List<IDependentEvaluator> dependentEvaluator) {
        this.dependentEvaluators.addAll(dependentEvaluator);
    }

    /***
     * 执行多个依赖（无序），评估是否可以执行Task任务
     * 逻辑：所有依赖都满足，则返回true，任一依赖不满足，则返回false
     * @param execution
     * @return
     */
    protected boolean ready(TaskExecution execution) {
        if (!CollectionUtils.isEmpty(dependentEvaluators)) {
            logger.info("Task:[{}] execute dependentEvaluators begin", execution.getCommandParameters());

            for (IDependentEvaluator evaluator : dependentEvaluators) {
                IListener listener = evaluator.listener();
                listener.beforeExecute(execution);
                boolean ready = false;
                try {
                    ready = evaluator.ready(execution);
                } catch (Exception e) {
                    logger.error(String.format("Task:[%s] execute dependentEvaluators failed: reason:[%s]",
                            execution.getCommandParameters(), e.getMessage()), e);
                    listener.onExecuteError(execution, e);
                } finally {
                    listener.afterExecute(execution);
                }
                //有任一依赖不满足，直接返回不执行
                if (!ready) {
                    return false;
                }
            }
        }
        return true;
    }

    /***
     * 选择按照handle（全自定义）模式 or
     * process（reader-process-writer）模式
     * @param execution
     * @throws Exception
     */
    @Override
    public ExitStatus execute(TaskExecution execution) throws Exception {
        //判断前置依赖Task任务是否处理完毕
        if (!ready(execution)) {
            logger.error(execution.getTaskName() + "  dependentEvaluators not ready");
            return ExitStatus.DEPENDENT_FAIL;
        }
        //执行任务处理
        doExecute(execution);
        return ExitStatus.FINISHED;
    }


    @Override
    void beforeExecute(TaskExecution execution) {
        //做内部数据统计使用
        execution.getExecutionContext().put(TaskConstant.RUNNING_NUM, 0);
    }

    @Override
    void afterExecute(ExitStatus status, TaskExecution execution) {
        // 任务展示
        logger.info("Task:[{} {},ExitStatus:{}]", execution.getTaskName(), execution.getTaskParameters(), status);
        //统计数据展示
        //统计数据
        logger.info(
                "-----------------------------Task:[{} {}] :statistic----------------------------------------------",
                execution.getTaskName(), execution.getTaskParameters());
        logger.info("{\"statistic\":{}}", execution.statistic());
        logger.info(
                "-----------------------------Task:[{} {}] :statistic----------------------------------------------",
                execution.getTaskName(), execution.getTaskParameters());
    }

    @Override
    public String toString() {
        return super.toString()
                + "UniDependentTask{"
                + "dependentEvaluators=" + dependentEvaluators
                + '}';
    }

    public void setTaskParameters(TaskParameters taskParameters) {
        this.taskParameters = taskParameters;
    }

    @Override
    public TaskParameters getTaskParameters() {
        return this.taskParameters;
    }
}
