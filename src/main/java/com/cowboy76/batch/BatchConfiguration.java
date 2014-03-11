package com.cowboy76.batch;

import javax.inject.Inject;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cowboy76.batch.model.Report;

@Configuration
@EnableBatchProcessing
@PropertySource("classpath:environment/database_loc.properties")
public class BatchConfiguration {

	@Value("classpath:org/springframework/batch/core/schema-drop-mysql.sql")
	private Resource dropScript;

	@Value("classpath:org/springframework/batch/core/schema-mysql.sql")
	private Resource createScript;

	@Inject
	private Environment environment;

	@Autowired
	private StepBuilderFactory stepBuilders;

	@Autowired
	private JobBuilderFactory jobBuilders;

	@Bean
	public ItemReader<Report> reader() {
		FlatFileItemReader<Report> reader = new FlatFileItemReader<Report>();
		reader.setResource(new ClassPathResource("cvs/input/report.csv"));
		reader.setLineMapper(lineMapper());
		return reader;
	}

	public LineMapper<Report> lineMapper() {
		DefaultLineMapper<Report> lineMapper = new DefaultLineMapper<Report>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] {"id", "sales", "qty", "staffName", "date"});
		lineTokenizer.setIncludedFields(new int[]{0,1,2,3,4});
		lineTokenizer.setDelimiter(",");
		//lineTokenizer.setStrict(false);
		lineMapper.setLineTokenizer(lineTokenizer);
		FieldSetMapper<Report> fieldSetMapper = new ReportFileSetMapper();		
		
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}

	@Bean
	public ItemProcessor<Report, Report> processor() {
		return new CustomItemProcessor();
	}
	
	@Bean
	public ItemWriter<Report> writer() {
		StaxEventItemWriter<Report> writer = new StaxEventItemWriter<Report>();
		writer.setResource(new FileSystemResource("xml/outputs/report.xml"));
		writer.setRootTagName("report");
		writer.setMarshaller(marshaller());
		return writer;
	}

	@Bean
	public Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Report.class);
		return marshaller;
	}
	
	@Bean
	public Job importUserJob() {
		return jobBuilders.get("importUserJob")		
			.start(step())
			.build();
	}

	@Bean
	public Step step() {
		return stepBuilders.get("step")
			.<Report, Report> chunk(1)
			.reader(reader())
			.processor(processor())
			.writer(writer())
			.build();
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer() {
		final DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource());
		initializer.setDatabasePopulator(databasePopulator());
		return initializer;
	}

	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(dropScript);
		populator.addScript(createScript);
		return populator;
	}

	@Bean(destroyMethod = "close")
	@Scope("prototype")
	public DataSource dataSource() {
		DataSource ds = new DataSource();
		ds.setDriverClassName(environment.getRequiredProperty("default.ds.jdbc.driverClassName"));
		ds.setUrl(environment.getRequiredProperty("default.ds.jdbc.url"));
		ds.setUsername(environment.getRequiredProperty("default.ds.jdbc.username"));
		ds.setPassword(environment.getRequiredProperty("default.ds.jdbc.password"));
		ds.setMaxActive(environment.getRequiredProperty("default.ds.jdbc.maxActive", int.class));
		ds.setMinIdle(environment.getRequiredProperty("default.ds.jdbc.minIdle", int.class));
		ds.setMaxIdle(environment.getRequiredProperty("default.ds.jdbc.maxIdle", int.class));
		ds.setMaxWait(environment.getRequiredProperty("default.ds.jdbc.maxWait", int.class));
		ds.setInitialSize(environment.getRequiredProperty("default.ds.jdbc.initialSize", int.class));
		ds.setValidationQuery(environment.getRequiredProperty("default.ds.jdbc.validationQuery"));
		ds.setValidationInterval(environment.getRequiredProperty("default.ds.jdbc.validationInterval", long.class));
		ds.setTestOnBorrow(environment.getRequiredProperty("default.ds.jdbc.testOnBorrow", boolean.class));
		ds.setTestWhileIdle(environment.getRequiredProperty("default.ds.jdbc.testWhileIdle", boolean.class));
		ds.setTimeBetweenEvictionRunsMillis(environment.getRequiredProperty(
			"default.ds.jdbc.timeBetweenEvictionRunsMillis", int.class));
		ds.setRemoveAbandoned(environment.getRequiredProperty("default.ds.jdbc.removeAbandoned", boolean.class));
		ds.setRemoveAbandonedTimeout(environment.getRequiredProperty("default.ds.jdbc.removeAbandonedTimeout",
			int.class));
		ds.setLogAbandoned(environment.getRequiredProperty("default.ds.jdbc.logAbandoned", boolean.class));
		ds.setAbandonWhenPercentageFull(environment.getRequiredProperty("default.ds.jdbc.abandonWhenPercentageFull",
			int.class));
		ds.setJdbcInterceptors(environment.getRequiredProperty("default.ds.jdbc.jdbcInterceptors"));
		ds.setConnectionProperties(environment.getRequiredProperty("default.ds.jdbc.connectionProperties"));
		return ds;
	}

}
