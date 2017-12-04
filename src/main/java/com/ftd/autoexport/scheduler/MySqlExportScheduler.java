package com.ftd.autoexport.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;



public class MySqlExportScheduler extends QuartzJobBean {
	private MySqlExportTask runMeTask;

	public void setRunMeTask(MySqlExportTask runMeTask) {
		this.runMeTask = runMeTask;
	}

	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		runMeTask.exportData();

	}
}
