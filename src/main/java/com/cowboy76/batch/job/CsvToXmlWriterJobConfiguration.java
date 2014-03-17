package com.cowboy76.batch.job;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cowboy76.batch.CustomItemProcessor;
import com.cowboy76.batch.config.BatchConfiguration;
import com.cowboy76.batch.config.DataSourceConfiguration;
import com.cowboy76.batch.listener.CustomJobExecutionListener;
import com.cowboy76.batch.mapper.ReportFileSetMapper;
import com.cowboy76.batch.model.Report;

@Configuration
@EnableBatchProcessing
public class CsvToXmlWriterJobConfiguration {
	
	@Autowired
	private StepBuilderFactory stepBuilders;

	@Autowired
	private JobBuilderFactory jobBuilders;
	
	@Autowired
	DataSourceConfiguration dataSourceConfiguration;

	@Autowired
	BatchConfiguration batchConfiguration;
	
	@Bean
	public ItemReader<Report> reader() {
		FlatFileItemReader<Report> reader = new FlatFileItemReader<Report>();
		reader.setResource(new ClassPathResource("input/report.csv"));
		reader.setLineMapper(lineMapper());
		return reader;
	}

	public LineMapper<Report> lineMapper() {
		DefaultLineMapper<Report> lineMapper = new DefaultLineMapper<Report>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] {"id", "sales", "qty", "staffName", "date"});
		lineTokenizer.setIncludedFields(new int[] {0, 1, 2, 3, 4});
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
	public ItemWriter<Report> csvToXmlWriter() {
		StaxEventItemWriter<Report> writer = new StaxEventItemWriter<Report>();
		writer.setResource(new FileSystemResource("xml/outputs/csvToXmlReport.xml"));
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

	@Bean(name="csvToXmlJob")
	public Job csvToXmlJob() {
		return jobBuilders.get("csvToXmlJob")
			.listener(batchConfiguration.customJobExecutionListener())
			.start(csvToXmlStep())
			.build();
	}

	@Bean(name="csvToXmlStep")
	public Step csvToXmlStep() {
		return stepBuilders.get("csvToXmlStep").<Report, Report> chunk(1)
			.reader(reader())
			.processor(processor())
			.writer(csvToXmlWriter())
			.build();
	}


	
}
