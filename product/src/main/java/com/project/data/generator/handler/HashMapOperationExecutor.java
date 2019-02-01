package com.project.data.generator.handler;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.data.generator.common.Constant;

import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

@Component
public class HashMapOperationExecutor implements RedisExecutor
{
    private static final Logger LOG = LoggerFactory.getLogger(HashMapOperationExecutor.class);

    @Autowired
    private RedisAdvancedClusterCommands<String, String> redisCommands;

    @Value("${com.project.data.generator.number.of.threads:10}")
    private int numOfThreads;

    @Value("${com.project.data.generator.number.of.entries:10}")
    private int entries;

    @Value("${com.project.data.generator.string.key.length:20}")
    private int keyLength;

    @Value("${com.project.data.generator.string.value.length:13}")
    private int valueLength;

    @Value("#{${com.project.data.generator.hashMap.keys.list:'firstSeen,secondSeen'}.split(',')}")
    private List<String> hashMapKeysList;


    @Override
    public String execute()
    {
        int iterations = entries / numOfThreads;

        LOG.info(String.format("%s %d", Constant.ITERATION_SIZE, iterations));

        for (int i = 0; i < iterations; i++)
        {
            String key = RandomStringUtils.random(keyLength, true, false);

            try
            {
                for (int j = 0; j < hashMapKeysList.size(); j++)
                {
                    String value = RandomStringUtils.random(valueLength, true, false);
                    Boolean status = redisCommands.hset(key, hashMapKeysList.get(j), value);
                    LOG.info(String.format("%s %s", Constant.STATUS, status ? Constant.OK : Constant.FAIL));
                }
            }
            catch (Exception e)
            {
                LOG.error(e.getMessage());
            }

        }

        return Constant.EXECUTION_COMPLETED;
    }
}
