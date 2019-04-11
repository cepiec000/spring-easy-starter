package com.easy.utils;

import java.util.HashSet;
import java.util.Random;

public class CollageCodeUtil {
    private static Random random = new Random();

    private static Snowflake snowflake = new Snowflake(random.nextInt(32), random.nextInt(32));


    /**
     * 生成交易流水号算法
     *
     * @return
     */
    public static String getCollageCode() {
        return snowflake.nextID64();
    }

    public static void main(String[] args) throws Exception {
        HashSet set = new HashSet();
        for (int i = 0; i < 100000; i++) {
            String code = getCollageCode();
           if( set.contains(code)){
               System.out.println("ERRORcode"+code);
           }else {
               set.add(code);
           }

        }
        System.out.println("end");
    }
}
