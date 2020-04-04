package com.sn.theft.util;

import java.util.Random;

/**
 * @author songning
 * @date 2019/8/29
 * description 获取随机数
 */
public class RandomUtil {

    /**
     * 生成 “ min <= 随机数 <= max ” 的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static String getRandom(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return String.valueOf(randomNum);
    }
}
