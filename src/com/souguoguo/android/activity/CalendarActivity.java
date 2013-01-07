package com.souguoguo.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.souguoguo.android.R;
import com.souguoguo.android.provider.NoteAdapter;
import com.souguoguo.android.util.CalendarUtil;
import com.souguoguo.android.util.OrderStringUtil;

public class CalendarActivity extends Activity {
	private String str;
	private NoteAdapter noteDB;
	
	private TextView[] calendarText;
	private TextView calendarDescription;
	
	private ImageButton previousYear;
	private ImageButton nextYear;
	private ImageButton nowDate;
	private ImageButton previousMonth;
	private ImageButton nextMonth;
	
	private String year;
	private String month;
	private String day;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_calendar);
str = OrderStringUtil.getDataFromIntent(getIntent());
		
		year = OrderStringUtil.getCurrentDate("yyyy");
		month = OrderStringUtil.getCurrentDate("MM");
		day = OrderStringUtil.getCurrentDate("dd");
		
		/**
		 * ��ʼ�����
		 */
		initObject();
		
		/**
		 * �������������
		 */
		setDataToObject();
		
		/**
		 * �����������¼�
		 */
		addListenerOnObject();
		
		/**
		 * ���ı���Ӱ�ť
		 */
		 setTextListener();
	}
	/**
	 * ���ı���Ӱ�ť
	 */
	private void setTextListener() {
		
	}

	/**
	 * �����������¼�
	 */
	private void addListenerOnObject() {
		previousYear.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!"1901".equals(year)){
					year = (Integer.parseInt(year) - 1) + "";
					setDataToObject();
				}
			}
		});
		nextYear.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!"2100".equals(year)){
					year = (Integer.parseInt(year) + 1) + "";
					setDataToObject();
				}
			}
		});
		previousMonth.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("1".equals(month)){
					if(!"1901".equals(year)){
						year = (Integer.parseInt(year) - 1) + "";
						month = "12";
					}
				} else {
					month = (Integer.parseInt(month) - 1) + "";
				}
				setDataToObject();
			}
		});
		nextMonth.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("12".equals(month)){
					if(!"2100".equals(year)){
						year = (Integer.parseInt(year) + 1) + "";
						month = "1";
					}
				} else {
					month = (Integer.parseInt(month) + 1) + "";
				}
				setDataToObject();
			}
		});
		nowDate.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				year = OrderStringUtil.getCurrentDate("yyyy");
				month = OrderStringUtil.getCurrentDate("MM");
				day = OrderStringUtil.getCurrentDate("dd");
				setDataToObject();
			}
		});
	}
	
	/**
	 * �������������
	 */
	private void setDataToObject() {
		noteDB = new NoteAdapter(CalendarActivity.this);
		CalendarUtil cu = new CalendarUtil();
		String chineseDay = "";
		String chineseMonth = "";
		chineseMonth = cu.getChineseMonth(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
		chineseDay = cu.getChineseDay(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
		StringBuilder buf = new StringBuilder();
		buf.append("��ǰ��").append(year).append("-")
			.append(month).append("-").append(day).append("��ũ����" + chineseMonth + chineseDay);
		calendarDescription.setText(buf);
		int y = Integer.parseInt(year);
		int m = Integer.parseInt(month);
		
		int days = CalendarUtil.daysInGregorianMonth(y, m);
		int start = CalendarUtil.dayOfWeek(y, m, 1);
		
		/**
		 * ǰ��Ϊ�ղ���
		 */
		for(int i = 0; i < start; i ++){
			calendarText[i].setText(" \n ");
			calendarText[i].setBackgroundResource(0);
		}
		
		/**
		 * ��������
		 */
		for(int i = 0; i < days; i ++){		
			chineseDay = cu.getChineseDay(y,m,Integer.parseInt(CalendarUtil.daysOfMonth[i]));
			if("��һ".equals(chineseDay))
				chineseDay = cu.getChineseMonth(y,m,Integer.parseInt(CalendarUtil.daysOfMonth[i]));
			calendarText[start+ i - 1].setBackgroundColor(getResources().getColor(R.color.calendar_text_bg));
			String text = CalendarUtil.daysOfMonth[i] + "\n" + chineseDay;
			calendarText[start+ i - 1].setText(text);
			
			/**
			 * ��ĩ�ж�
			 */
			if(restDay(year, month, CalendarUtil.daysOfMonth[i])){
				calendarText[start+ i - 1].setBackgroundColor(getResources().getColor(R.color.rest_color));
			}
			
			/**
			 * ����¼�ж�
			 */
			if(hansSomthing(year, month, CalendarUtil.daysOfMonth[i])){
				calendarText[start+ i - 1].setBackgroundColor(getResources().getColor(R.color.red));
			}
			
			/**
			 * ����¼�
			 */
			calendarText[start+ i - 1].setOnClickListener(new TextView.OnClickListener() {
				@Override
				public void onClick(View v) {
					CalendarUtil cutil = new CalendarUtil();
					String chineseDay = "";
					String chineseMonth = "";
					String d = ((TextView)v).getText().toString().substring(0, ((TextView)v).getText().toString().lastIndexOf('\n'));
					chineseMonth = cutil.getChineseMonth(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(d));
					chineseDay = cutil.getChineseDay(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(d));
					StringBuilder buf = new StringBuilder();
					buf.append("��ǰ��").append(year).append("-")
						.append(month).append("-").append(d).append("��ũ����").append(chineseMonth).append(chineseDay);
					calendarDescription.setText(buf);
				}
			});
			
			/**
			 * ��ʱ�����¼�
			 */
			calendarText[start+ i - 1].setOnLongClickListener(new TextView.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					CalendarUtil c = new CalendarUtil();
					StringBuilder date = new StringBuilder();
					String d = ((TextView)v).getText().toString().substring(0, ((TextView)v).getText().toString().lastIndexOf('\n'));
					String chineseMonth = c.getChineseMonth(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(d));
					String chineseDay = c.getChineseDay(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(d));
					date.append(year).append("-").append(month).append("-").append(d)
						.append("��ũ����").append(chineseMonth).append(chineseDay);
					Intent i  = new Intent(CalendarActivity.this, OrderNoteActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					i.putExtra("date", date.toString());
					i.putExtra("addDate", year + "-" + month + "-" + d);
					startActivity(i);
					return false;
				}
			});       
		}
		noteDB.closeDB();
		/**
		 * ����Ϊ�ղ���
		 */
		for(int i = days+start-1; i < calendarText.length; i ++){
			calendarText[i].setText(" \n ");
			calendarText[i].setBackgroundResource(0);
		}
		
	}

	/**
	 * ����¼�ж�
	 */
	private boolean hansSomthing(String y, String m, String d) {
		int count = noteDB.queryNumbers(y + "-" + m + "-" + d);
		if(count > 0)
			return true;
		return false;
	}

	/**
	 * ��ĩ�ж�
	 */
	private boolean restDay(String y, String m, String d) {
		
		int dow = CalendarUtil.dayOfWeek(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));

		boolean flag = false;
		// ��ĩ
		if(1 == dow || 7 == dow)
			flag = true;
		
		return flag;
	}

	/**
	 * ��ʼ�����
	 */
	private void initObject() {
		
		// �����ı�
		calendarDescription =(TextView)findViewById(R.id.calendar_text_description);
		
		// ��ť
		previousYear = (ImageButton)findViewById(R.id.calendar_year_previous_btn);
		nextYear = (ImageButton)findViewById(R.id.calendar_year_next_btn);
		previousMonth = (ImageButton)findViewById(R.id.calendar_month_previous_btn);
		nextMonth = (ImageButton)findViewById(R.id.calendar_month_next_btn);
		nowDate = (ImageButton)findViewById(R.id.calendar_now_date_btn);
		
		// ����
		int i = 0;
		calendarText = new TextView[42];
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_1);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_2);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_3);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_4);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_5);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_6);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_7);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_8);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_9);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_10);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_11);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_12);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_13);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_14);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_15);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_16);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_17);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_18);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_19);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_20);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_21);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_22);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_23);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_24);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_25);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_26);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_27);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_28);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_29);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_30);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_31);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_32);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_33);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_34);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_35);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_36);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_37);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_38);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_39);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_40);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_41);
		calendarText[i++]=(TextView)findViewById(R.id.calendar_text_42);
	}
}
