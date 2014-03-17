package com.cowboy76.batch.config;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cowboy76.batch.listener.CustomJobExecutionListener;

@Configuration
public class BatchJobConfiguration {
	
	@Autowired
	private JobRepository jobRepository;
		
	@Bean(name="customJobBuilders")
	public BatchJobBuilderFactory customJobBuilders() throws Exception{
		return new BatchJobBuilderFactory(jobRepository, customJobExecutionListener());
	}
	
	@Bean(name="customJobExecutionListener")
	public CustomJobExecutionListener customJobExecutionListener(){
		return new CustomJobExecutionListener();
	}
	
	
}
