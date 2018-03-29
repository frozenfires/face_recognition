package com.ethink.face.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ethink.face.db.FaceDAO;

public class DatabaseScheduleJob extends QuartzJobBean {

	private static final Logger log = LoggerFactory.getLogger(DatabaseScheduleJob.class);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void executeInternal(JobExecutionContext job) throws JobExecutionException {
		log.info("开始执行清理数据库任务--------");
		JobDataMap dataMap = job.getJobDetail().getJobDataMap();

		String databaseInterval = dataMap.getString("databaseInterval");
		FaceDAO faceDAO = (FaceDAO) dataMap.get("faceDAO");
		String datetime = beforeMonth(new Date(), Integer.parseInt(databaseInterval));
		
		int result = faceDAO.deleteMatchInfoByTime(datetime);
		if (result != -1) {
			log.info("清理数据库成功,清理 " + result + " 条数据");
		} else {
			log.info("\"清理数据库失败");
		}

	}


	/**
	 * 日期 向前向后计算n个月
	 * @param date
	 * @param num
	 * @return
	 */
	public String beforeMonth(Date date, int num) {

		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.MONTH, -num);//"-"为之前，'+'为之后
		return sdf.format(calendar.getTime());
	}
	
	public static void main(String[] args) {
		String ss = new DatabaseScheduleJob().beforeMonth(new Date(), 25);
		System.out.println(ss);
	}

}
