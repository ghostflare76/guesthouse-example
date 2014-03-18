package com.cowboy76.batch.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cowboy76.batch.adapter.JaxbBigDecimalAdapter;
import com.cowboy76.batch.adapter.JaxbDateAdapter;

@XmlRootElement(name = "record")
public class Report {

	private int id;

	private BigDecimal sales;

	private int qty;

	private String staffName;

	private Date date;

	@XmlElement(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement(name = "sales")
	@XmlJavaTypeAdapter(JaxbBigDecimalAdapter.class)
	public BigDecimal getSales() {
		return sales;
	}

	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}

	@XmlElement(name = "qty")
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	@XmlElement(name = "staffName")
	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	
	@XmlElement(name = "date")
	@XmlJavaTypeAdapter(JaxbDateAdapter.class)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Report [id=" + id + ", sales=" + sales + ", qty=" + qty + ", staffName=" + staffName + ", date=" + date
			+ "]";
	}

}
