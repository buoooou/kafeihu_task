package com.kafeihu.task.launch;

import com.kafeihu.task.builder.TaskParametersBuilder;
import com.kafeihu.task.core.ITask;
import com.kafeihu.task.core.TaskParameters;
import com.kafeihu.task.core.constant.ExitStatus;
import com.kafeihu.task.core.constant.TaskConstant;
import com.kafeihu.task.core.exception.TaskProcessException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.util.Assert;

/**
 * date: 2022/4/28 16:10
 * Task 任务启动入口
 * *         离线批处理
 * *         ----job进程维度触发
 * *         ----常驻进程 todo 任务调度、管理、触发
 *
 * @author kroulzhang
 */
public class TaskCommandLineRunner implements CommandLineRunner, Ordered, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TaskCommandLineRunner.class);
    private static final List<String> VALID_OPTS = Arrays.asList(new String[]{"-T", "-O"});
    private int order = TaskConstant.DEFAULT_ORDER;
    private Collection<ITask> tasks = Collections.emptySet();
    @Autowired
    private ApplicationContext applicationContext;

    private ExitCodeMapper exitCodeMapper = new SimpleJvmExitCodeMapper();

    private TaskLauncher launcher;

    public void setLauncher(TaskLauncher launcher) {
        this.launcher = launcher;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Autowired(required = false)
    public void setTasks(Collection<ITask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Running default command line with: " + Arrays.asList(args));
        launchTaskFromProperties(Arrays.asList(args));
    }

    /***
     * 加载参数并启动Task
     * @param props
     */
    protected void launchTaskFromProperties(List<String> props) {
        int result;
        try {
            if (props == null || props.isEmpty()) {
                throw new TaskProcessException("args not null");
            }

            Object task = applicationContext.getBean(props.get(0));

            Assert.notNull(task, String.format(
                    "taskName:%s is invalid, Please check the parameters of the job, parameters:[%s]",
                    props.get(0), props));
            Assert.isInstanceOf(ITask.class, task, "The task must instanceof ITask.");

            TaskCommandParameters taskParameters = convertParameters(props);

            TaskParameters taskParameter = new TaskParametersBuilder()
                    .getTaskParameters((ITask) task)
                    .addCommondParameters(taskParameters)
                    .toTaskParameters();
            Assert.state(launcher != null, "A TaskLauncher must be provided.  Please add one to the configuration.");

            launcher.run((ITask) task, props.get(0), taskParameter);

            result = exitCodeMapper.intValue(ExitStatus.FINISHED.name());
        } catch (Throwable e) {
            String message = "Job Terminated in error: " + e.getMessage();
            logger.error(message, e);
            result = exitCodeMapper.intValue(ExitStatus.EXCEPTION.name());
        }

        int finalResult = result;
        System.exit(SpringApplication.exit(applicationContext, () -> finalResult));
    }


    /***
     * 解析参数，后续可以指定ops，处理不同模式，热更新等
     * @param props
     * @return
     */
    protected TaskCommandParameters convertParameters(List<String> props) {
        logger.info("Task:[{}] convert parameter begin", props);
        List<String> parameters = new ArrayList<>(props.size() - 1);
        //过滤job调度平台最后两个参数（调度job_agent和调度id）
        for (int i = 1; i < props.size(); i++) {
            parameters.add(props.get(i));
        }
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < parameters.size(); i += 2) {
            if (parameters.size() < 2 || i + 1 >= parameters.size()) {
                params.put(parameters.get(i), "");
            } else {
                params.put(parameters.get(i), parameters.get(i + 1));
            }
        }
        TaskCommandParameters taskParameters = new TaskCommandParameters(params);
        logger.info("Task:[{}] convert parameter end", taskParameters);
        return taskParameters;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (launcher == null) {
            logger.info("No TaskLauncher has been set, defaulting to synchronous executor.");
            launcher = new SimpleTaskLauncher();
            launcher.setTaskExecutor(new SyncTaskExecutor());
        }
    }

}
