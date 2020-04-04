package com.sn.theft.util.classconvert;


import com.sn.theft.util.classconvert.service.IClassConvert;

/**
 * @author songning
 * @date 2020/1/21
 * description
 */
public class ClassConvertContext {
    public static IClassConvert getInstance(Class src, Class target) {
        IClassConvert iClassConvert = null;
        String key = src.getSimpleName() + "2" + target.getSimpleName();
        Class<? extends IClassConvert> strategy = ClassConvertEnum.getStrategy(key);
        try {
            assert strategy != null;
            iClassConvert = strategy.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iClassConvert;
    }
}
