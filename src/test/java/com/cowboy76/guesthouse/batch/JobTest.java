package com.cowboy76.guesthouse.batch;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cowboy76.guesthouse.batch.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
/*
 * @ContextConfiguration(classes={DataSourceConfiguration.class,
 * BatchConfiguration.class})
 */
@ContextConfiguration(classes = {AppConfig.class})
public class JobTest {

	@Autowired
	private JobRegistry jobRegistry;

	@Autowired
	private JobLauncher jobLauncher;

	@Test
	public void testCsvToXmlJob() throws Exception {
		Job job = jobRegistry.getJob("csvToXmlJob");
		jobLauncher.run(job, new JobParameters());
		assertThat(job.getName(), is("csvToXmlJob"));
	}

	@Test
	public void testDbToXmlJob() throws Exception {
		Job job = jobRegistry.getJob("dbToXmlJob");
		jobLauncher.run(job, new JobParameters());
		assertThat(job.getName(), is("dbToXmlJob"));
	}

	@Test
	public void testXmlToDbJob() throws Exception {
		Job job = jobRegistry.getJob("xmlToDbJob");
		jobLauncher.run(job, new JobParameters());
		assertThat(job.getName(), is("xmlToDbJob"));
	}

}