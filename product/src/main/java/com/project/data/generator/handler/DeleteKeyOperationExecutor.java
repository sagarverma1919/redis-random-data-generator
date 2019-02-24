package com.project.data.generator.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.data.generator.common.Constant;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;

@Component
public class DeleteKeyOperationExecutor implements RedisExecutor
{
    private static final Logger LOG = LoggerFactory.getLogger(DeleteKeyOperationExecutor.class);

    @Autowired
    private RedisAdvancedClusterAsyncCommands<String, String> redisCommands;

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

        for (int i = 0; i < iterations; )
        {
            try
            {
                RedisFuture<KeyScanCursor<String>> future = redisCommands.scan(cursor, scanArgs);

                KeyScanCursor<String> keyScanCursor = future.get();

                cursor = keyScanCursor;

                if (cursor.getCursor().equals("0"))
                {
                    LOG.info("No matching items found");
                    break;
                }

                if (keyScanCursor.getKeys().size() > 0)
                {
                    RedisFuture<Long> status = redisCommands.del(keyScanCursor.getKeys().stream().toArray(String[]::new));

                    Long deletedItems = status.get();

                    i = (int) (i + deletedItems);

                    LOG.info(String.format("%s %d", Constant.STATUS, deletedItems));
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
