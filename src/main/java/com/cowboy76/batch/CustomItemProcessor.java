package com.cowboy76.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

import com.cowboy76.batch.model.Report;


public class CustomItemProcessor implements ItemProcessor<Report, Report> {

	@Override
	public Report process(final Report item) throws Exception {
		
		final String stffName = item.getStaffName().toUpperCase(); 
		final Report report = new Report();
		
		
		report.setStaffName(stffName);
		
		System.out.println("Converting ( " + item +  ") into (" + report + ")");
		return report;
	}

}
