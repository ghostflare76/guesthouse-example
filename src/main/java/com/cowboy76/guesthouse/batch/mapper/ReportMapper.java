package com.cowboy76.guesthouse.batch.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.cowboy76.guesthouse.batch.model.Report;

public class ReportMapper implements RowMapper<Report> {

	@Override
	public Report mapRow(ResultSet rs, int rowNum) throws SQLException {

		Report report = new Report();
		report.setId(rs.getInt("ID"));
		report.setSales(rs.getBigDecimal("SALES"));
		report.setDate(rs.getDate("DATE"));
		report.setQty(rs.getInt("QTY"));
		report.setStaffName(rs.getString("STAFF_NAME"));

		return report;

	}

}
