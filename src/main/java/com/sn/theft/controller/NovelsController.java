package com.sn.theft.controller;

import com.sn.theft.annotation.AControllerAspect;
import com.sn.theft.dto.CommonDTO;
import com.sn.theft.service.NovelsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: songning
 * @date: 2020/3/9 22:55
 */
@Slf4j
@RestController
@RequestMapping("/novels")
public class NovelsController {

    @Autowired
    private NovelsService novelsService;

    @AControllerAspect(description = "盗取书籍")
    @GetMapping("/theftNovels")
    public <T> CommonDTO<T> theftAllNovels(@RequestParam("sourceName") String sourceName) {
        CommonDTO<T> commonDTO = novelsService.theftNovels(sourceName);
        return commonDTO;
    }
}
