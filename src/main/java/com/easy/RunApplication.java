package com.easy;

import com.easy.cache.EnableEasyCache;
import com.easy.lock.EnableEasyLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chendd
 * @Date Created in 2018/11/14 15:50
 * @Description:
 */
@SpringBootApplication
@EnableEasyCache
public class RunApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
    }

}
