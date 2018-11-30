package com.easy.lock.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * @author chendd
 * @Date Created in 2018/11/19 14:29
 * @Description:
 */
@ConfigurationProperties(ELockConfig.PREFIX)
public class ELockConfig {
    public static final String PREFIX = "spring.redis";
    private int database = 0;
    private String url;
    private String host = "localhost";
    private String password;
    private int port = 6379;
    private boolean ssl;
    private Duration timeout;
    private Sentinel sentinel;
    private Cluster cluster;
    private final Jedis jedis = new Jedis();

    public ELockConfig() {
    }

    public int getDatabase() {
        return this.database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Duration getTimeout() {
        return this.timeout;
    }

    public Sentinel getSentinel() {
        return this.sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Jedis getJedis() {
        return this.jedis;
    }


    public static class Jedis {
        private Pool pool;

        public Jedis() {
        }

        public Pool getPool() {
            return this.pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }
    }

    public static class Sentinel {
        private String master;
        private List<String> nodes;
        private String[] arrayNodes;

        public Sentinel() {
        }

        public String getMaster() {
            return this.master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public String[] getArrayNodes() {
            if (this.nodes == null || this.nodes.size() == 0) {
                return null;
            }
            for (int i = 0; i < this.nodes.size(); i++) {
                arrayNodes[i] = "redis://"+this.nodes.get(i);
            }
            return arrayNodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
            if (nodes != null && nodes.size() > 0) {
                this.arrayNodes = new String[this.nodes.size()];
            }
        }
    }

    public static class Cluster {
        private List<String> nodes;
        private Integer maxRedirects;
        private String[] arrayNodes;

        public Cluster() {
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
            if (nodes != null && nodes.size() > 0) {
                this.arrayNodes = new String[this.nodes.size()];
            }
        }

        public String[] getArrayNodes() {
            if (this.nodes == null || this.nodes.size() == 0) {
                return null;
            }
            for (int i = 0; i < this.nodes.size(); i++) {
                arrayNodes[i] = "redis://"+this.nodes.get(i);
            }
            return arrayNodes;
        }

        public Integer getMaxRedirects() {
            return this.maxRedirects;
        }

        public void setMaxRedirects(Integer maxRedirects) {
            this.maxRedirects = maxRedirects;
        }
    }

    public static class Pool {
        private int maxIdle = 8;
        private int minIdle = 0;
        private int maxActive = 8;
        private Duration maxWait = Duration.ofMillis(-1L);

        public Pool() {
        }

        public int getMaxIdle() {
            return this.maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return this.minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return this.maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public Duration getMaxWait() {
            return this.maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }
    }
}
