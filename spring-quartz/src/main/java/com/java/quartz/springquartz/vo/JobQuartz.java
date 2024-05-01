package com.java.quartz.springquartz.vo;

import java.util.Date;
import java.util.HashMap;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class JobQuartz {
    
    private String name;

    private String group;

    @NonNull
    private String cronExpression;

    private Date startTime;

    private HashMap<String, String> data;
}
