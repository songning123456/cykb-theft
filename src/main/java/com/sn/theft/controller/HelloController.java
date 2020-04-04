package com.sn.theft.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: songning
 * @date: 2020/4/3 19:23
 */
@RestController
@RequestMapping("/hello")
@Slf4j
public class HelloController {

    @GetMapping("/say")
    public String sayHello() {
        log.info("say hello!!!");
        return "say hello!!!";
    }
}
