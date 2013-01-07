package com.amaker.wlo.util;


/**
 * ChineseCalendarGB.java Copyright (c) 1997-2002 by Dr. Herong Yang �й�ũ���㷨 -
 * ʵ���ڹ��� 1901 ���� 2100 ��֮��� 200 ��
 */
public class CalendarUtil {
	private int gregorianYear;
	private int gregorianMonth;
	private int gregorianDate;
	private boolean isGregorianLeap;
	private int dayOfYear;
	private int dayOfWeek; // ����һ���ڵĵ�һ��
	private int chineseYear;
	private int chineseMonth; // ������ʾ����
	private int chineseDate;
	private int sectionalTerm;
	private int principleTerm;
	private static char[] daysInGregorianMonth = { 31, 28, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 };
	private static String[] stemNames = { "��", "��", "��", "��", "��", "��", "��",
			"��", "��", "��" };
	private static String[] branchNames = { "��", "��", "��", "î", "��", "��", "��",
			"δ", "��", "��", "��", "��" };
	private static String[] animalNames = { "��", "ţ", "��", "��", "��", "��", "��",
			"��", "��", "��", "��", "��" };

	public static final String[] daysOfMonth = { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
			"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "30", "31" };

	private String monthOfAlmanac[] = {"����","����","����","����","����","����","����","����","����","ʮ��","����","����"};
	private String daysOfAlmanac[] = { "��һ", "����", "����", "����", "����", "����",
			"����", "����", "����", "��ʮ", "ʮһ", "ʮ��", "ʮ��", "ʮ��", "ʮ��", "ʮ��", "ʮ��",
			"ʮ��", "ʮ��", "��ʮ", "إһ", "إ��", "إ��", "إ��", "إ��", "إ��", "إ��", "إ��",
			"إ��", "��ʮ" }; // ũ��������

	public CalendarUtil() {
		setGregorian(1901, 1, 1);
	}

	/**
	 * �õ���Ӧ���ũ�� Ҫ�ж����� �³� ��ĩ
	 */
	public String getChineseDay(int y, int m, int d) {

		CalendarUtil c = new CalendarUtil();

		c.setGregorian(y, m, d);
		c.computeChineseFields();
		c.computeSolarTerms();

		int cd = c.getChineseDate();

		return daysOfAlmanac[cd - 1];
	}

	/**
     *  �õ���Ӧ���ũ��
     *  Ҫ�ж����� �³� ��ĩ
     */
	public String getChineseMonth(int y, int m, int d) {
		setGregorian(y,m,d);
		computeChineseFields();
		computeSolarTerms();
		
		int cd = getChineseMonth();
		if(cd < 1 || cd > 29)
			cd = 1;
		return monthOfAlmanac[cd -1];
	}
	
	public void setGregorian(int y, int m, int d) {
		gregorianYear = y;
		gregorianMonth = m;
		gregorianDate = d;
		isGregorianLeap = isGregorianLeapYear(y);
		dayOfYear = dayOfYear(y, m, d);
		dayOfWeek = dayOfWeek(y, m, d);
		chineseYear = 0;
		chineseMonth = 0;
		chineseDate = 0;
		sectionalTerm = 0;
		principleTerm = 0;
	}

	// �ж��Ƿ�������
	public static boolean isGregorianLeapYear(int year) {
		boolean isLeap = false;
		if (year % 4 == 0)
			isLeap = true;
		if (year % 100 == 0)
			isLeap = false;
		if (year % 400 == 0)
			isLeap = true;
		return isLeap;
	}

	// ����һ�����м���
	public static int daysInGregorianMonth(int y, int m) {
		int d = daysInGregorianMonth[m - 1];
		if (m == 2 && isGregorianLeapYear(y))
			d++; // ����������¶�һ��
		return d;
	}

	// ���㵱ǰ���ڱ������ǵڼ���
	public static int dayOfYear(int y, int m, int d) {
		int c = 0;
		for (int i = 1; i < m; i++) {
			c = c + daysInGregorianMonth(y, i);
		}
		c = c + d;
		return c;
	}

	// ��ǰ���Ǳ��ܵĵڼ��� �� �������쿪ʼ��
	public static int dayOfWeek(int y, int m, int d) {
		int w = 1; // ����һ��һ��һ��������һ��������ʼֵΪ������
		y = (y - 1) % 400 + 1; // ��������ֵ�ֲ� 400 ��ѭ��һ��
		int ly = (y - 1) / 4; // �������
		ly = ly - (y - 1) / 100;
		ly = ly + (y - 1) / 400;
		int ry = y - 1 - ly; // �������
		w = w + ry; // ��������ֵ��һ
		w = w + 2 * ly; // ��������ֵ����
		w = w + dayOfYear(y, m, d);
		w = (w - 1) % 7 + 1;
		return w;
	}

	// ũ���·ݴ�Сѹ�����������ֽڱ�ʾһ�ꡣ�����ֽڹ�ʮ����������λ����
	// ǰ�ĸ�λ����ʾ�����·ݣ���ʮ����λ����ʾʮ����ũ���·ݵĴ�С��
	private static char[] chineseMonths = { 0x00, 0x04, 0xad, 0x08, 0x5a, 0x01,
			0xd5, 0x54, 0xb4, 0x09, 0x64, 0x05, 0x59, 0x45, 0x95, 0x0a, 0xa6,
			0x04, 0x55, 0x24, 0xad, 0x08, 0x5a, 0x62, 0xda, 0x04, 0xb4, 0x05,
			0xb4, 0x55, 0x52, 0x0d, 0x94, 0x0a, 0x4a, 0x2a, 0x56, 0x02, 0x6d,
			0x71, 0x6d, 0x01, 0xda, 0x02, 0xd2, 0x52, 0xa9, 0x05, 0x49, 0x0d,
			0x2a, 0x45, 0x2b, 0x09, 0x56, 0x01, 0xb5, 0x20, 0x6d, 0x01, 0x59,
			0x69, 0xd4, 0x0a, 0xa8, 0x05, 0xa9, 0x56, 0xa5, 0x04, 0x2b, 0x09,
			0x9e, 0x38, 0xb6, 0x08, 0xec, 0x74, 0x6c, 0x05, 0xd4, 0x0a, 0xe4,
			0x6a, 0x52, 0x05, 0x95, 0x0a, 0x5a, 0x42, 0x5b, 0x04, 0xb6, 0x04,
			0xb4, 0x22, 0x6a, 0x05, 0x52, 0x75, 0xc9, 0x0a, 0x52, 0x05, 0x35,
			0x55, 0x4d, 0x0a, 0x5a, 0x02, 0x5d, 0x31, 0xb5, 0x02, 0x6a, 0x8a,
			0x68, 0x05, 0xa9, 0x0a, 0x8a, 0x6a, 0x2a, 0x05, 0x2d, 0x09, 0xaa,
			0x48, 0x5a, 0x01, 0xb5, 0x09, 0xb0, 0x39, 0x64, 0x05, 0x25, 0x75,
			0x95, 0x0a, 0x96, 0x04, 0x4d, 0x54, 0xad, 0x04, 0xda, 0x04, 0xd4,
			0x44, 0xb4, 0x05, 0x54, 0x85, 0x52, 0x0d, 0x92, 0x0a, 0x56, 0x6a,
			0x56, 0x02, 0x6d, 0x02, 0x6a, 0x41, 0xda, 0x02, 0xb2, 0xa1, 0xa9,
			0x05, 0x49, 0x0d, 0x0a, 0x6d, 0x2a, 0x09, 0x56, 0x01, 0xad, 0x50,
			0x6d, 0x01, 0xd9, 0x02, 0xd1, 0x3a, 0xa8, 0x05, 0x29, 0x85, 0xa5,
			0x0c, 0x2a, 0x09, 0x96, 0x54, 0xb6, 0x08, 0x6c, 0x09, 0x64, 0x45,
			0xd4, 0x0a, 0xa4, 0x05, 0x51, 0x25, 0x95, 0x0a, 0x2a, 0x72, 0x5b,
			0x04, 0xb6, 0x04, 0xac, 0x52, 0x6a, 0x05, 0xd2, 0x0a, 0xa2, 0x4a,
			0x4a, 0x05, 0x55, 0x94, 0x2d, 0x0a, 0x5a, 0x02, 0x75, 0x61, 0xb5,
			0x02, 0x6a, 0x03, 0x61, 0x45, 0xa9, 0x0a, 0x4a, 0x05, 0x25, 0x25,
			0x2d, 0x09, 0x9a, 0x68, 0xda, 0x08, 0xb4, 0x09, 0xa8, 0x59, 0x54,
			0x03, 0xa5, 0x0a, 0x91, 0x3a, 0x96, 0x04, 0xad, 0xb0, 0xad, 0x04,
			0xda, 0x04, 0xf4, 0x62, 0xb4, 0x05, 0x54, 0x0b, 0x44, 0x5d, 0x52,
			0x0a, 0x95, 0x04, 0x55, 0x22, 0x6d, 0x02, 0x5a, 0x71, 0xda, 0x02,
			0xaa, 0x05, 0xb2, 0x55, 0x49, 0x0b, 0x4a, 0x0a, 0x2d, 0x39, 0x36,
			0x01, 0x6d, 0x80, 0x6d, 0x01, 0xd9, 0x02, 0xe9, 0x6a, 0xa8, 0x05,
			0x29, 0x0b, 0x9a, 0x4c, 0xaa, 0x08, 0xb6, 0x08, 0xb4, 0x38, 0x6c,
			0x09, 0x54, 0x75, 0xd4, 0x0a, 0xa4, 0x05, 0x45, 0x55, 0x95, 0x0a,
			0x9a, 0x04, 0x55, 0x44, 0xb5, 0x04, 0x6a, 0x82, 0x6a, 0x05, 0xd2,
			0x0a, 0x92, 0x6a, 0x4a, 0x05, 0x55, 0x0a, 0x2a, 0x4a, 0x5a, 0x02,
			0xb5, 0x02, 0xb2, 0x31, 0x69, 0x03, 0x31, 0x73, 0xa9, 0x0a, 0x4a,
			0x05, 0x2d, 0x55, 0x2d, 0x09, 0x5a, 0x01, 0xd5, 0x48, 0xb4, 0x09,
			0x68, 0x89, 0x54, 0x0b, 0xa4, 0x0a, 0xa5, 0x6a, 0x95, 0x04, 0xad,
			0x08, 0x6a, 0x44, 0xda, 0x04, 0x74, 0x05, 0xb0, 0x25, 0x54, 0x03 };

	// ��ʼ�գ�����ũ����Ӧ���ڣ�
	// ���� 1901 �� 1 �� 1 �գ���Ӧũ�� 4598 �� 11 �� 11 ��
	private static int baseYear = 1901;
	private static int baseMonth = 1;
	private static int baseDate = 1;
	private static int baseIndex = 0;
	private static int baseChineseYear = 4598 - 1;
	private static int baseChineseMonth = 11;
	private static int baseChineseDate = 11;

	public int computeChineseFields() {
		if (gregorianYear < 1901 || gregorianYear > 2100)
			return 1;
		int startYear = baseYear;
		int startMonth = baseMonth;
		int startDate = baseDate;
		chineseYear = baseChineseYear;
		chineseMonth = baseChineseMonth;
		chineseDate = baseChineseDate;
		// �ڶ�����Ӧ�գ�������߼���Ч��
		// ���� 2000 �� 1 �� 1 �գ���Ӧũ�� 4697 �� 11 �� 25 ��
		if (gregorianYear >= 2000) {
			startYear = baseYear + 99;
			startMonth = 1;
			startDate = 1;
			chineseYear = baseChineseYear + 99;
			chineseMonth = 11;
			chineseDate = 25;
		}
		int daysDiff = 0;
		for (int i = startYear; i < gregorianYear; i++) {
			daysDiff += 365;
			if (isGregorianLeapYear(i))
				daysDiff += 1; // leap year
		}
		for (int i = startMonth; i < gregorianMonth; i++) {
			daysDiff += daysInGregorianMonth(gregorianYear, i);
		}
		daysDiff += gregorianDate - startDate;

		chineseDate += daysDiff;
		int lastDate = daysInChineseMonth(chineseYear, chineseMonth);
		int nextMonth = nextChineseMonth(chineseYear, chineseMonth);
		while (chineseDate > lastDate) {
			if (Math.abs(nextMonth) < Math.abs(chineseMonth))
				chineseYear++;
			chineseMonth = nextMonth;
			chineseDate -= lastDate;
			lastDate = daysInChineseMonth(chineseYear, chineseMonth);
			nextMonth = nextChineseMonth(chineseYear, chineseMonth);
		}
		return 0;
	}

	private static int[] bigLeapMonthYears = {
			// �����µ��������
			6, 14, 19, 25, 33, 36, 38, 41, 44, 52, 55, 79, 117, 136, 147, 150,
			155, 158, 185, 193 };

	public static int daysInChineseMonth(int y, int m) {
		// ע�⣺���� m < 0
		int index = y - baseChineseYear + baseIndex;
		int v = 0;
		int l = 0;
		int d = 30;
		if (1 <= m && m <= 8) {
			v = chineseMonths[2 * index];
			l = m - 1;
			if (((v >> l) & 0x01) == 1)
				d = 29;
		} else if (9 <= m && m <= 12) {
			v = chineseMonths[2 * index + 1];
			l = m - 9;
			if (((v >> l) & 0x01) == 1)
				d = 29;
		} else {
			v = chineseMonths[2 * index + 1];
			v = (v >> 4) & 0x0F;
			if (v != Math.abs(m)) {
				d = 0;
			} else {
				d = 29;
				for (int i = 0; i < bigLeapMonthYears.length; i++) {
					if (bigLeapMonthYears[i] == index) {
						d = 30;
						break;
					}
				}
			}
		}
		return d;
	}

	public static int nextChineseMonth(int y, int m) {
		int n = Math.abs(m) + 1;
		if (m > 0) {
			int index = y - baseChineseYear + baseIndex;
			int v = chineseMonths[2 * index + 1];
			v = (v >> 4) & 0x0F;
			if (v == m)
				n = -m;
		}
		if (n == 13)
			n = 1;
		return n;
	}

	private static char[][] sectionalTermMap = {
			{ 7, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 5, 5,
					5, 5, 5, 4, 5, 5 },
			{ 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3,
					3, 4, 4, 3, 3, 3 },
			{ 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,
					5, 5, 4, 5, 5, 5, 5 },
			{ 5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4,
					4, 5, 4, 4, 4, 4, 5 },
			{ 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,
					5, 5, 4, 5, 5, 5, 5 },
			{ 6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5,
					5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 5 },
			{ 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,
					7, 7, 6, 6, 6, 7, 7 },
			{ 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,
					7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 7 },
			{ 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,
					7, 7, 6, 7, 7, 7, 7 },
			{ 9, 9, 9, 9, 8, 9, 9, 9, 8, 8, 9, 9, 8, 8, 8, 9, 8, 8, 8, 8, 7, 8,
					8, 8, 7, 7, 8, 8, 8 },
			{ 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7,
					7, 7, 6, 6, 7, 7, 7 },
			{ 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,
					7, 7, 6, 6, 6, 7, 7 } };
	private static char[][] sectionalTermYear = {
			{ 13, 49, 85, 117, 149, 185, 201, 250, 250 },
			{ 13, 45, 81, 117, 149, 185, 201, 250, 250 },
			{ 13, 48, 84, 112, 148, 184, 200, 201, 250 },
			{ 13, 45, 76, 108, 140, 172, 200, 201, 250 },
			{ 13, 44, 72, 104, 132, 168, 200, 201, 250 },
			{ 5, 33, 68, 96, 124, 152, 188, 200, 201 },
			{ 29, 57, 85, 120, 148, 176, 200, 201, 250 },
			{ 13, 48, 76, 104, 132, 168, 196, 200, 201 },
			{ 25, 60, 88, 120, 148, 184, 200, 201, 250 },
			{ 16, 44, 76, 108, 144, 172, 200, 201, 250 },
			{ 28, 60, 92, 124, 160, 192, 200, 201, 250 },
			{ 17, 53, 85, 124, 156, 188, 200, 201, 250 } };
	private static char[][] principleTermMap = {
			{ 21, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20,
					20, 20, 20, 20, 20, 19, 20, 20, 20, 19, 19, 20 },
			{ 20, 19, 19, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19, 19,
					19, 18, 18, 19, 19, 18, 18, 18, 18, 18, 18, 18 },
			{ 21, 21, 21, 22, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21,
					20, 20, 20, 21, 20, 20, 20, 20, 19, 20, 20, 20, 20 },
			{ 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 20, 20, 20, 20,
					19, 20, 20, 20, 19, 19, 20, 20, 19, 19, 19, 20, 20 },
			{ 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21,
					20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 21 },
			{ 22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22,
					21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 21 },
			{ 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23,
					22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 23 },
			{ 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,
					22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23 },
			{ 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,
					22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23 },
			{ 24, 24, 24, 24, 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24,
					23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 23 },
			{ 23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23,
					22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 22 },
			{ 22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 21, 22, 22, 22,
					21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21, 22 } };
	private static char[][] principleTermYear = {
			{ 13, 45, 81, 113, 149, 185, 201 },
			{ 21, 57, 93, 125, 161, 193, 201 },
			{ 21, 56, 88, 120, 152, 188, 200, 201 },
			{ 21, 49, 81, 116, 144, 176, 200, 201 },
			{ 17, 49, 77, 112, 140, 168, 200, 201 },
			{ 28, 60, 88, 116, 148, 180, 200, 201 },
			{ 25, 53, 84, 112, 144, 172, 200, 201 },
			{ 29, 57, 89, 120, 148, 180, 200, 201 },
			{ 17, 45, 73, 108, 140, 168, 200, 201 },
			{ 28, 60, 92, 124, 160, 192, 200, 201 },
			{ 16, 44, 80, 112, 148, 180, 200, 201 },
			{ 17, 53, 88, 120, 156, 188, 200, 201 } };

	public int computeSolarTerms() {
		if (gregorianYear < 1901 || gregorianYear > 2100)
			return 1;
		sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth);
		principleTerm = principleTerm(gregorianYear, gregorianMonth);
		return 0;
	}

