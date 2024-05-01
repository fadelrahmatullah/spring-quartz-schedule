package com.java.quartz.springquartz.service.impl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.quartz.springquartz.service.MainService;
import com.java.quartz.springquartz.service.QuartzService;
import com.java.quartz.springquartz.vo.JobQuartz;
import com.java.quartz.springquartz.vo.MainRequestVo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService{

    private final ObjectMapper objectMapper;

    @Autowired
    private QuartzService quartzService;

	@Override
	public void createTaskJob1(MainRequestVo vo) {
        
        JobQuartz jobVo = new JobQuartz();
        jobVo.setName("Job Quartz 1");
        jobVo.setGroup("Job-1");
        HashMap<String, String> data = new HashMap<>();
        try {
            data.put("data", objectMapper.writeValueAsString(vo));
        } catch (JsonProcessingException e) {
            data.put("data", null);
        }
        data.put("group", jobVo.getGroup());
        data.put("username", vo.getUsername());
        jobVo.setData(data);


        // Set Cron Job 3 minutes
        jobVo.setCronExpression("0 */3 * * * ?");
        jobVo.setStartTime(new Date());
        quartzService.createJob(jobVo);
	}

	@Override
	public void createTaskJob2(MainRequestVo vo) {

		JobQuartz jobVo = new JobQuartz();
        jobVo.setName("Job Quartz 2");
        jobVo.setGroup("Job-2");
	
        HashMap<String, String> data = new HashMap<>();
        try {
            data.put("data", objectMapper.writeValueAsString(vo));
        } catch (JsonProcessingException e) {
            data.put("data", null);
        }
        data.put("group", jobVo.getGroup());
        data.put("username", vo.getUsername());
        jobVo.setData(data);


        // Set Cron Job 1 minutes
        jobVo.setCronExpression("0 */2 * * * ?");
        jobVo.setStartTime(new Date());
        quartzService.createJob(jobVo);
	}
    
}
