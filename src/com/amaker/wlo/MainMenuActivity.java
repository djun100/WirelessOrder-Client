package com.amaker.wlo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.amaker.wlo.provider.OrderAdapter;
import com.amaker.wlo.util.CalendarUtil;
import com.amaker.wlo.util.OrderHttpUtil;
import com.amaker.wlo.util.OrderStringUtil;
import com.amaker.wlo.util.OrderUrlUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
	private TextView txtinfo;
	private ImageButton menu_dc;
	private ImageButton menu_sc;
	private ImageButton menu_gx;
	
	private ImageButton menu_yl;
	private ImageButton menu_tq;
	private ImageButton menu_rl;

	private ImageButton menu_xx;
	private ImageButton menu_sz;
	private ImageButton menu_tc;
	private ProgressDialog proDlg;
	private String str;
	private String result;
	private String data;
	private OrderAdapter dbAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        ShowInfos();
        FindViewID();
        SetListener();
}
   
	private void FindViewID() {
		menu_dc=(ImageButton) findViewById(R.id.menu_dc);
		menu_dc.setBackgroundResource(0);
		menu_sc = (ImageButton)findViewById(R.id.menu_sc);
		menu_sc.setBackgroundResource(0);
		menu_gx = (ImageButton)findViewById(R.id.menu_gx);
		menu_gx.setBackgroundResource(0);
		
		menu_rl = (ImageButton)findViewById(R.id.menu_rl);
		menu_rl.setBackgroundResource(0);
		menu_yl = (ImageButton)findViewById(R.id.menu_yl);
		menu_yl.setBackgroundResource(0);
		menu_tq = (ImageButton)findViewById(R.id.menu_tq);
		menu_tq.setBackgroundResource(0);

		menu_xx = (ImageButton)findViewById(R.id.menu_xx);
		menu_xx.setBackgroundResource(0);
		menu_sz = (ImageButton)findViewById(R.id.menu_sz);
		menu_sz.setBackgroundResource(0);
		menu_tc = (ImageButton)findViewById(R.id.menu_tc);
		menu_tc.setBackgroundResource(0);
		
	}
	/**���������ָ�û���¼ʱ��һЩ��Ϣ��������¼�û���Ϣ����ǰʱ�䣬���ڵ�**/
	private void ShowInfos() {
		txtinfo = (TextView) findViewById(R.id.infor_text);
       /**
		 * str �ǵ�½�ĳɹ����� �����ʽΪ id,loginid,password,nikename,create_at
		 */
		str = OrderStringUtil.getDataFromIntent(getIntent());

		StringBuilder builder = new StringBuilder();

		builder.append("��ӭ�����ֻ����ϵͳ,���ǽ�����Ϊ������").append("\n\n");

		builder.append("��ӭ����").append("\n");
		
		CalendarUtil cu = new CalendarUtil();
		String chineseMonth = cu.getChineseMonth(
				Integer.parseInt(OrderStringUtil.getCurrentDate("yyyy")),
				Integer.parseInt(OrderStringUtil.getCurrentDate("MM")),
				Integer.parseInt(OrderStringUtil.getCurrentDate("dd")));
		String chineseDay = cu.getChineseDay(
				Integer.parseInt(OrderStringUtil.getCurrentDate("yyyy")),
				Integer.parseInt(OrderStringUtil.getCurrentDate("MM")),
				Integer.parseInt(OrderStringUtil.getCurrentDate("dd")));
		builder.append("�����ǣ�").append(
				OrderStringUtil.getCurrentDate("yyyy��MM��dd��"));
		builder.append("\nũ����").append(chineseMonth).append(chineseDay);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String ly_time = sdf.format(new Date());
		builder.append("\n���ڱ���ʱ��:").append(ly_time);
		txtinfo.setText(builder.toString());
		System.out.println("Main:" + "����������");
		
	}
	 private void SetListener() {
		 menu_dc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				proDlg = OrderStringUtil.createProgressDialog(MainMenuActivity.this, getResources().getString(R.string.pro_title), 
						getResources().getString(R.string.pro_message), true, false);
				
				proDlg.show();
				new Thread(){

					@Override
					public void run() {
                     result = checkOrderVersion();
						
						Message m = new Message();						
						
						if("exception".equals(result))
							m.what = OrderStringUtil.SERVER_ERROR;
						else if("error".equals(result))
							m.what = OrderStringUtil.SERVER_NO_DATA;
						else if(result.split(",").length == 4)
							m.what = OrderStringUtil.DATA_DETAIL;
						else if("010010101101110101000100".equals(result))
							m.what = OrderStringUtil.GO_ORDER;
						else 
							m.what = OrderStringUtil.ERROR;
						handler.sendMessage(m);
					}

				 	
				}.start();
			}
		});
		 menu_tc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
				startActivity(intent);
				
			}
		});
		 /**
			 * ��Ʒ�ղ�
			 */
			menu_sc.setOnClickListener(new ImageButton.OnClickListener() {
				
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, OrderCollectActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			/**�˵�����**/
			menu_gx.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					proDlg = OrderStringUtil.createProgressDialog(MainMenuActivity.this, getResources().getString(R.string.pro_title), 
							getResources().getString(R.string.pro_message), true, false);
					
					proDlg.show();
					new Thread(){
						@Override
						public void run() {
							/**
							 * exception Ϊ����������
							 *  ��   ��      ɾ    ��
							 * new,update,delete,error
							 * 010010101101110101000100 ���
							 */
							result = checkOrderVersion();
							
							Message m = new Message();
							
							if("exception".equals(result))
								m.what = OrderStringUtil.SERVER_ERROR;							
							else if("010010101101110101000100".equals(result))
								m.what = OrderStringUtil.OK;
							else if(result.split(",").length == 4)
								m.what = OrderStringUtil.DATA_DETAIL;
							else 
								m.what = OrderStringUtil.ERROR;
							
							handler.sendMessage(m);
						}
					}.start();
				}
			});
			/**
			 * ����
			 */
			menu_yl.setOnClickListener(new ImageButton.OnClickListener() {
				
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, AmusementActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			
			/**
			 * ����Ԥ��
			 */
			menu_tq.setOnClickListener(new ImageButton.OnClickListener() {
				
				public void onClick(View v) {
					Intent intent = new Intent(MainMenuActivity.this, WeatherActivity.class);
					Bundle b = new Bundle();
					b.putString("data", str);
					intent.putExtra("data", b);
					startActivity(intent);
				}
			});
			/**
			 * ��������
			 */
			menu_rl.setOnClickListener(new ImageButton.OnClickListener() {
				
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, CalendarActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			
			/**
			 * ��Ϣ�޸�
			 */
			menu_xx.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, ModifyActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			
			/**
			 * ��������
			 */
			menu_sz.setOnClickListener(new ImageButton.OnClickListener() {			
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, SettingActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			
	}

		private String checkOrderVersion() {
			dbAdapter = new OrderAdapter(MainMenuActivity.this);
			/**
			 *  ������
			 *  ���߱���������Ϣ����װ�ַ�����������������
			 *  ���������������ݣ����ظ��½����״̬
			 */

			Cursor cursor = dbAdapter.queryOrderVersion();
			
			StringBuilder buf = new StringBuilder();
			if(cursor.moveToFirst()){
				do{
					/**
					 * �õ�ID
					 */
					int idIndex = cursor.getColumnIndex(OrderAdapter.ORDER_ID);
					String id = cursor.getString(idIndex);
					buf.append(id).append("-");
					/**
					 * �õ��汾
					 */
					int versionIndex = cursor.getColumnIndex(OrderAdapter.VERSION);
					String version = cursor.getString(versionIndex);
					buf.append(version).append(",");
					
					cursor.moveToNext();
				}while(!cursor.isAfterLast());
			}
			
			dbAdapter.closeDB();
			data = buf.toString();
			
			if(data.length() > 0)
				data = data.substring(0, data.length()-1);
			
			String queryString = "data=" + data;
			String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.CHECK_ORDER_VERSION + queryString;
			
			String result = OrderHttpUtil.getHttpPostResultForUrl(url);
			return result;
		}
	 /**��Ϣ����**/
	 Handler handler=new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			AlertDialog.Builder builder=new AlertDialog.Builder(MainMenuActivity.this);
			proDlg.dismiss();
			switch (msg.what) {
			case OrderStringUtil.SERVER_ERROR:
				builder.setTitle("����������")
					.setIcon(R.drawable.alert_wanring)
					.setMessage("�������������Ժ����ԣ�")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
				break;
			case OrderStringUtil.SERVER_NO_DATA:
				builder.setTitle("������")
					.setIcon(R.drawable.alert_error)
					.setMessage("���������ݣ����ܵ�ˣ�")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
				break;
			case OrderStringUtil.DATA_DETAIL:
				final String r[] = result.split(",");
				builder.setTitle("ͬ���˵�")
					.setIcon(R.drawable.alert_ok)
					.setMessage("����Ҫ��������\n ������" + r[0] + "\t���£�" + r[1] + "\n ɾ����" + r[2] + "\t����" + r[3])
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						
							// code update order ���²˵���
							
							Bundle bundle = new Bundle();
							bundle.putString("new", r[0]);
							bundle.putString("update", r[1]);
							bundle.putString("delete", r[2]);
							bundle.putString("error", r[3]);
							bundle.putString("data", data);

							Intent intent = new Intent(MainMenuActivity.this, OrderUpdateActivity.class);
							intent.putExtra("data", bundle);
							startActivity(intent);
						}
					}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
				break;
			case OrderStringUtil.GO_ORDER:
				/**
				 * ���
				 */
				Intent intent = new Intent(MainMenuActivity.this, OrderListActivity.class);				
				OrderStringUtil.putDataIntoIntent(intent, str);				
				startActivity(intent);
				break;
			case OrderStringUtil.ERROR :
				builder.setTitle("����")
					.setIcon(R.drawable.alert_error)
					.setMessage("���������ԣ�����")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						   
						}
					}).show();
				break;
			case OrderStringUtil.OK:
				builder.setTitle("���ݲ���Ҫ����")
					.setIcon(R.drawable.alert_wanring)
					.setMessage("���ݲ���Ҫ���£�������ֱ�ӵ��")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(MainMenuActivity.this, OrderListActivity.class);
							OrderStringUtil.putDataIntoIntent(intent, str);
							startActivity(intent);
						}
					}).show();
				break;
			}
			
		}
		 
	 };
}