	public static int sectionalTerm(int y, int m) {
		if (y < 1901 || y > 2100)
			return 0;
		int index = 0;
		int ry = y - baseYear + 1;
		while (ry >= sectionalTermYear[m - 1][index])
			index++;
		int term = sectionalTermMap[m - 1][4 * index + ry % 4];
		if ((ry == 121) && (m == 4))
			term = 5;
		if ((ry == 132) && (m == 4))
			term = 5;
		if ((ry == 194) && (m == 6))
			term = 6;
		return term;
	}

	public static int principleTerm(int y, int m) {
		if (y < 1901 || y > 2100)
			return 0;
		int index = 0;
		int ry = y - baseYear + 1;
		while (ry >= principleTermYear[m - 1][index])
			index++;
		int term = principleTermMap[m - 1][4 * index + ry % 4];
		if ((ry == 171) && (m == 3))
			term = 21;
		if ((ry == 181) && (m == 5))
			term = 21;
		return term;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Gregorian Year: " + gregorianYear + "\n");
		buf.append("Gregorian Month: " + gregorianMonth + "\n");
		buf.append("Gregorian Date: " + gregorianDate + "\n");
		buf.append("Is Leap Year: " + isGregorianLeap + "\n");
		buf.append("Day of Year: " + dayOfYear + "\n");
		buf.append("Day of Week: " + dayOfWeek + "\n");
		buf.append("Chinese Year: " + chineseYear + "\n");
		buf.append("Heavenly Stem: " + ((chineseYear - 1) % 10) + "\n");
		buf.append("Earthly Branch: " + ((chineseYear - 1) % 12) + "\n");
		buf.append("Chinese Month: " + chineseMonth + "\n");
		buf.append("Chinese Date: " + chineseDate + "\n");
		buf.append("Sectional Term: " + sectionalTerm + "\n");
		buf.append("Principle Term: " + principleTerm + "\n");
		return buf.toString();
	}

