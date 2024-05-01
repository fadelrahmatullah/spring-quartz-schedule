package com.java.quartz.springquartz.util;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.java.quartz.springquartz.service.MainService;
import com.java.quartz.springquartz.vo.MainRequestVo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExecuteQuartz {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MainService mainService;

    @PostConstruct
    public void executeSQLScript() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:quartz.sql");
        String sql = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        jdbcTemplate.execute(sql);
    }

    @Scheduled(cron = "0 */4 * * * *")
    public void createTaskJob1(){

        log.info("===== Start Create Task JOB-1 ======");
        MainRequestVo mainRequestVo = new MainRequestVo();
        mainRequestVo.setUsername("I am a Job One");
        mainRequestVo.setAddress("jobb street number one");
        mainRequestVo.setGender("Male");
        mainRequestVo.setPhoneNumber("00009999000");
        mainService.createTaskJob1(mainRequestVo);
    }

    @Scheduled(cron = "0 */4 * * * *")
    public void createTaskJob2(){

        log.info("===== Start Create Task JOB-2 ======");
        MainRequestVo mainRequestVo = new MainRequestVo();
        mainRequestVo.setUsername("I am a Job Two");
        mainRequestVo.setAddress("jobb street number Two");
        mainRequestVo.setGender("Male");
        mainRequestVo.setPhoneNumber("00009999000");
        mainService.createTaskJob2(mainRequestVo);
    }
}
