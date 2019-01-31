package com.project.data.generator.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.data.generator.common.ExecutorUtils;
import com.project.data.generator.handler.RedisExecutor;

public class MultiThreadProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(MultiThreadProcessor.class);

    private final ExecutorService service;

    private final int numOfThreads;

    private final RedisExecutor redisExecutor;

    public MultiThreadProcessor(RedisExecutor redisExecutor, String name, int numOfThreads)
    {
        this.service = ExecutorUtils.createExecutor(name, numOfThreads);
        this.redisExecutor = redisExecutor;
        this.numOfThreads = numOfThreads;
    }

    public void executeInBatches() throws InterruptedException
    {
        List<String> result = new ArrayList<>();

        for (Future<String> f : service.invokeAll(toCallable()))
        {
            try
            {
                result.add(f.get());
            }
            catch (Exception e)
            {
                LOG.error(e.getMessage());
            }
        }
    }

    private Collection<Callable<String>> toCallable()
    {
        Collection<Callable<String>> messageBatch = new ArrayList<>();

        for (int i = 0; i < numOfThreads; i++)
        {
            messageBatch.add(() -> redisExecutor.execute());

        }
        return messageBatch;
    }

}