	public String[] getYearTable() {
		setGregorian(gregorianYear, 1, 1);
		computeChineseFields();
		computeSolarTerms();
		String[] table = new String[58]; // 6*9 + 4
		table[0] = getTextLine(27, "����������" + gregorianYear);
		table[1] = getTextLine(27, "ũ��������" + (chineseYear + 1) + " ("
				+ stemNames[(chineseYear + 1 - 1) % 10]
				+ branchNames[(chineseYear + 1 - 1) % 12] + " - "
				+ animalNames[(chineseYear + 1 - 1) % 12] + "��)");
		int ln = 2;
		String blank = "                                         " + "  "
				+ "                                         ";
		String[] mLeft = null;
		String[] mRight = null;
		for (int i = 1; i <= 6; i++) {
			table[ln] = blank;
			ln++;
			mLeft = getMonthTable();
			mRight = getMonthTable();
			for (int j = 0; j < mLeft.length; j++) {
				String line = mLeft[j] + "  " + mRight[j];
				table[ln] = line;
				ln++;
			}
		}
		table[ln] = blank;
		ln++;
		table[ln] = getTextLine(0, "##/## - ��������/ũ�����ڣ�(*)#�� - (��)ũ���µ�һ��");
		ln++;
		return table;
	}

	public static String getTextLine(int s, String t) {
		String str = "                                         " + "  "
				+ "                                         ";
		if (t != null && s < str.length() && s + t.length() < str.length())
			str = str.substring(0, s) + t + str.substring(s + t.length());
		return str;
	}

