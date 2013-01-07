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
	/**这个方法是指用户登录时候一些信息，包括登录用户信息，当前时间，日期等**/
	private void ShowInfos() {
		txtinfo = (TextView) findViewById(R.id.infor_text);
       /**
		 * str 是登陆的成功数据 具体格式为 id,loginid,password,nikename,create_at
		 */
		str = OrderStringUtil.getDataFromIntent(getIntent());

		StringBuilder builder = new StringBuilder();

		builder.append("欢迎访问手机点餐系统,我们将热心为您服务。").append("\n\n");

		builder.append("欢迎您：").append("\n");
		
		CalendarUtil cu = new CalendarUtil();
		String chineseMonth = cu.getChineseMonth(
				Integer.parseInt(OrderStringUtil.getCurrentDate("yyyy")),
				Integer.parseInt(OrderStringUtil.getCurrentDate("MM")),
				Integer.parseInt(OrderStringUtil.getCurrentDate("dd")));
		String chineseDay = cu.getChineseDay(
				Integer.parseInt(OrderStringUtil.getCurrentDate("yyyy")),
				Integer.parseInt(OrderStringUtil.getCurrentDate("MM")),
				Integer.parseInt(OrderStringUtil.getCurrentDate("dd")));
		builder.append("今天是：").append(
				OrderStringUtil.getCurrentDate("yyyy年MM月dd日"));
		builder.append("\n农历：").append(chineseMonth).append(chineseDay);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String ly_time = sdf.format(new Date());
		builder.append("\n现在北京时间:").append(ly_time);
		txtinfo.setText(builder.toString());
		System.out.println("Main:" + "这是主界面");
		
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
			 * 菜品收藏
			 */
			menu_sc.setOnClickListener(new ImageButton.OnClickListener() {
				
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, OrderCollectActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			/**菜单更新**/
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
							 * exception 为服务器错误
							 *  新   改      删    错
							 * new,update,delete,error
							 * 010010101101110101000100 点菜
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
			 * 娱乐
			 */
			menu_yl.setOnClickListener(new ImageButton.OnClickListener() {
				
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, AmusementActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			
			/**
			 * 天气预报
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
			 * 日历记事
			 */
			menu_rl.setOnClickListener(new ImageButton.OnClickListener() {
				
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, CalendarActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			
			/**
			 * 信息修改
			 */
			menu_xx.setOnClickListener(new ImageButton.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(MainMenuActivity.this, ModifyActivity.class);
					OrderStringUtil.putDataIntoIntent(i, str);
					startActivity(i);
				}
			});
			
			/**
			 * 个性设置
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
			 *  检查更新
			 *  更具本地数据信息，组装字符串，发动到服务器
			 *  服务器检查更新数据，返回更新结果和状态
			 */

			Cursor cursor = dbAdapter.queryOrderVersion();
			
			StringBuilder buf = new StringBuilder();
			if(cursor.moveToFirst()){
				do{
					/**
					 * 得到ID
					 */
					int idIndex = cursor.getColumnIndex(OrderAdapter.ORDER_ID);
					String id = cursor.getString(idIndex);
					buf.append(id).append("-");
					/**
					 * 得到版本
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
	 /**消息处理**/
	 Handler handler=new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			AlertDialog.Builder builder=new AlertDialog.Builder(MainMenuActivity.this);
			proDlg.dismiss();
			switch (msg.what) {
			case OrderStringUtil.SERVER_ERROR:
				builder.setTitle("服务器错误")
					.setIcon(R.drawable.alert_wanring)
					.setMessage("服务器错误，请稍后重试！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
				break;
			case OrderStringUtil.SERVER_NO_DATA:
				builder.setTitle("无数据")
					.setIcon(R.drawable.alert_error)
					.setMessage("服务无数据，不能点菜！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
				break;
			case OrderStringUtil.DATA_DETAIL:
				final String r[] = result.split(",");
				builder.setTitle("同步菜单")
					.setIcon(R.drawable.alert_ok)
					.setMessage("你需要更新数据\n 新增：" + r[0] + "\t更新：" + r[1] + "\n 删除：" + r[2] + "\t错误：" + r[3])
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						
							// code update order 更新菜单表
							
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
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
				break;
			case OrderStringUtil.GO_ORDER:
				/**
				 * 点菜
				 */
				Intent intent = new Intent(MainMenuActivity.this, OrderListActivity.class);				
				OrderStringUtil.putDataIntoIntent(intent, str);				
				startActivity(intent);
				break;
			case OrderStringUtil.ERROR :
				builder.setTitle("错误")
					.setIcon(R.drawable.alert_error)
					.setMessage("错误，请重试！！！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						   
						}
					}).show();
				break;
			case OrderStringUtil.OK:
				builder.setTitle("数据不需要更新")
					.setIcon(R.drawable.alert_wanring)
					.setMessage("数据不需要更新，您可以直接点菜")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
