package com.easy.utils;

public class NumericConvertUtils {

    /**
     *     * 在进制表示中的字符集合，0-Z分别用于表示最大为62进制的符号表示
     *    
     */
    private static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     *     * 将十进制的数字转换为指定进制的字符串
     *     * @param number 十进制的数字
     *     * @param seed   指定的进制
     *     * @return 指定进制的字符串
     *    
     */
    public static String toOtherNumberSystem(long number, int seed) {
        if (number < 0) {
            number = ((long) 2 * 0x7fffffff) + number + 2;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((number / seed) > 0) {
            buf[--charPos] = digits[(int) (number % seed)];
            number /= seed;
        }
        buf[--charPos] = digits[(int) (number % seed)];
        return new String(buf, charPos, (32 - charPos));
    }

    /**
     *     * 将其它进制的数字（字符串形式）转换为十进制的数字
     *     * @param number 其它进制的数字（字符串形式）
     *     * @param seed   指定的进制，也就是参数str的原始进制
     *     * @return 十进制的数字
     *    
     */
    public static long toDecimalNumber(String number, int seed) {
        char[] charBuf = number.toCharArray();
        if (seed == 10) {
            return Long.parseLong(number);
        }

        long result = 0, base = 1;

        for (int i = charBuf.length - 1; i >= 0; i--) {
            int index = 0;
            for (int j = 0, length = digits.length; j < length; j++) {
                //找到对应字符的下标，对应的下标才是具体的数值
                if (digits[j] == charBuf[i]) {
                    index = j;
                }
            }
            result += index * base;
            base *= seed;
        }
        return result;
    }

//    public static void main(String[] args) {
//        Snowflake snowFlake = new Snowflake(2, 3);
//
//        for (int i = 0; i < (1 << 4); i++) {
////10进制
//            Long id = snowFlake.nextId();
////62进制
//            String convertedNumStr = NumericConvertUtils.toOtherNumberSystem(id, 62);
//
////10进制转化为62进制
//            System.out.println("10进制：" + id + "  62进制短地址:" + convertedNumStr);
//
//
////62进制转化为10进制
//            System.out.println("62进制短地址：" + convertedNumStr + "  10进制:" + NumericConvertUtils.toDecimalNumber(convertedNumStr, 62));
//            System.out.println();
//        }
//    }
}
