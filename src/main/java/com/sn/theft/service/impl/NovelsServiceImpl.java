package com.sn.theft.service.impl;

import com.sn.theft.dto.CommonDTO;
import com.sn.theft.service.NovelsService;
import org.springframework.stereotype.Service;

/**
 * @author: songning
 * @date: 2020/3/9 22:59
 */
@Service
public class NovelsServiceImpl implements NovelsService {

    @Override
    public <T> CommonDTO<T> theftNovels(String sourceName) {
        return new CommonDTO<>();
    }
}
