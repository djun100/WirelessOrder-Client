package com.souguoguo.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.souguoguo.android.R;
import com.souguoguo.android.provider.OrderAdapter;
import com.souguoguo.android.util.OrderHttpUtil;
import com.souguoguo.android.util.OrderStringUtil;
import com.souguoguo.android.util.OrderUrlUtil;

public class OrderUpdateActivity extends Activity {
    private Bundle bundle;
	
	private TextView new_num_text;
	private TextView update_num_text;
	private TextView delete_num_text;
	private TextView error_num_text;
	
	private ImageButton update_order;
	
	private OrderAdapter orderDao;
	
	private ProgressDialog proDlg;
	private String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.UPDATE_ORDERS + "data=";
	/**
	 * �˵�ID�Ͱ汾  orderId-version,orderId-version ...
	 */
	private String data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_order);
		new_num_text = (TextView)findViewById(R.id.new_num_order);
		update_num_text = (TextView)findViewById(R.id.update_num_order);
		delete_num_text = (TextView)findViewById(R.id.delete_num_order);
		error_num_text = (TextView)findViewById(R.id.error_num_order);
		
		update_order = (ImageButton)findViewById(R.id.update_order_button);
		
		orderDao = new OrderAdapter(OrderUpdateActivity.this);
		
		bundle = getIntent().getBundleExtra("data");
		
		final String new_ = bundle.getString("new");
		final String update_ = bundle.getString("update");
		final String delete_ = bundle.getString("delete");
		final String error_ = bundle.getString("error");
		data = bundle.getString("data");
		
		setTextViewValue(new_, update_, delete_, error_);
		update_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				proDlg = OrderStringUtil.createProgressDialog(OrderUpdateActivity.this, getResources().getString(R.string.pro_title),
						getResources().getString(R.string.pro_message), true, true);
				proDlg.show();
				new Thread(){
					@Override
					public void run() {
						/**
						 * ͬ�������˵�
						 */
						if(Integer.parseInt(new_) > 0){
							String res = OrderHttpUtil.getHttpPostResultForUrl(url + data + "&type=new");
							try {
								String reses[] = res.split("#");
								String createAt = OrderStringUtil.getCurrentDate("yyyy-MM-dd");
								for(String str : reses){
									String r[] = str.split("@");
									orderDao.saveOrder(r[0], r[1], r[2], r[3], r[6], r[4], r[5], createAt);
								}
							} catch (Exception e) {
							    
								e.printStackTrace();
							}
							orderDao.closeDB();
							Message nm = new Message();
							nm.what = OrderStringUtil.NEW_ORDER_FINASH;
							handler.sendMessage(nm);
						}
						Log.i("UPDATE_ORDER_END", "new order();");
						/**
						 * ͬ�����²˵�
						 */
						if(Integer.parseInt(update_) > 0){
							String res = OrderHttpUtil.getHttpPostResultForUrl(url.toString() + data + "&type=update");
							String reses[] = res.split("#");
							for(String str : reses){
								String r[] = str.split("@");
								orderDao.updateOrder(r[1], r[2], r[3], r[6], r[4], r[5], r[0]);
							}
							orderDao.closeDB();
							Message update = new Message();
							update.what = OrderStringUtil.UPDATE_ORDER_FINASH;
							handler.sendMessage(update);
						}
						Log.i("UPDATE_ORDER_END", "update order();");
						
						/**
						 * ͬ��ɾ���˵�
						 */
						if(Integer.parseInt(delete_) > 0){
							String res = OrderHttpUtil.getHttpPostResultForUrl(url.toString() + data + "&type=delete");
							String reses[] = res.split(",");
							for(String str : reses){
								orderDao.deleteOrder(str);
							}
							orderDao.closeDB();
							Message delete = new Message();
							delete.what = OrderStringUtil.DELETE_ORDER_FINASH;
							handler.sendMessage(delete);
						}
						Log.i("UPDATE_ORDER_END", "delete order();");
						
						/**
						 * ͬ������˵�
						 */
						if(Integer.parseInt(error_) > 0){
							String res = OrderHttpUtil.getHttpPostResultForUrl(url.toString() + data + "&type=error");
							String reses[] = res.split(",");
							for(String str : reses){
								orderDao.deleteOrder(str);
							}
							orderDao.closeDB();
							orderDao.closeDB();
							Message error = new Message();
							error.what = OrderStringUtil.ERROR_ORDER_FINASH;
							handler.sendMessage(error);
						}
						Log.i("UPDATE_ORDER_END", "error order();");
					}
				}.start();
				
			}
		});
	}
	/**
	 * ����ֵ
	 */
	private void setTextViewValue(String new_, String update_, String delete_,
			String error_) {

		new_num_text.setText(getResources().getString(R.string.new_num_text) + " : " + new_ + " ��");
		update_num_text.setText(getResources().getString(R.string.update_num_text) + " : " + update_ + " ��");
		delete_num_text.setText(getResources().getString(R.string.delete_num_text) + " : " + delete_ + " ��");
		error_num_text.setText(getResources().getString(R.string.error_num_text) + " : " + error_ + " ��");
		
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			proDlg.dismiss();
			
			switch(msg.what){
			case OrderStringUtil.NEW_ORDER_FINASH:
				proDlg.setTitle("��������");
				proDlg.setMessage("�������ݸ������");
				break;
			case OrderStringUtil.UPDATE_ORDER_FINASH:
				proDlg.setTitle("�޸�����");
				proDlg.setMessage("�޸����ݸ������");
				break;
			case OrderStringUtil.DELETE_ORDER_FINASH:
				proDlg.setTitle("ɾ������");
				proDlg.setMessage("ɾ�����ݸ������");
				break;
			case OrderStringUtil.ERROR_ORDER_FINASH:
				proDlg.setTitle("��������");
				proDlg.setMessage("�������ݸ������");
				break;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(OrderUpdateActivity.this);
			builder.setIcon(R.drawable.alert_ok)
					.setTitle("�������")
					.setMessage("�˵�������ɣ������Ե���ˡ�")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						// ���ȷ����ť
						public void onClick(DialogInterface dialog, int which) {
							
							Intent i = new Intent(OrderUpdateActivity.this, OrderListActivity.class);
							i.putExtra("data", bundle);
							startActivity(i);
						}
					}).show();
		}

	};
}
