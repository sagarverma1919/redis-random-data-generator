package com.project.data.generator.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;

@Configuration
public class RedisConfiguration
{

    @Value("${com.project.data.generator.redis.uris}")
    private String redisURIs;

    @Value("${com.project.data.generator.redis.cluster.maxRedirects:3}")
    private int maxRedirects;

    @Value("${com.project.data.generator.redis.cluster.slave.read:false}")
    private boolean slaveRead;

    @Value("${com.project.data.generator.redis.connection.timeout:5000}")
    private long redisConnectionTimeout;

    @Value("${com.project.data.generator.redis.socket.timeout:5000}")
    private long redisSocketTimeout;

    @Value("${com.project.data.generator.redis.cluster.topology.refreshPeriod:60}")
    private long redisClusterTopologyRefreshPeriod;


    @Bean
    public RedisAdvancedClusterCommands<String, String> syncCommands()
    {
        List<RedisURI> redisURIS = redisURIS();
        RedisClusterClient clusterClient = RedisClusterClient.create(redisURIS);
        clusterClient.setOptions(ClusterClientOptions.builder().maxRedirects(maxRedirects).build());
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(redisClusterTopologyRefreshPeriod))
                .enableAllAdaptiveRefreshTriggers()
                .build();

        clusterClient.setOptions(ClusterClientOptions.builder()
                                         .topologyRefreshOptions(topologyRefreshOptions)
                                         .build());

        RedisAdvancedClusterCommands<String, String> syncCommands = null;
        StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
        if (slaveRead)
        {
            connection.setReadFrom(ReadFrom.SLAVE);
        }
        syncCommands = connection.sync();
        syncCommands.setTimeout(Duration.ofMillis(redisSocketTimeout));
        return syncCommands;
    }

    @Bean
    public RedisAdvancedClusterAsyncCommands<String, String> asyncCommands()
    {
        List<RedisURI> redisURIS = redisURIS();
        RedisClusterClient clusterClient = RedisClusterClient.create(redisURIS);
        clusterClient.setOptions(ClusterClientOptions.builder().maxRedirects(maxRedirects).build());
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(redisClusterTopologyRefreshPeriod))
                .enableAllAdaptiveRefreshTriggers()
                .build();

        clusterClient.setOptions(ClusterClientOptions.builder()
                                         .topologyRefreshOptions(topologyRefreshOptions)
                                         .build());

        RedisAdvancedClusterAsyncCommands<String, String> asyncCommands = null;
        StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
        if (slaveRead)
        {
            connection.setReadFrom(ReadFrom.SLAVE);
        }
        asyncCommands = connection.async();
        asyncCommands.setTimeout(Duration.ofMillis(redisSocketTimeout));
        return asyncCommands;
    }

    private List<RedisURI> redisURIS()
    {
        List<RedisURI> redisURIS = new ArrayList<>();
        for (String hostAndPort : redisURIs.split(","))
        {
            String host = hostAndPort.split(":")[0];
            int port = Integer.parseInt(hostAndPort.split(":")[1]);
            RedisURI redisUri = RedisURI.Builder.redis(host).withPort(port).withTimeout(Duration.ofMillis
                    (redisConnectionTimeout)).build();
            redisURIS.add(redisUri);
        }
        return redisURIS;
    }
}
