package com.project.data.generator.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.data.generator.common.Constant;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

@Component
public class DeleteKeyOperationExecutor implements RedisExecutor
{
    private static final Logger LOG = LoggerFactory.getLogger(DeleteKeyOperationExecutor.class);

    @Autowired
    private RedisAdvancedClusterCommands<String, String> redisCommands;

    @Value("${com.project.data.generator.number.of.threads:10}")
    private int numOfThreads;

    @Value("${com.project.data.generator.number.of.entries:100}")
    private int entries;

    @Value("${com.project.data.generator.deletion.key.prefix:''}")
    private String keyPrefix;

    @Override
    public String execute()
    {
        int iterations = entries / numOfThreads;
        LOG.info(String.format("%s %d", Constant.ITERATION_SIZE, iterations));

        ScanCursor cursor = ScanCursor.INITIAL;
        ScanArgs scanArgs = ScanArgs.Builder.matches(keyPrefix);

        for (int i = 0; i < iterations; i++)
        {
            try
            {
                KeyScanCursor<String> keyScanCursor = redisCommands.scan(cursor, scanArgs);


                Long status = redisCommands.del(keyScanCursor.getKeys().stream().toArray(String[]::new));
                if (status != keyScanCursor.getKeys().size())
                {
                    cursor = new ScanCursor(keyScanCursor.getCursor(), false);
                }

                i = (int) (i + status - 1);

                LOG.info(String.format("%s %d", Constant.STATUS, status));
            }
            catch (Exception e)
            {
                LOG.error(e.getMessage());
            }

        }

        return Constant.EXECUTION_COMPLETED;
    }
}
