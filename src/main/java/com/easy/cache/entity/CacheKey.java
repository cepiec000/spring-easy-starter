package com.easy.cache.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author chendd
 * @Date Created in 2018/11/6 15:09
 * @Description: 缓存key
 */
public class CacheKey implements Serializable {
    private static final long serialVersionUID = 7229320497442357253L;
    private final String nameSpace;
    private final String key;
    private final String hField;
    private final Integer expire;

    public CacheKey(String nameSpace, String key, String hField, Integer expire) {
        if (StringUtils.isNotBlank(nameSpace)) {
            this.nameSpace = nameSpace;
        } else {
            this.nameSpace = "EasyCache";
        }
        this.key = key;
        this.hField = hField;
        this.expire = expire;
    }

    public String getCacheKey() {
        if (StringUtils.isNotBlank(this.nameSpace)) {
            return String.format("%s:%s", this.nameSpace, this.key);
        }
        return this.key;
    }

    /**
     * 用于分布式下，刷新缓存使用
     *
     * @return lock key
     */
    public Serializable getLockKey() {
        StringBuilder key = new StringBuilder(getCacheKey());
        if (StringUtils.isNotBlank(this.hField)) {
            key.append(":").append(hField);
        }
        key.append(":lock");
        return key.toString();
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public String getKey() {
        return key;
    }

    public String gethField() {
        return hField;
    }

    public Integer getExpire() {
        return expire;
    }

    @Override
    public int hashCode() {
        int result = nameSpace != null ? nameSpace.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (hField != null ? hField.hashCode() : 0);
        result = 31 * result + (expire != null ? expire.hashCode() : 0);
        return result;
    }
}
