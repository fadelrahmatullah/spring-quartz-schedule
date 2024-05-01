package com.java.quartz.springquartz.service;

import com.java.quartz.springquartz.vo.JobQuartz;

public interface QuartzService {

    boolean createJob(JobQuartz jobQuartz);

    boolean deleteJob(JobQuartz jobQuartz);

    boolean pauseJob(JobQuartz jobQuartz);

    boolean resumeJob(JobQuartz jobQuartz);

    boolean updateScheduleJob(JobQuartz jobQuartz);

    boolean isExist(JobQuartz jobQuartz);

}

