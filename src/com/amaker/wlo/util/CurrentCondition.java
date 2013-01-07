package com.amaker.wlo.util;

/**
 * <current_conditions>
 * <condition data="����" />
 * <temp_f data="91" />
 * <temp_c data="33" />
 * <humidity data="ʪ�ȣ� 59%" />
 * <icon data="/ig/images/weather/cn_cloudy.gif" />
 * <wind_condition data="���� �ϡ����٣�5 ��/��" />
 * </current_conditions>
 */
public class CurrentCondition {
	/**
	 * condition ��������
	 */
	private String condition;
	
	/**
	 * temp_c ��ǰ�¶�
	 */
	private String temp_c;
	
	/**
	 * humidity ʪ��
	 */
	private String humidity;
	
	/**
	 * icon ͼƬ����·��
	 */
	private String icon;
	
	/**
	 * wind_condition ���򣬷���
	 */
	private String wind_condition;

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getTemp_c() {
		return temp_c;
	}

	public void setTemp_c(String tempC) {
		temp_c = tempC;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getWind_condition() {
		return wind_condition;
	}

	public void setWind_condition(String windCondition) {
		wind_condition = windCondition;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("��ǰ��").append(condition).append("��")
			.append(temp_c).append("��C��").append(humidity)
			.append("��").append(wind_condition);
		return buf.toString();
	}
	
}
