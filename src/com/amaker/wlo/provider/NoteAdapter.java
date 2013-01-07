package com.amaker.wlo.provider;

import com.amaker.wlo.util.OrderDataBaseUtil;
import com.amaker.wlo.util.OrderStringUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 菜单表信息维护
 * @author Administrator
 */
public class NoteAdapter {
	
	/**
	 * 表名
	 */
	private String tableName = "notes";
	
	/**
	 * 列名
	 */
	public static final String ID = "_id";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String LOGINID = "loginid";
	public static final String REFDATE = "refdate";
	public static final String CREATE_AT = "create_at";
	
	private SQLiteDatabase sdb ;
	private OrderDataBaseUtil orderDB;
	
	public NoteAdapter(Context context){
		orderDB = new OrderDataBaseUtil(context, tableName, new String[]{TITLE, CONTENT, LOGINID, REFDATE, CREATE_AT},
				new String[]{OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL, 
					OrderStringUtil.TEXT_NOT_NULL, OrderStringUtil.TEXT_NOT_NULL});
		sdb = orderDB.openWriteDB();
	}
	
	/**
	 * 打开数据库
	 */
	public void openDB(){
		sdb = orderDB.openWriteDB();
	}
	
	/**
	 * 增
	 */
	public long saveNote(String title, String content, String loginid, String refDate, String createAt){
		ContentValues values = new ContentValues();
		
		values.put(TITLE, title);
		values.put(CONTENT, content);
		values.put(LOGINID, loginid);
		values.put(REFDATE, refDate);
		values.put(CREATE_AT, createAt);
		
		long tag = sdb.insert(tableName, null, values);
		return tag;
		
	}
	
	/**
	 * 查
	 */
	public Cursor queryNotes(String date){
		
		Cursor cur = sdb.query(tableName, new String[]{ID, TITLE, CONTENT, LOGINID, REFDATE, CREATE_AT},
				REFDATE+"=?", new String[]{date}, null, null, "create_at desc");
		
		return cur;
	}
	
	/**
	 * 查备忘录
	 */
	public int queryNumbers(String date) {
		Cursor cur = sdb.query(tableName, new String[]{ID},
				REFDATE+"=?", new String[]{date}, null, null, null);
		
		return cur.getCount();
	}

	/**
	 * 关闭数据库
	 */
	public void closeDB(Cursor cursor) {
		if(null != cursor && !cursor.isClosed())
			cursor.close();
		closeDB();
	}
	
	/**
	 * 关闭数据库
	 */
	public void closeDB() {
		if(null != sdb && sdb.isOpen())
			sdb.close();
	}

}
