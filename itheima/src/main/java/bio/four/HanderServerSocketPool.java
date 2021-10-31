package bio.four;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @title: HanderServerSocketPool
 * @Description // 创建线程池，执行任务
 * @Author Chen
 * @Date: 2021/10/30 11:45
 * @Version 1.0
 */
public class HanderServerSocketPool {

    private ExecutorService executorService;

    public HanderServerSocketPool(int maxThreads, int queueSize){

        /**
         * 创建 线程池，线程池参数
         * int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
         * BlockingQueue<Runnable> workQueue
         */
        executorService = new ThreadPoolExecutor(3, maxThreads, 120,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    }

    /**
     * 将任务放进线程池执行
     * @param task
     */
    public void execute(Runnable task){
        executorService.execute(task);
    }





}
