package com.cowboy76.batch.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cowboy76.batch.listener.CustomJobExecutionListener;


@Configuration
public class BatchConfiguration {

	@Bean
	public JobRegistry jobRegistry(){
		return new MapJobRegistry();
	}
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
		JobRegistryBeanPostProcessor beanPostProcessor = new JobRegistryBeanPostProcessor();
		beanPostProcessor.setJobRegistry(jobRegistry());
		return beanPostProcessor;
	}
	
	
	@Bean
	public CustomJobExecutionListener customJobExecutionListener(){
		return new CustomJobExecutionListener();
	}

}