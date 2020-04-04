package com.sn.theft.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author sn
 */
@Data
public class CommonDTO<T> {
    /**
     * 额外数据集
     */
    private Map<String, Object> dataExt;

    /**
     * 額外的List結果集
     */
    private List<Map<String, Object>> listExt;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 数据集
     */
    private List<T> data;
    /**
     * 错误码
     */
    private Integer errCode;
    /**
     * 错误信息
     */
    private String message;
    /**
     * timeout
     */
    private String timeout;
    /**
     * (分页)总数
     */
    private Long total;
}
