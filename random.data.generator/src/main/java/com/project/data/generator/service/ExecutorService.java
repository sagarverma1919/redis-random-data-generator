package com.project.data.generator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.data.generator.common.Constant;
import com.project.data.generator.handler.RedisExecutor;
import com.project.data.generator.utils.ServiceUtils;

@Component
public class ExecutorService
{
    private static final Logger LOG = LoggerFactory.getLogger(ExecutorService.class);


    @Value("${com.project.data.generator.number.of.threads:10}")
    private int numOfThreads;

    @Value("${com.project.data.generator.operation:string}")
    private String operation;

    @Autowired
    private ServiceUtils serviceUtils;


    public void process() throws Exception
    {
        RedisExecutor executorService = serviceUtils.getExecutor(operation);

        MultiThreadProcessor multiThreadProcessor = new MultiThreadProcessor(executorService, operation, numOfThreads);

        multiThreadProcessor.executeInBatches();

        LOG.info(Constant.EXECUTION_COMPLETED);
    }
}
