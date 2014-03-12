package com.cowboy76.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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

import com.cowboy76.batch.model.Report;

@Configuration
public class CsvToXmlWriterJobConfiguration {
	
	@Autowired
	private StepBuilderFactory stepBuilders;

	@Autowired
	private JobBuilderFactory jobBuilders;

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
		return jobBuilders.get("importUserJob").start(step()).build();
	}

	@Bean
	public Step step() {
		return stepBuilders.get("step").<Report, Report> chunk(1).reader(reader()).processor(processor()).writer(
			writer()).build();
	}

}
