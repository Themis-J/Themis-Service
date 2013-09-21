package com.jdc.themis.dealer.report.job;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class JobInitializer {
	private final static Logger logger = LoggerFactory
			.getLogger(JobInitializer.class);

	private Executor executor = Executors.newSingleThreadExecutor();
	
	@Autowired
	private DealerIncomeReportGeneratorJob job;
	
	private CronTriggerFactoryBean cronTriggerFactoryBean;
	
	private MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean;
	
	private SchedulerFactoryBean schedulerFactoryBean;
	
	public void init() {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					logger.error("", e);
				}
				try {
					methodInvokingJobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
					methodInvokingJobDetailFactoryBean.setName("DealerIncomeReportGeneratorJob");
					methodInvokingJobDetailFactoryBean.setTargetObject(job);
					methodInvokingJobDetailFactoryBean.setTargetMethod("execute");
					methodInvokingJobDetailFactoryBean.afterPropertiesSet();
					
					cronTriggerFactoryBean = new CronTriggerFactoryBean();
					cronTriggerFactoryBean.setName("DealerIncomeReportGeneratorJobTrigger");
					cronTriggerFactoryBean.setStartDelay(2000);
					cronTriggerFactoryBean.setJobDetail(methodInvokingJobDetailFactoryBean.getObject());
					cronTriggerFactoryBean.setCronExpression("0 0/45 * * * ?");
					cronTriggerFactoryBean.afterPropertiesSet();
					
					schedulerFactoryBean = new SchedulerFactoryBean();
					schedulerFactoryBean.setTriggers(new Trigger[]{cronTriggerFactoryBean.getObject()});
				
					schedulerFactoryBean.afterPropertiesSet();
				} catch (Exception e) {
					logger.error("", e);
				}
				schedulerFactoryBean.start();
			}
			
		});

	}
	
	public void shutdown() {
		schedulerFactoryBean.stop();
	}
}
