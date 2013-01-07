package com.souguoguo.android.util;

/**
 * <forecast_conditions>
 * <day_of_week data="����" />
 * <low data="26" />
 * <high data="33" />
 * <icon data="/ig/images/weather/sunny.gif" />
 * <condition data="��" />
 * </forecast_conditions>
 */
public class ForecastCondition {
	/**
	 * day_of_week ����
	 */
	private String day_of_week;
	
	/**
	 * low ����¶�
	 */
	private String low;
	
	/**
	 * high ����¶�
	 */
	private String high;
	
	/**
	 * icon ��ȥͼ��·��
	 */
	private String icon;
	
	/**
	 * condition ��������
	 */
	private String condition;

	public String getDay_of_week() {
		return day_of_week;
	}

	public void setDay_of_week(String dayOfWeek) {
		day_of_week = dayOfWeek;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		
		StringBuilder buf = new StringBuilder();
		
		buf.append(day_of_week);
		buf.append("��").append(high).append("/").append(low)
		   .append("��C").append("��").append(condition);
		
		return buf.toString();
	}
	
}
