package com.sn.theft.service;

import com.sn.theft.dto.CommonDTO;

/**
 * @author: songning
 * @date: 2020/3/9 22:58
 */
public interface NovelsService {

    <T> CommonDTO<T> theftNovels(String sourceName);
}
