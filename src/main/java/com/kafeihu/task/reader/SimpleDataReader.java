package com.kafeihu.task.reader;

import com.kafeihu.task.core.TaskExecution;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

/**
 * date:  2022/7/15 14:33
 * 简易数据读取器
 *
 * @param <T> 写数据结构体
 * @param <R> 读数据结构体
 * @author kroulzhang
 */
public class SimpleDataReader<T, R> extends AbstractDataReader<R> {

    /**
     * 数据库分页查询所使用的事务管理器
     */
    private PlatformTransactionManager transactionManager;
    /**
     * 数据库分页查询所使用的事务传递级别,默认总是新起一个单独的事务
     */
    private Propagation propagation = Propagation.REQUIRES_NEW;
    /**
     * 数据库分页查询所使用的事务隔离级别,默认要求可重复读
     */
    private Isolation isolation = Isolation.REPEATABLE_READ;
    /**
     * 数据库分页查询所使用的事务超时时间,默认不超时
     */
    private int transactionTimeout = DefaultTransactionAttribute.TIMEOUT_DEFAULT;

    /***
     * 通用数据查询函数
     */
    private DataQueryFunction<T, R> queryFunction;

    private T params;

    public void setQueryParams(T params) {
        this.params = params;
    }

    public T getParams() {
        return params;
    }

    public void setQueryFunction(DataQueryFunction<T, R> queryFunction) {
        this.queryFunction = queryFunction;
    }

    /**
     * 设置数据库分页查询所使用的事务管理器
     *
     * @param transactionManager
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 获取分页查询的事务属性
     *
     * @return
     */
    private DefaultTransactionAttribute getTransactionAttribute() {
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setPropagationBehavior(propagation.value());
        attribute.setIsolationLevel(isolation.value());
        attribute.setTimeout(transactionTimeout);

        return new DefaultTransactionAttribute(attribute) {
            /**
             * 任何异常均回滚
             *
             * @param ex
             * @return
             */
            @Override
            public boolean rollbackOn(Throwable ex) {
                return true;
            }
        };
    }

    @Override
    public void beforeRead(TaskExecution execution) {
        Assert.isNull(params, "query params must not be null");
    }

    @Override
    public List<R> doRead(TaskExecution execution) throws Exception {
        //读取数据
        List<R> queryResult = new ArrayList<>();

        if (transactionManager == null) {
            queryResult.addAll(queryFunction.query(params));
        } else {
            DefaultTransactionAttribute transactionAttribute = getTransactionAttribute();

            queryResult.addAll(new TransactionTemplate(transactionManager, transactionAttribute)
                    .execute(transactionStatus -> {
                        try {
                            return queryFunction
                                    .query(params);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }));

        }

        return queryResult;
    }
}
