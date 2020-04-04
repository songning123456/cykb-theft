package com.sn.theft.util;

import com.sn.theft.annotation.AClassConvert;
import com.sn.theft.util.classconvert.ClassConvertContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author sn
 */
public class ClassConvertUtil {

    /**
     * 将dto和entity之间的属性互相转换,dto中属性一般为String等基本类型
     * 但是entity中可能有复合主键等复杂类型,需要注意同名问题
     *
     * @param src
     * @param target
     */
    public static void populate(Object src, Object target) {

        // 获取当前类定义的所有方法，不包括父类和接口的
        Method[] srcMethods = src.getClass().getDeclaredMethods();
        Method[] targetMethods = target.getClass().getDeclaredMethods();

        for (Method srcMethod : srcMethods) {
            String srcMethodName = srcMethod.getName();
            if (srcMethodName.startsWith("get")) {
                try {
                    Object result = srcMethod.invoke(src);
                    // 根据方法名获取字段，然后获取字段上的注解
                    String srcFieldName = srcMethodName.substring(3, 4).toLowerCase() + srcMethodName.substring(4);
                    Field srcField = src.getClass().getDeclaredField(srcFieldName);
                    AClassConvert aClassConvert = srcField.getAnnotation(AClassConvert.class);
                    // 获取 get 方法返回值类型
                    Class<?> srcConvertClass = srcMethod.getReturnType();

                    for (Method targetMethod : targetMethods) {
                        // 如果某个字段存在此注解，则跳过转换
                        if (aClassConvert != null && aClassConvert.ignore()) {
                            continue;
                        }
                        String targetMethodName = targetMethod.getName();
                        if (targetMethodName.startsWith("set")) {
                            String targetFieldName = targetMethodName.substring(3, 4).toLowerCase() + targetMethodName.substring(4);
                            if (srcFieldName.equals(targetFieldName) || (aClassConvert != null && aClassConvert.fieldName().equals(targetFieldName))) {
                                // 获取 set 方法 参数类型
                                Class<?> targetConvertClass = targetMethod.getParameterTypes()[0];
                                // 执行 类型转化
                                if (result != null && srcConvertClass != targetConvertClass) {
                                    result = ClassConvertContext.getInstance(srcConvertClass, targetConvertClass).classConvert(result);
                                }
                                targetMethod.invoke(target, result);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * dto集合和entity集合间的互相属性映射
     *
     * @param src
     * @param target
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <S, T> List<T> populateList(List<S> src, List<T> target, Class<?> targetClass) {
        for (int i = 0; i < src.size(); i++) {
            try {
                Object object = targetClass.newInstance();
                target.add((T) object);
                populate(src.get(i), object);
            } catch (Exception e) {
                // 某个方法反射异常
                continue;
            }
        }
        return target;
    }
}
