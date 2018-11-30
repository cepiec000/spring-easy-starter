package com.easy.lock.config;

import com.easy.lock.config.ELockConfig;
import com.easy.lock.lock.LockFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author chendd
 * @Date Created in 2018/11/19 11:17
 * @Description:
 */
@Configuration
@ComponentScan({"com.easy.lock"})
@EnableConfigurationProperties(ELockConfig.class)
public class LockAutoConfiguration {

    private static final String DEFAULT_HOST = "localhost";

    @Autowired
    private ELockConfig elockConfig;

    @Bean
    LockFactory createLockFactory() throws Exception {
        RedissonClient client = null;
        if (elockConfig == null) {
            return null;
        }
        if (elockConfig.getHost().equals(DEFAULT_HOST) &&
                elockConfig.getCluster() == null &&
                elockConfig.getSentinel() == null) {
            return null;
        }
        if (elockConfig.getSentinel() != null) {
            client = redissonSentinel();
        } else if (elockConfig.getCluster() != null) {
            client = redissonCluster();
        } else {
            client = redissonSingle();
        }
        if (client != null) {
            return new LockFactory(client);
        }
        return null;
    }

    /**
     * 哨兵模式
     *
     * @return
     * @throws Exception
     */
    private RedissonClient redissonSentinel() throws Exception {
        Config config = new Config();
        SentinelServersConfig serverConfig = config.useSentinelServers();
        serverConfig.addSentinelAddress(elockConfig.getSentinel().getArrayNodes());
        serverConfig.setMasterName(elockConfig.getSentinel().getMaster());

        if (StringUtils.isNotBlank(elockConfig.getPassword())) {
            serverConfig.setPassword(elockConfig.getPassword());
        }
        serverConfig.setDatabase(elockConfig.getDatabase());
        config.setCodec(new JsonJacksonCodec());
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    /**
     * 单机模式
     *
     * @return
     * @throws Exception
     */
    private RedissonClient redissonSingle() throws Exception {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(elockConfig.getHost());
        if (elockConfig.getJedis() != null && elockConfig.getJedis().getPool() != null) {
            serverConfig.setConnectionPoolSize(elockConfig.getJedis().getPool().getMaxActive())
                    .setConnectionMinimumIdleSize(elockConfig.getJedis().getPool().getMinIdle());
        }

        if (StringUtils.isNotBlank(elockConfig.getPassword())) {
            serverConfig.setPassword(elockConfig.getPassword());
        }
        config.setCodec(new JsonJacksonCodec());
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

    /**
     * 集群模式
     *
     * @return
     * @throws Exception
     */
    private RedissonClient redissonCluster() throws Exception {
        Config config = new Config();
        ClusterServersConfig serverConfig = config.useClusterServers().addNodeAddress(elockConfig.getCluster().getArrayNodes());
        if (elockConfig.getJedis() != null && elockConfig.getJedis().getPool() != null) {
            serverConfig.setMasterConnectionPoolSize(elockConfig.getJedis().getPool().getMaxActive())
                    .setSlaveConnectionPoolSize(elockConfig.getJedis().getPool().getMinIdle());
        }
        if (StringUtils.isNotBlank(elockConfig.getPassword())) {
            serverConfig.setPassword(elockConfig.getPassword());
        }
        config.setCodec(new JsonJacksonCodec());
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

}
