package com.amaker.wlo;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.amaker.wlo.util.OrderHttpUtil;
import com.amaker.wlo.util.OrderStringUtil;
import com.amaker.wlo.util.OrderUrlUtil;

public class ModifyActivity extends TabActivity {
	private static final String BASE_INFOR = "base_tab";
	private static final String PASSWORD_INFOR = "password_tab";
	private String str;
	private String userData[] = null;
	private String gender;
	
	private TabHost tabHost;
	
	private TextView loginId;
	private TextView nikeName;
	private TextView email;
	private TextView phone;
	private RadioGroup genderGroup;
	private RadioButton genderBoy;
	private RadioButton genderGril;
	private Button baseSubmit;
	
	private TextView oldPassword;
	private TextView newPassword;
	private TextView re_newPassword;
	private Button passwordSubmit;
	
	private ProgressDialog proDlg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infor_modify);
		// ��ʼ�����
				initObject(); 
				//id,loginid,password,nikename,phone,email,gender,create_at	
				str = OrderStringUtil.getDataFromIntent(getIntent());
				userData = str.split(",");
				gender = userData[6];
				//���û�����Ϣ
				setUserBaseData();		
				
				tabHost = getTabHost();
				TabSpec tabBase = tabHost.newTabSpec(BASE_INFOR);
				tabBase.setIndicator("�޸ĸ�����Ϣ", getResources().getDrawable(R.drawable.base_infor));
				tabBase.setContent(R.id.modify_infor_base);
				tabHost.addTab(tabBase);
				
				TabSpec tabPwd = tabHost.newTabSpec(PASSWORD_INFOR);
				tabPwd.setIndicator("�޸ĵ�½����", getResources().getDrawable(R.drawable.password_infor));
				tabPwd.setContent(R.id.modify_infor_password);
				tabHost.addTab(tabPwd);
			
				tabHost.setCurrentTab(0);
				
				tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
					@Override
					public void onTabChanged(String tabId) {
						if(tabId.equals(BASE_INFOR))
							setUserBaseData();
						
						if(tabId.equals(PASSWORD_INFOR)){
							oldPassword.setText("");
							newPassword.setText("");
							re_newPassword.setText("");
						}
					}
				});
				
				setButtonOnclickListener();
	}
	/**
	 * ��������ĵ���¼�
	 */
	private void setButtonOnclickListener() {
		/**
		 * �޸Ļ�����Ϣ
		 */
		baseSubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ModifyActivity.this);
				if("".equals(nikeName.getText().toString().trim())){
					builder.setTitle("�ǳ�Ϊ��").setMessage("�ǳ�Ϊ�գ��������ǳƣ�")
						.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						}).show();
					return;
				}
				if("".equals(email.getText().toString().trim())){
					builder.setTitle("����Ϊ��").setMessage("����Ϊ�գ����������䣡")
						.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						}).show();
					return;
				}
				if(!OrderStringUtil.emailRule(email.getText().toString().trim())){
					builder.setTitle("���䲻�Ϸ�").setMessage("���䲻�Ϸ������������룡")
						.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						}).show();
					return;
				}
				if("".equals(phone.getText().toString().trim())){
					builder.setTitle("�ֻ�����Ϊ��").setMessage("�ֻ�����Ϊ�գ��������ֻ����룡")
						.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						}).show();
					return;
				}
				if(!OrderStringUtil.phoneNumberRule(phone.getText().toString().trim())){
					builder.setTitle("�ֻ����벻�Ϸ�").setMessage("�ֻ����벻�Ϸ����������ֻ����룡")
						.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {}
						}).show();
					return;
				}
				/**
				 * �޸ĸ�����Ϣ
				 */
				StringBuilder requestString = new StringBuilder();
				requestString.append("loginid=").append(userData[1]).append("&nikename=")
					.append(nikeName.getText().toString());
				requestString.append("&email=").append(email.getText().toString());
				requestString.append("&phone=").append(phone.getText().toString());
				requestString.append("&gender=").append(gender);
				final String requestUrl = OrderHttpUtil.BASE_URL + OrderUrlUtil.MODIFY_BASE_INFOR + requestString;
				
				proDlg = OrderStringUtil.createProgressDialog(ModifyActivity.this, "�����ύ����", 
						"���ڷ����������Ժ�...", false, true);
				proDlg.show();
				
				new Thread(){
					@Override
					public void run() {
						String res = OrderHttpUtil.getHttpPostResultForUrl(requestUrl);

						/**
						 * res ����ж�
						 * -1 �޸Ĵ���
						 * 0 �޸ĳɹ�
						 * 1 �����Ѿ�����
						 */
						Message m = new Message();
						if("0".equals(res))
							m.what = OrderStringUtil.BASE_MODIFY_OK;
						else if("1".equals(res))
							m.what = OrderStringUtil.EMAIL_EXISTS;
						else 
							m.what = OrderStringUtil.BASE_ERROR;
						handler.sendMessage(m);
					}
				}.start();
				
				
				
			}
		});
		
		/**
		 * �޸�����
		 */
		passwordSubmit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ModifyActivity.this);
				if("".equals(oldPassword.getText().toString().trim())){
					builder.setTitle("ԭ����Ϊ��").setMessage("ԭ����Ϊ�գ�������ԭ���룡")
					.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if("".equals(newPassword.getText().toString().trim())){
					builder.setTitle("������Ϊ��").setMessage("������Ϊ�գ������������룡")
					.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				if(!re_newPassword.getText().toString().trim().equals(newPassword.getText().toString().trim())){
					builder.setTitle("�������벻���").setMessage("�����������벻�������ȷ�ϣ�")
					.setIcon(R.drawable.alert_wanring).setCancelable(true).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					}).show();
					return;
				}
				
				/**
				 * �޸�����
				 */
				StringBuilder requestString = new StringBuilder();
				requestString.append("loginid=").append(userData[1])
					.append("&oldpwd=").append(oldPassword.getText().toString().trim())
					.append("&newpwd=").append(newPassword.getText().toString().trim());
				final String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.MODIFY_PASSWORD_INFOR + requestString;
				proDlg = OrderStringUtil.createProgressDialog(ModifyActivity.this, 
							"�ύ����", "�����ύ�������ݣ����Ժ�...", false, true);
				proDlg.show();
				new Thread(){
					@Override
					public void run() {
						/**
						 * res ����ж�
						 * -1 �޸Ĵ���
						 * 0 �޸ĳɹ�
						 * 1 ԭ�������
						 */
						String res = OrderHttpUtil.getHttpPostResultForUrl(url);
						Message m = new Message();
						if("-1".equals(res))
							m.what = OrderStringUtil.PASSWORD_ERROR;
						else if("0".equals(res))
							m.what = OrderStringUtil.PASSWORD_MODIFY_OK;
						else 
							m.what = OrderStringUtil.PASSWORD_OLD_REEOR;
						handler.sendMessage(m);
							
					}
				}.start();
			}
		});
		
		/**
		 * �����Ա�
		 */
		genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == genderBoy.getId())
					gender = "M";
				else if(checkedId == genderGril.getId())
					gender = "F";
			}
		});
	}

	/**
	 * ���û�����Ϣ����
	 */
	private void setUserBaseData() {
		loginId.setText(userData[1]);
		
		if(!"�ǳ�".equals(userData[3]))
			nikeName.setText(userData[3]);
		
		if(!"�ֻ�".equals(userData[4]))
			phone.setText(userData[4]);
		
		email.setText(userData[5]);

		if("M".equals(userData[6]))
			genderBoy.setChecked(true);
		else if("F".equals(userData[6]))
			genderGril.setChecked(true);
	}

	/**
	 * ��ʼ�����
	 */
	private void initObject() {
		loginId = (TextView)findViewById(R.id.modify_login_id);
		nikeName = (TextView)findViewById(R.id.modify_nike_name);
		email = (TextView)findViewById(R.id.modify_email_account);
		phone = (TextView)findViewById(R.id.modify_phone_number);
		genderGroup = (RadioGroup)findViewById(R.id.modify_gender);
		genderBoy = (RadioButton)findViewById(R.id.modify_gender_boy);
		genderGril = (RadioButton)findViewById(R.id.modify_gender_gril);
		baseSubmit = (Button)findViewById(R.id.modify_base_submit);
		findViewById(R.id.modify_base_go_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ModifyActivity.this, MainMenuActivity.class);
				OrderStringUtil.putDataIntoIntent(i, str);
				startActivity(i);
			}
		});
		
		oldPassword = (TextView)findViewById(R.id.modify_old_password);
		newPassword = (TextView)findViewById(R.id.modify_new_password);
		re_newPassword = (TextView)findViewById(R.id.modify_re_new_password);
		passwordSubmit = (Button)findViewById(R.id.modify_password_submit);
		findViewById(R.id.modify_password_go_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ModifyActivity.this, MainMenuActivity.class);
				OrderStringUtil.putDataIntoIntent(i, str);
				startActivity(i);
			}
		});
	}
	
	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ModifyActivity.this);
			proDlg.dismiss();
			switch(msg.what){
			case OrderStringUtil.BASE_MODIFY_OK:
				builder.setTitle("��Ϣ�޸ĳ�").setMessage("��Ϣ�޸ĳɹ�����Ҫ���µ�¼������Ϣ.")
					.setIcon(R.drawable.alert_ok).setCancelable(false).setPositiveButton("���µ�¼", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(ModifyActivity.this, MainMenuActivity.class);
							startActivity(i);
						}
					}).setNegativeButton("�Ժ��½", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent i = new Intent(ModifyActivity.this, MainMenuActivity.class);
							OrderStringUtil.putDataIntoIntent(i, str);
							startActivity(i);
						}
					}).show();
				break;
			case OrderStringUtil.EMAIL_EXISTS:
				builder.setTitle("�����Ѿ�����").setMessage("�����Ѿ����ڣ��뻻����������")
				.setIcon(R.drawable.alert_wanring).setCancelable(false).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
				break;
			case OrderStringUtil.BASE_ERROR:
				builder.setTitle("����").setMessage("�������������Ժ����ԣ�")
				.setIcon(R.drawable.alert_error).setCancelable(false).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
				break;
			case OrderStringUtil.PASSWORD_ERROR:
				builder.setTitle("����").setMessage("�������������Ժ����ԣ�")
				.setIcon(R.drawable.alert_error).setCancelable(false).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
				break;
			case OrderStringUtil.PASSWORD_OLD_REEOR:
				builder.setTitle("ԭ�������").setMessage("ԭ���������ȷ�ϣ�")
				.setIcon(R.drawable.alert_wanring).setCancelable(false).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {}
				}).show();
				break;
			case OrderStringUtil.PASSWORD_MODIFY_OK:
				builder.setTitle("�����޸ĳɹ�").setMessage("�����޸ĳɹ��������µ�¼")
				.setIcon(R.drawable.alert_ok).setCancelable(false).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(ModifyActivity.this, MainMenuActivity.class);
						startActivity(i);
					}
				}).show();
				break;
			}  
		};
	};
}