	private static String[] monthNames = { "һ", "��", "��", "��", "��", "��", "��",
			"��", "��", "ʮ", "ʮһ", "ʮ��" };

	public String[] getMonthTable() {
		setGregorian(gregorianYear, gregorianMonth, 1);
		computeChineseFields();
		computeSolarTerms();
		String[] table = new String[8];
		String title = null;
		if (gregorianMonth < 11)
			title = "                   ";
		else
			title = "                 ";
		title = title + monthNames[gregorianMonth - 1] + "��"
				+ "                   ";
		String header = "   ��    һ    ��    ��    ��    ��    �� ";
		String blank = "                                          ";
		table[0] = title;
		table[1] = header;
		int wk = 2;
		String line = "";
		for (int i = 1; i < dayOfWeek; i++) {
			line += "     " + ' ';
		}
		int days = daysInGregorianMonth(gregorianYear, gregorianMonth);
		for (int i = gregorianDate; i <= days; i++) {
			line += getDateString() + ' ';
			rollUpOneDay();
			if (dayOfWeek == 1) {
				table[wk] = line;
				line = "";
				wk++;
			}
		}
		for (int i = dayOfWeek; i <= 7; i++) {
			line += "     " + ' ';
		}
		table[wk] = line;
		for (int i = wk + 1; i < table.length; i++) {
			table[i] = blank;
		}
		for (int i = 0; i < table.length; i++) {
			table[i] = table[i].substring(0, table[i].length() - 1);
		}

		return table;
	}

