package com.souguoguo.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.souguoguo.android.R;
import com.souguoguo.android.util.OrderHttpUtil;
import com.souguoguo.android.util.OrderStringUtil;
import com.souguoguo.android.util.OrderUrlUtil;

public class LoginActivity extends Activity {
	private ImageButton m_login;
	private ImageButton m_clear;
	private ImageButton m_register;

	private EditText m_username;
	private EditText m_password;
	private String uname;
	private String pwd;
	private ProgressDialog prgDialog;
	private String res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		m_login = (ImageButton) findViewById(R.id.imb_login);
		m_clear = (ImageButton) findViewById(R.id.imb_clear);
		m_register = (ImageButton) findViewById(R.id.imb_register);

		m_username = (EditText) findViewById(R.id.text_username);
		m_password = (EditText) findViewById(R.id.text_password);
		LoadUserNamePassword();
		m_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ��½
				m_login.setOnClickListener(new ImageButton.OnClickListener() {
					public void onClick(View v) {				
						uname = m_username.getText().toString().trim();
						pwd = m_password.getText().toString().trim();
						if("".equals(uname)){
							AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
							builder.setIcon(R.drawable.alert_wanring)
									.setTitle(R.string.login_account_null)
									.setMessage(R.string.login_account_null)
									.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
										// ���ȷ����ť
										public void onClick(DialogInterface dialog, int which) {}
									}).show();
							return ;
						}
						if("".equals(pwd)){
							AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
							builder.setIcon(R.drawable.alert_wanring)
									.setTitle(R.string.login_password_null)
									.setMessage(R.string.login_password_null)
									.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
										// ���ȷ����ť
										public void onClick(DialogInterface dialog, int which) {									
										}
									}).show();
							return ;
						}
						
						// ��ʾ��½�Ի���
						prgDialog = new ProgressDialog(LoginActivity.this);
						prgDialog.setIcon(R.drawable.progress);
						prgDialog.setTitle("���Ե�");
						prgDialog.setMessage("���ڵ�½�����Ե�...");
						prgDialog.setCancelable(false);
						prgDialog.setIndeterminate(true);
						prgDialog.show();
						login();

					}
					
				});
				m_clear.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						m_username.setText("");
						m_password.setText("");
						
					}
				});
				m_register.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
						startActivity(intent);
						
						
					}
				});
				
			}
		});

	}
    /***��ʼ���û���������**/
	private void LoadUserNamePassword() {
		SharedPreferences share = getSharedPreferences(
				OrderStringUtil.USER_DATA_PROVIDE,
				PreferenceActivity.MODE_PRIVATE);

		String u_name = share.getString(OrderStringUtil.USERNAME, "");
		m_username.setText(u_name);

		String u_pwd = share.getString(OrderStringUtil.PASSWORD, "");
		m_password.setText(u_pwd);

	}
	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences share = getSharedPreferences(OrderStringUtil.USER_DATA_PROVIDE, PreferenceActivity.MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();
		
		boolean name = share.getBoolean(OrderStringUtil.IS_USER_NAME, false);
		boolean pwd = share.getBoolean(OrderStringUtil.IS_PASSWORD, false);
		if(name)
			editor.putString(OrderStringUtil.USERNAME, m_username.getText().toString());			
		else
			editor.remove(OrderStringUtil.USERNAME);
		if(pwd)
			editor.putString(OrderStringUtil.PASSWORD, m_password.getText().toString());
		else
			editor.remove(OrderStringUtil.PASSWORD);
		editor.commit();
	}
	/***��¼����***/
	protected void login() {
		new Thread(){

			@Override
			public void run() {
				String loginString = "loginid=" + uname + "&password=" + pwd;
				String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.LOGIN_URL + loginString;
				System.out.println(url);
				res = OrderHttpUtil.getHttpPostResultForUrl(url);
				Message m = new Message();
				System.out.println("+++++++++++++++");
				System.out.println("---------------");
				if("-1".equals(res))
					m.what = OrderStringUtil.LOGIN_ERROR;
				else
					m.what = OrderStringUtil.LOGIN_SUCCESS;
				
				handler.sendMessage(m);
			}
			
		}.start();
	}
	/***��һ���߳̿�ʼ��¼�����öԻ�����ʾ�Ƿ��¼�ɹ�***/
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
			
			prgDialog.dismiss();
			switch(msg.what){
			case OrderStringUtil.LOGIN_ERROR:
				builder.setIcon(R.drawable.alert_error)
						.setTitle("����")
						.setMessage("�û��������������ȷ��")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							// ���ȷ����ť
							public void onClick(DialogInterface dialog, int which) {									
							}
						}).show();
				break;
			case OrderStringUtil.LOGIN_SUCCESS:	
				builder.setIcon(R.drawable.alert_ok)
						.setTitle("��½�ɹ�")
						.setMessage("��ϲ������½�ɹ�")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							// ���ȷ����ť
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
								
								Bundle bundle = new Bundle();
								bundle.putString("data", res);
								
								intent.putExtra("data", bundle);
								
								startActivity(intent);
							}
						}).show();
				break;			
			}
		}
	};
}
