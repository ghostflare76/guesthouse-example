package com.cowboy76.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cowboy76.batch.config.DataSourceConfiguration;
import com.cowboy76.batch.model.Report;

@Configuration
public class XmlToDbWriterConfiguration {
	
	@Autowired
	private StepBuilderFactory stepBuilders;

	@Autowired
	private JobBuilderFactory jobBuilders;

	@Autowired
	private DataSourceConfiguration dataSourceConfiguration;
	
	@Bean(name="xmlReader")
	public ItemReader<Report> reader() {
		StaxEventItemReader<Report> reader = new StaxEventItemReader<Report>();
		reader.setResource(new ClassPathResource("input/report.xml"));
		reader.setFragmentRootElementName("record");
		reader.setUnmarshaller(unMarshaller());
		return reader;
	}
	
	@Bean
	public Unmarshaller unMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Report.class);
		
		return marshaller;
	}
	
	@Bean(name="xmlToDbJob")
	public Job job() {
		return jobBuilders.get("xmlToDbJob")		
			.start(step())
			.build();
	}

	@Bean(name="xmlToDbStep")
	public Step step() {
		return stepBuilders.get("xmlToDbStep").<Report, Report> chunk(1)
			.reader(reader())		
			.writer(writer())
			.build();
	}
	
	@Bean(name="dbWriter")
	public ItemWriter<Report> writer() {
		JdbcBatchItemWriter<Report> writer = new JdbcBatchItemWriter<Report>();
		writer.setDataSource(dataSourceConfiguration.dataSource());
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("INSERT INTO `REPORT` (ID, SALES, QTY, STAFF_NAME, DATE) ");
		sbf.append("VALUES (:id, :sales, :qty, :staffName, :date)");	
		writer.setSql(sbf.toString());
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Report>());
		return writer;
	}

}