	private static String[] chineseMonthNames = { "��", "��", "��", "��", "��", "��",
			"��", "��", "��", "ʮ", "��", "��" };
	private static String[] principleTermNames = { "��ˮ", "����", "����", "����",
			"����", "����", "����", "���", "˪��", "Сѩ", "����", "��" };
	private static String[] sectionalTermNames = { "����", "����", "����", "����",
			"â��", "С��", "����", "��¶", "��¶", "����", "��ѩ", "С��" };

	public String getDateString() {
		String str = "*  /  ";
		String gm = String.valueOf(gregorianMonth);
		if (gm.length() == 1)
			gm = ' ' + gm;
		String cm = String.valueOf(Math.abs(chineseMonth));
		if (cm.length() == 1)
			cm = ' ' + cm;
		String gd = String.valueOf(gregorianDate);
		if (gd.length() == 1)
			gd = ' ' + gd;
		String cd = String.valueOf(chineseDate);
		if (cd.length() == 1)
			cd = ' ' + cd;
		if (gregorianDate == sectionalTerm) {
			str = " " + sectionalTermNames[gregorianMonth - 1];
		} else if (gregorianDate == principleTerm) {
			str = " " + principleTermNames[gregorianMonth - 1];
		} else if (chineseDate == 1 && chineseMonth > 0) {
			str = " " + chineseMonthNames[chineseMonth - 1] + "��";
		} else if (chineseDate == 1 && chineseMonth < 0) {
			str = "*" + chineseMonthNames[-chineseMonth - 1] + "��";
		} else {
			str = gd + '/' + cd;
		}
		return str;
	}

