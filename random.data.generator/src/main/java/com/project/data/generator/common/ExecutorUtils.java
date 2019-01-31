package com.project.data.generator.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ExecutorUtils
{
    public static ExecutorService createExecutor(String namePrefix, int numThreads)
    {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(namePrefix + "-%d")
                .build();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads, threadFactory);
        return MoreExecutors.getExitingExecutorService(executor);
    }
}
