package com.cowboy76.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing(modular=true)
public class BatchConfiguration {

	@Bean
	public ApplicationContextFactory csvToXmlJob() {
		return new GenericApplicationContextFactory(CsvToXmlWriterJobConfiguration.class);
	}
	
	@Bean
	public ApplicationContextFactory dbtoXmlJob() {
		return new GenericApplicationContextFactory(DbToXmlWriterConfiguration.class);
	}

}