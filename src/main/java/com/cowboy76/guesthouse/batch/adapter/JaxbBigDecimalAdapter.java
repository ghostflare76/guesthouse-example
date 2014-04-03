package com.cowboy76.guesthouse.batch.adapter;

import java.math.BigDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbBigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

	@Override
	public String marshal(BigDecimal bigDecimal) throws Exception {
		return bigDecimal.toString();

	}

	@Override
	public BigDecimal unmarshal(String bigDecimal) throws Exception {
		return new BigDecimal(bigDecimal.replaceAll(",", ""));
	}

}
