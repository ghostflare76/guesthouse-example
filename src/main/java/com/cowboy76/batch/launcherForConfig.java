package com.cowboy76.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class launcherForConfig {
    public static void main(String[] args) {
    	ApplicationContext ctx = new AnnotationConfigApplicationContext(BatchConfiguration.class);
    	executeJob(ctx);        
    }
    
    private static void executeJob(ApplicationContext ctx) {
    	Job job = (Job)ctx.getBean("importUserJob");
    	JobLauncher launcher = ctx.getBean(JobLauncher.class);
    	
		try {
			JobParameters parameters = new JobParameters();
			launcher.run(job, parameters);
		}
		catch(JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			e.printStackTrace();
		}
    }
}