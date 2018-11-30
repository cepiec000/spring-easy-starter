package com.easy.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author chendd
 * @Date Created in 2018/11/30 11:23
 * @Description:
 */
public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET;
    private final JavaType javaType;
    private static final byte[] EMPTY_ARRAY = new byte[0];

    public FastJson2RedisSerializer(Class<T> type) {
        this.javaType = this.getJavaType(type);
    }

    public FastJson2RedisSerializer(JavaType javaType) {
        this.javaType = javaType;
    }

    @Override
    public byte[] serialize(@Nullable T t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        } else {
            try {
                return JSON.toJSONBytes(t, SerializerFeature.DisableCircularReferenceDetect);
            } catch (Exception var3) {
                throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
            }
        }
    }

    @Override
    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        } else {
            try {
                return JSON.parseObject(bytes, this.javaType);
            } catch (Exception var3) {
                throw new SerializationException("Could not read JSON: " + var3.getMessage(), var3);
            }
        }
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    static {
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
    }
}
