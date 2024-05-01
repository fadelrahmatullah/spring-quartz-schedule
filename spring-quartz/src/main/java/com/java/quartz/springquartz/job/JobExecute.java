package com.java.quartz.springquartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.quartz.springquartz.service.QuartzService;
import com.java.quartz.springquartz.vo.JobQuartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
@RequiredArgsConstructor
public class JobExecute extends QuartzJobBean{

    @Autowired
    private QuartzService quartzService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        
        log.info("======= {} Start ======", getClass().getSimpleName());

        log.info("Job ** {}-{} ** fired @ {}", context.getJobDetail().getKey().getGroup(),
                context.getJobDetail().getKey().getName(), context.getFireTime());

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String group = jobDataMap.getString("group");

        switch (group) {

            case "Job-1":
                System.out.println("Success Execute Job 1");
                this.printData(jobDataMap);
                break;
            case "Job-2":
                System.out.println("Success Execute Job 2");
                this.printData(jobDataMap);
                break;
            case "Job-3":
                System.out.println("Success Execute Job 3");
                this.printData(jobDataMap);
                break;
            default:
                break;
        }
    }

    private void printData(JobDataMap jobDataMap){
        String group = jobDataMap.getString("group");
        String printData = jobDataMap.getString("data");

        log.info("==== Group: {} ====> Data : {} ====", group, printData);

        JobQuartz jobQuartz = new JobQuartz();
        try {
            jobQuartz = objectMapper.readValue(printData, JobQuartz.class);
        } catch (Exception e) {
            jobQuartz = null;
        }

        log.info("DELETE JOB {} ====", group);
        jobQuartz.setName(jobDataMap.getString("username"));
        jobQuartz.setGroup(group);
        quartzService.deleteJob(jobQuartz);
    }
    
}
