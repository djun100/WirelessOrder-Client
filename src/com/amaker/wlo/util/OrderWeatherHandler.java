package com.amaker.wlo.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * ��������Ԥ����������Ϊ�ȸ�Ĺ�ϵ����ʹ���޷�����
 * @author Administrator
 *
 */
public class OrderWeatherHandler extends DefaultHandler{
	/**
	 * ��ǰ�������
	 */
	private CurrentCondition currentCondition;
	
	/**
	 * ĳһ����������
	 */
	private ForecastCondition  forecastCondition;
	
	/**
	 * �����б�
	 */
	private List<ForecastCondition> forecastList;

	private boolean isCurrent = false;;
	
	/**
	 * ��ȡ��ǰ����ʵ��
	 * @return CurrentCondition
	 */
	public CurrentCondition getCurrentCondition() {
		return currentCondition;
	}

	/**
	 * ��ȡ�����б�
	 * @return List<ForecastCondition>
	 */
	public List<ForecastCondition> getForecastList() {
		return forecastList;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// �����ǰ������ϣ�����Ϊ��ֵ������ForecaseCondition����
		if("current_conditions".equals(localName))
			isCurrent = false;
		
		// ���ĳһ�������ϣ�����뵽List��
		if("forecast_conditions".equals(localName))
			forecastList.add(forecastCondition);
		
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if("current_conditions".equals(localName))
			isCurrent = true;
		if("forecast_conditions".equals(localName))
			forecastCondition = new ForecastCondition();
		
		String value = attributes.getValue("data");
		
		if(isCurrent){ // ��ǰ����
			
			if("temp_c".equals(localName))
				currentCondition.setTemp_c(value);
			else if("condition".equals(localName))
				currentCondition.setCondition(value);
			else if("humidity".equals(localName))
				currentCondition.setHumidity(value);
			else if("icon".equals(localName))
				currentCondition.setIcon(value);
			else if("wind_condition".equals(localName))
				currentCondition.setWind_condition(value);
						
		}else{ // �����б�
			
			if("day_of_week".equals(localName))
				forecastCondition.setDay_of_week(value);
			else if("low".equals(localName))
				forecastCondition.setLow(value);
			else if("high".equals(localName))
				forecastCondition.setHigh(value);
			else if("icon".equals(localName))
				forecastCondition.setIcon(value);
			else if("condition".equals(localName))
				forecastCondition.setCondition(value);
			
		}
		
	}
	
	@Override
	public void startDocument() throws SAXException {
		forecastList = new ArrayList<ForecastCondition>();
		currentCondition = new CurrentCondition();		
	}

	
}
