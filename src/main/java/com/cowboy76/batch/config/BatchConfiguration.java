package com.cowboy76.batch.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.cowboy76.batch.listener.CustomJobExecutionListener;


@Configuration
public class BatchConfiguration {
	
	@Autowired	
	DataSourceConfiguration dataSourceConfiguration;
	
	@Bean
	public JobRegistry jobRegistry(){
		return new MapJobRegistry();
	}	
	
	@Bean
	public LobHandler lobHandler(){
		return new DefaultLobHandler();
	}
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(){
		JobRegistryBeanPostProcessor beanPostProcessor = new JobRegistryBeanPostProcessor();
		beanPostProcessor.setJobRegistry(jobRegistry());
		return beanPostProcessor;
	}
	
	public JobRepository getJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSourceConfiguration.dataSource());
		factory.setTransactionManager(dataSourceConfiguration.getTransactionManager());
		factory.setLobHandler(lobHandler());
		factory.setDatabaseType(DatabaseType.fromMetaData(dataSourceConfiguration.dataSource()).name());
		factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
		factory.afterPropertiesSet();
		return  (JobRepository) factory.getObject();
	}
		
	@Bean
	public JobLauncher jobLauncher() throws Exception{
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(getJobRepository());
		launcher.setTaskExecutor(new SyncTaskExecutor());
		return launcher;
	}
	
	@Bean
	public CustomJobExecutionListener customJobExecutionListener(){
		return new CustomJobExecutionListener();
	}

}