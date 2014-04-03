package com.cowboy76.guesthouse.batch.job;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cowboy76.guesthouse.batch.CustomItemProcessor;
import com.cowboy76.guesthouse.batch.config.BatchConfiguration;
import com.cowboy76.guesthouse.batch.config.DataSourceConfiguration;
import com.cowboy76.guesthouse.batch.mapper.ReportMapper;
import com.cowboy76.guesthouse.batch.model.Report;

@Configuration
public class DbToXmlWriterJob {
	
	@Autowired
	private StepBuilderFactory stepBuilders;

	@Autowired
	private JobBuilderFactory jobBuilders;

	@Autowired
	private DataSourceConfiguration dataSourceConfiguration;

	@Autowired
	BatchConfiguration batchConfiguration;

	@Bean(name = "reportReader")
	public ItemReader<Report> reader() {
		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause("SELECT ID, SALES, QTY, STAFF_NAME, DATE");
		provider.setFromClause("FROM REPORT");
		Map<String, Order> sortKeys = new LinkedHashMap<String, Order>();
		sortKeys.put("ID", Order.ASCENDING);
		provider.setSortKeys(sortKeys);

		JdbcPagingItemReader<Report> reader = new JdbcPagingItemReader<Report>();
		reader.setDataSource(dataSourceConfiguration.dataSource());
		reader.setRowMapper(new ReportMapper());
		reader.setQueryProvider(provider);

		//	reader.setSql("SELECT ID, SALES, QTY, STAFF_NAME, DATE FROM REPORT ORDER BY ID ASC");

		return reader;

	}

	@Bean
	public ItemProcessor<Report, Report> processor() {
		return new CustomItemProcessor();
	}

	@Bean(name = "dbToXmlWriter")
	public ItemWriter<Report> writer() {
		StaxEventItemWriter<Report> writer = new StaxEventItemWriter<Report>();
		writer.setResource(new FileSystemResource("xml/outputs/DbToXmlReport.xml"));
		writer.setRootTagName("report");
		writer.setMarshaller(unmarshaller());

		return writer;
	}

	@Bean
	public Marshaller unmarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Report.class);
		
		return marshaller;
	}

	@Bean(name = "dbToXmlJob")
	public Job dbToXmlJob() {		
		return jobBuilders.get("dbToXmlJob").
		listener(batchConfiguration.customJobExecutionListener())
		.start(dbToXmlStep())
		.build();
	}

	@Bean
	public Step dbToXmlStep() {
		return stepBuilders.get("dbToXmlStep").<Report, Report> chunk(10)
		.reader(reader())
		.processor(processor())
		.writer(writer())
		.build();
	}
	
}
