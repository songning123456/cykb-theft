package com.sn.theft.service.impl;

import com.sn.theft.dto.CommonDTO;
import com.sn.theft.entity.Novels;
import com.sn.theft.repository.NovelsRepository;
import com.sn.theft.service.NovelsService;
import com.sn.theft.thread.TheftProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/9 22:59
 */
@Service
public class NovelsServiceImpl implements NovelsService {

    @Autowired
    private NovelsRepository novelsRepository;
    @Autowired
    private TheftProcessor theftProcessor;

    @Override
    public <T> CommonDTO<T> theftNovels(String sourceName) {
        CommonDTO<T> commonDTO = new CommonDTO<>();
        if (StringUtils.isEmpty(sourceName)) {
            commonDTO.setStatus(202);
            commonDTO.setMessage("sourceName不能为空!");
            return commonDTO;
        }
        // 如果表里存在就说明 已经开始爬虫了
        List<Novels> novelsList = novelsRepository.findFirstClassifyNative(sourceName, 1);
        if (novelsList != null && novelsList.size() > 0) {
            commonDTO.setMessage("此网站正在爬虫!");
        } else {
            commonDTO.setMessage("准备开始爬虫!");
            switch (sourceName) {
                case "笔趣阁":
                    theftProcessor.theftBiquge();
                    break;
                case "147小说":
                    theftProcessor.theft147();
                    break;
                case "天天书吧":
                    theftProcessor.theftTtsb();
                    break;
                default:
                    theftProcessor.testTheft();
                    break;
            }
        }
        return commonDTO;
    }
}
