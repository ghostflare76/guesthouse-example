package com.cowboy76.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableBatchProcessing(modular=true)
public class BatchConfiguration {

	@Bean
	public ApplicationContextFactory someJobs() {
		return new GenericApplicationContextFactory(CsvToXmlWriterJobConfiguration.class);
	}

}