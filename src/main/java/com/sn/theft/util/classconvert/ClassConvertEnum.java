package com.sn.theft.util.classconvert;


import com.sn.theft.util.classconvert.service.DateToStringImpl;
import com.sn.theft.util.classconvert.service.IClassConvert;
import com.sn.theft.util.classconvert.service.StringToDateImpl;

/**
 * @author songning
 * @date 2020/1/21
 * description
 */
public enum ClassConvertEnum {

    /**
     * 继续添加
     */
    String2Date("String2Date", StringToDateImpl.class),

    Date2String("Date2String", DateToStringImpl.class);

    private String key;
    private Class<? extends IClassConvert> value;

    public String getKey() {
        return this.key;
    }

    public Class getValue() {
        return this.value;
    }

    ClassConvertEnum(String key, Class<? extends IClassConvert> value) {
        this.key = key;
        this.value = value;
    }

    public static Class<? extends IClassConvert> getStrategy(String key) {
        for (ClassConvertEnum cse : ClassConvertEnum.values()) {
            if (key.equals(cse.getKey())) {
                return cse.getValue();
            }
        }
        return null;
    }
}