	public int rollUpOneDay() {
		dayOfWeek = dayOfWeek % 7 + 1;
		dayOfYear++;
		gregorianDate++;
		int days = daysInGregorianMonth(gregorianYear, gregorianMonth);
		if (gregorianDate > days) {
			gregorianDate = 1;
			gregorianMonth++;
			if (gregorianMonth > 12) {
				gregorianMonth = 1;
				gregorianYear++;
				dayOfYear = 1;
				isGregorianLeap = isGregorianLeapYear(gregorianYear);
			}
			sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth);
			principleTerm = principleTerm(gregorianYear, gregorianMonth);
		}
		chineseDate++;
		days = daysInChineseMonth(chineseYear, chineseMonth);
		if (chineseDate > days) {
			chineseDate = 1;
			chineseMonth = nextChineseMonth(chineseYear, chineseMonth);
			if (chineseMonth == 1)
				chineseYear++;
		}
		return 0;
	}

	public int getGregorianYear() {
		return gregorianYear;
	}

	public void setGregorianYear(int gregorianYear) {
		this.gregorianYear = gregorianYear;
	}

	public int getGregorianMonth() {
		return gregorianMonth;
	}

	public void setGregorianMonth(int gregorianMonth) {
		this.gregorianMonth = gregorianMonth;
	}

	public int getGregorianDate() {
		return gregorianDate;
	}

	public void setGregorianDate(int gregorianDate) {
		this.gregorianDate = gregorianDate;
	}

	public boolean isGregorianLeap() {
		return isGregorianLeap;
	}

	public void setGregorianLeap(boolean isGregorianLeap) {
		this.isGregorianLeap = isGregorianLeap;
	}

	public int getDayOfYear() {
		return dayOfYear;
	}

	public void setDayOfYear(int dayOfYear) {
		this.dayOfYear = dayOfYear;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int getChineseYear() {
		return chineseYear;
	}

	public void setChineseYear(int chineseYear) {
		this.chineseYear = chineseYear;
	}

	public int getChineseMonth() {
		return chineseMonth;
	}

	public void setChineseMonth(int chineseMonth) {
		this.chineseMonth = chineseMonth;
	}

	public int getChineseDate() {
		return chineseDate;
	}

	public void setChineseDate(int chineseDate) {
		this.chineseDate = chineseDate;
	}

	public int getSectionalTerm() {
		return sectionalTerm;
	}

	public void setSectionalTerm(int sectionalTerm) {
		this.sectionalTerm = sectionalTerm;
	}

	public int getPrincipleTerm() {
		return principleTerm;
	}

	public void setPrincipleTerm(int principleTerm) {
		this.principleTerm = principleTerm;
	}

	public static char[] getDaysInGregorianMonth() {
		return daysInGregorianMonth;
	}

	public static void setDaysInGregorianMonth(char[] daysInGregorianMonth) {
		CalendarUtil.daysInGregorianMonth = daysInGregorianMonth;
	}

	public static String[] getStemNames() {
		return stemNames;
	}

	public static void setStemNames(String[] stemNames) {
		CalendarUtil.stemNames = stemNames;
	}

	public static String[] getBranchNames() {
		return branchNames;
	}

	public static void setBranchNames(String[] branchNames) {
		CalendarUtil.branchNames = branchNames;
	}

	public static String[] getAnimalNames() {
		return animalNames;
	}

	public static void setAnimalNames(String[] animalNames) {
		CalendarUtil.animalNames = animalNames;
	}

	public static char[] getChineseMonths() {
		return chineseMonths;
	}

	public static void setChineseMonths(char[] chineseMonths) {
		CalendarUtil.chineseMonths = chineseMonths;
	}

	public static int getBaseYear() {
		return baseYear;
	}

	public static void setBaseYear(int baseYear) {
		CalendarUtil.baseYear = baseYear;
	}

	public static int getBaseMonth() {
		return baseMonth;
	}

	public static void setBaseMonth(int baseMonth) {
		CalendarUtil.baseMonth = baseMonth;
	}

	public static int getBaseDate() {
		return baseDate;
	}

	public static void setBaseDate(int baseDate) {
		CalendarUtil.baseDate = baseDate;
	}

	public static int getBaseIndex() {
		return baseIndex;
	}

	public static void setBaseIndex(int baseIndex) {
		CalendarUtil.baseIndex = baseIndex;
	}

	public static int getBaseChineseYear() {
		return baseChineseYear;
	}

	public static void setBaseChineseYear(int baseChineseYear) {
		CalendarUtil.baseChineseYear = baseChineseYear;
	}

	public static int getBaseChineseMonth() {
		return baseChineseMonth;
	}

	public static void setBaseChineseMonth(int baseChineseMonth) {
		CalendarUtil.baseChineseMonth = baseChineseMonth;
	}

	public static int getBaseChineseDate() {
		return baseChineseDate;
	}

	public static void setBaseChineseDate(int baseChineseDate) {
		CalendarUtil.baseChineseDate = baseChineseDate;
	}

	public static int[] getBigLeapMonthYears() {
		return bigLeapMonthYears;
	}

	public static void setBigLeapMonthYears(int[] bigLeapMonthYears) {
		CalendarUtil.bigLeapMonthYears = bigLeapMonthYears;
	}

	public static char[][] getSectionalTermMap() {
		return sectionalTermMap;
	}

	public static void setSectionalTermMap(char[][] sectionalTermMap) {
		CalendarUtil.sectionalTermMap = sectionalTermMap;
	}

	public static char[][] getSectionalTermYear() {
		return sectionalTermYear;
	}

	public static void setSectionalTermYear(char[][] sectionalTermYear) {
		CalendarUtil.sectionalTermYear = sectionalTermYear;
	}

	public static char[][] getPrincipleTermMap() {
		return principleTermMap;
	}

	public static void setPrincipleTermMap(char[][] principleTermMap) {
		CalendarUtil.principleTermMap = principleTermMap;
	}

	public static char[][] getPrincipleTermYear() {
		return principleTermYear;
	}

	public static void setPrincipleTermYear(char[][] principleTermYear) {
		CalendarUtil.principleTermYear = principleTermYear;
	}

	public static String[] getMonthNames() {
		return monthNames;
	}

	public static void setMonthNames(String[] monthNames) {
		CalendarUtil.monthNames = monthNames;
	}

	public static String[] getChineseMonthNames() {
		return chineseMonthNames;
	}

	public static void setChineseMonthNames(String[] chineseMonthNames) {
		CalendarUtil.chineseMonthNames = chineseMonthNames;
	}

	public static String[] getPrincipleTermNames() {
		return principleTermNames;
	}

	public static void setPrincipleTermNames(String[] principleTermNames) {
		CalendarUtil.principleTermNames = principleTermNames;
	}

	public static String[] getSectionalTermNames() {
		return sectionalTermNames;
	}

	public static void setSectionalTermNames(String[] sectionalTermNames) {
		CalendarUtil.sectionalTermNames = sectionalTermNames;
	}

	public static void main(String[] arg) {
		CalendarUtil c = new CalendarUtil();
		String cmd = "day";
		int y = 2010;
		int m = 8;
		int d = 11;

		c.setGregorian(y, m, d);
		c.computeChineseFields();
		c.computeSolarTerms();

		if (cmd.equalsIgnoreCase("year")) {
			String[] t = c.getYearTable();
			for (int i = 0; i < t.length; i++)
				System.out.println(t[i]);
		} else if (cmd.equalsIgnoreCase("month")) {
			String[] t = c.getMonthTable();
			for (int i = 0; i < t.length; i++)
				System.out.println(t[i]);
		} else {
			System.out.println(c.toString());
		}
		System.out.println(c.getDateString());

	}

}