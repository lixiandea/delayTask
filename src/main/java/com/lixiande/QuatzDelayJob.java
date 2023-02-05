package com.lixiande;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuatzDelayJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("扫描数据库");
    }

    public static void main(String[] args) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(QuatzDelayJob.class).withIdentity("job1", "group1").build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever()).build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }
}
