package com.ethink.face.schedule;

import java.util.Calendar;
import java.util.Date;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ethink.face.db.FaceDAO;

@Component
public class FaceScheduler {
	
	@Value("${app.basePath}")
	private String basePath;
	
	@Value("${face.file.interval}")
	private String fileInterval;
	
	@Value("${face.database.interval}")
	private String databaseInterval;
	
	@Autowired
    FaceDAO faceDAO;
	

	@Bean
	public JobDetail fileJobDetail() {
		JobDataMap jobData = new JobDataMap();
 		jobData.put("basePath", basePath);
 		jobData.put("fileInterval", fileInterval);
        JobDetail jobDetail = JobBuilder.newJob(FileScheduleJob.class)
        		.storeDurably()//可以在没有触发器指向任务的时候，将任务保存在队列中了。然后就能手动触发了。
        		.usingJobData(jobData)
        		.withIdentity("fileJob_1", "group1_face").build(); 
		return jobDetail;
	}

	@Bean
	public Trigger fileJobTrigger() {
/*		 String cron = "0 0 0 1 1/"+fileInterval+" ?";
	        //String cron = "10/10 * * * * ?";
	        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron); 
	        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
	        		.forJob(fileJobDetail())
	        		.withIdentity("trigger1", "group1") 
	        		.withSchedule(scheduleBuilder).build(); */
	        
	        Calendar calendar = Calendar.getInstance();// 日历对象
			calendar.setTime(new Date());// 设置当前日期
			calendar.add(Calendar.MONTH,Integer.parseInt(fileInterval));//"-"为之前，'+'为之后
			
	        Trigger trigger = TriggerBuilder.newTrigger()
	        		.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
	        				.withIntervalInMonths(Integer.parseInt(fileInterval)))//每隔n个月
	        		.forJob(fileJobDetail())//指定jobdetail
	        		.startAt(calendar.getTime())
	        		.withIdentity("trigger1", "group1").build(); 

	        return trigger;
	}
	
	
	
	/**
	 * 清理数据库job
	 * @return
	 */
	@Bean
	public JobDetail databaseDetail() {
		JobDataMap jobData = new JobDataMap();
 		jobData.put("basePath", basePath);
 		jobData.put("databaseInterval", databaseInterval);
 		jobData.put("faceDAO", faceDAO);
 		
        JobDetail jobDetail = JobBuilder.newJob(DatabaseScheduleJob.class)
        		.storeDurably()//可以在没有触发器指向任务的时候，将任务保存在队列中了。然后就能手动触发了。
        		.usingJobData(jobData)
        		.withIdentity("databaseJob_1", "group1_face").build(); 
        
        return jobDetail;
	}

	@Bean
	public Trigger databaseJobTrigger() {
/*		String cron = "0 0 0 1 1 ? 0/"+databaseInterval;
        String cron = "15/15 * * * * ?";
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron); 
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
        		.forJob(databaseDetail())
        		.withIdentity("trigger2", "group2")
        		.withSchedule(scheduleBuilder).build(); */
		
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(new Date());// 设置当前日期
		calendar.add(Calendar.MONTH,Integer.parseInt(databaseInterval));//"-"为之前，'+'为之后
		
        Trigger trigger = TriggerBuilder.newTrigger()
        		.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
        				.withIntervalInMonths(Integer.parseInt(databaseInterval)))//每隔n个月
        		.forJob(databaseDetail())
        		.startAt(calendar.getTime())
        		.withIdentity("trigger2", "group2").build(); 
     
        return  trigger;
	}

}
