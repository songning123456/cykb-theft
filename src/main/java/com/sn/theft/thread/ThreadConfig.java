package com.sn.theft.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: songning
 * @date: 2020/2/19 22:49
 */
@Configuration
public class ThreadConfig {

    /**
     * 获取 当前在线blogger 的线程池
     *
     * @return
     */
    @Bean(name = "CategoryExecutor")
    public Executor categoryExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = 100;
        executor.setCorePoolSize(corePoolSize);
        int maxPoolSize = 100;
        executor.setMaxPoolSize(maxPoolSize);
        int queueCapacity = 200;
        executor.setQueueCapacity(queueCapacity);
        int keepAliveSecond = 60 * 10;
        executor.setKeepAliveSeconds(keepAliveSecond);
        String threadNamePrefix = "TheftAsync_";
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        int awaitTerminationSeconds = 60 * 10;
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        return executor;
    }

    @Bean(name = "PageExecutor")
    public Executor pageExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100 * 4);
        executor.setMaxPoolSize(100 * 4);
        executor.setQueueCapacity(200 * 3);
        executor.setKeepAliveSeconds(60 * 10);
        executor.setThreadNamePrefix("PageAsync_");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60 * 10);
        return executor;
    }

    @Bean(name = "NovelsExecutor")
    public Executor novelsExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100 * 10);
        executor.setMaxPoolSize(100 * 20);
        executor.setQueueCapacity(200 * 10);
        executor.setKeepAliveSeconds(60 * 100);
        executor.setThreadNamePrefix("NovelsAsync_");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60 * 100);
        return executor;
    }

    @Bean(name = "ListExecutor")
    public Executor listExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100 * 50);
        executor.setMaxPoolSize(100 * 50);
        executor.setQueueCapacity(200 * 20);
        executor.setKeepAliveSeconds(60 * 50);
        executor.setThreadNamePrefix("NovelsAsync_");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60 * 50);
        return executor;
    }

    @Bean(name = "ChaptersExecutor")
    public Executor chaptersExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100 * 100 * 5);
        executor.setMaxPoolSize(100 * 100 * 10);
        executor.setQueueCapacity(200 * 100 * 5);
        executor.setKeepAliveSeconds(60 * 100);
        executor.setThreadNamePrefix("ChaptersAsync_");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60 * 100);
        return executor;
    }
}
