package com.project.data.generator.handler;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Value("${com.project.data.generator.key.prefix:''}")
    private String keyPrefix;

    @Override
    public String execute()
    {

        int iterations = entries / numOfThreads;
        LOG.info(String.format("%s %d", Constant.ITERATION_SIZE, iterations));

        int length = keyLength;
        if (!StringUtils.isBlank(keyPrefix))
        {
            length = keyLength - keyPrefix.length();

        }

        for (int i = 0; i < iterations; i++)
        {
            String key = keyPrefix.concat(RandomStringUtils.random(length, true, false));
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
