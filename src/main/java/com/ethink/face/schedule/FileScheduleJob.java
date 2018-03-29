package com.ethink.face.schedule;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class FileScheduleJob extends QuartzJobBean {

	private static final Logger log = LoggerFactory.getLogger(FileScheduleJob.class);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void executeInternal(JobExecutionContext job) throws JobExecutionException {
		log.info("开始执行清理文件任务--------");
		JobDataMap dataMap = job.getJobDetail().getJobDataMap();
		String basePath = dataMap.getString("basePath");
		String fileInterval = dataMap.getString("fileInterval");
		File path = new File(basePath + "/tmp/match");
		if (!path.isDirectory()) {
			throw new JobExecutionException("文件路径不存在----");
		}
		File dateFolders[] = path.listFiles();
		String timeNow = sdf.format(new Date());
		int num = 0;
		try {
			for (File folder : dateFolders) {
				if (folder.isDirectory()) {
					String folderName = folder.getName();
					int time = getDistMonth(folderName, timeNow);
					if (time > Integer.parseInt(fileInterval)) {
						delFileFolder(folder);
						log.info("文件夹已删除:" + folderName);
						num++;
					}
				}
			}
		} catch (ParseException e) {
			log.error("日期转换错误", e);
		}
		log.info("清理文件夹完成,清理数量:" + num);
	}

	/**
	 * 计算时间之间的月份差距
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 * @throws ParseException
	 */
	public int getDistMonth(String time1, String time2) throws ParseException {

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(sdf.parse(time1));
		calendar2.setTime(sdf.parse(time2));
		int result = calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH);
		int month = (calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR)) * 12;
		return month + result;

	}

	public void delFileFolder(File file) {
		if (file != null) {

			if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (File f : files) {
					delFileFolder(f);
				}
			}
			file.delete();
		}
	}

	public static void main(String[] args) {
		/*
		 * int f; try { f = new FileScheduleJob().getDistMonth("2018-02-2", "2018-2-1");
		 * System.out.println(f); } catch (ParseException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
	}

}
