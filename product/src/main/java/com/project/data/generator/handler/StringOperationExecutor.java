package com.project.data.generator.handler;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.data.generator.common.Constant;

import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

@Component
public class StringOperationExecutor implements RedisExecutor
{
    private static final Logger LOG = LoggerFactory.getLogger(StringOperationExecutor.class);

    @Autowired
    private RedisAdvancedClusterCommands<String, String> redisCommands;

    @Value("${com.project.data.generator.number.of.threads:10}")
    private int numOfThreads;

    @Value("${com.project.data.generator.number.of.entries:100}")
    private int entries;

    @Value("${com.project.data.generator.string.key.length:20}")
    private int keyLength;

    @Value("${com.project.data.generator.string.value.length:13}")
    private int valueLength;

    @Override
    public String execute()
    {

        int iterations = entries / numOfThreads;

        for (int i = 0; i < iterations; i++)
        {
            String key = RandomStringUtils.random(keyLength, true, false);
            String value = RandomStringUtils.random(valueLength, true, false);

            try
            {
                String status = redisCommands.set(key, value);
                LOG.info(String.format("%s %s", Constant.STATUS, status));
            }
            catch (Exception e)
            {
                LOG.error(e.getMessage());
            }

        }

        return Constant.EXECUTION_COMPLETED;

    }
}
