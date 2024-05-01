package com.java.quartz.springquartz.service.impl;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.java.quartz.springquartz.job.JobExecute;
import com.java.quartz.springquartz.service.QuartzService;
import com.java.quartz.springquartz.vo.JobQuartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuartzServiceImpl implements QuartzService{

    private final SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public boolean createJob(JobQuartz jobQuartz) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobQuartz.getName(), jobQuartz.getGroup());
            if (!scheduler.checkExists(jobKey)) {
                JobDetail jobDetail = createNewJobDetail(jobQuartz);
                Trigger trigger = createNewTrigger(jobQuartz);
                scheduler.scheduleJob(jobDetail, trigger);
                log.info("job: {}-{} created", jobQuartz.getName(), jobQuartz.getGroup());
                return true;
            } else {
                log.error("job {}-{} is exist", jobQuartz.getName(), jobQuartz.getGroup());
                return false;
            }
        } catch (SchedulerException schedulerException) {
            log.error("error create schedule: {}", schedulerException.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteJob(JobQuartz jobQuartz) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            boolean isDeleted = scheduler.deleteJob(new JobKey(jobQuartz.getName(), jobQuartz.getGroup()));
            if (isDeleted) {
                log.info("job: {}-{} is deleted", jobQuartz.getName(), jobQuartz.getGroup());
            }
            return isDeleted;
        } catch (SchedulerException schedulerException) {
            log.error("failed to delete the job: {}-{}", jobQuartz.getName(), jobQuartz.getGroup(), schedulerException);
            return false;
        }
    }

    @Override
    public boolean pauseJob(JobQuartz jobQuartz) {
        try {
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobQuartz.getName(), jobQuartz.getGroup()));
            log.info("job: {}-{} is pause", jobQuartz.getName(), jobQuartz.getGroup());
            return true;
        } catch (SchedulerException e) {
            log.error("job: {}-{} is error when try to pause", jobQuartz.getName(), jobQuartz.getGroup());
            return false;
        }
    }

    @Override
    public boolean resumeJob(JobQuartz jobQuartz) {
        try {
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobQuartz.getName(), jobQuartz.getGroup()));
            log.info("job: {}-{} is resumed", jobQuartz.getName(), jobQuartz.getGroup());
            return true;
        } catch (SchedulerException e) {
            log.error("job: {}-{} is error when try to resume", jobQuartz.getName(), jobQuartz.getGroup());
            return false;
        }
    }

    @Override
    public boolean updateScheduleJob(JobQuartz jobQuartz) {
       try {
            Trigger trigger = createNewTrigger(jobQuartz);
            schedulerFactoryBean.getScheduler().rescheduleJob(new TriggerKey(jobQuartz.getName(), jobQuartz.getGroup()), trigger);
            return true;
        } catch (SchedulerException e) {
            log.error("job: {}-{} is error when try to be rescheduled", jobQuartz.getName(), jobQuartz.getGroup());
            return false;
        }
    }

    @Override
    public boolean isExist(JobQuartz jobQuartz) {

        boolean result = false;
        try {
            result = schedulerFactoryBean.getScheduler().checkExists(new JobKey(jobQuartz.getName(), jobQuartz.getGroup()));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return result;
    }

    private JobDetail createNewJobDetail(JobQuartz jobQuartz) {
        return JobBuilder.newJob(JobExecute.class)
                .storeDurably().withIdentity(jobQuartz.getName(), jobQuartz.getGroup())
                .setJobData(new JobDataMap(jobQuartz.getData())).build();
    }

    private Trigger createNewTrigger(JobQuartz jobQuartz) throws SchedulerException {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobQuartz.getName(), jobQuartz.getGroup())
                .startAt(jobQuartz.getStartTime()).withSchedule(CronScheduleBuilder.cronSchedule(jobQuartz.getCronExpression()).withMisfireHandlingInstructionFireAndProceed()).build();
    }
    
}
