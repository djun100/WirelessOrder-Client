package com.amaker.wlo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.amaker.wlo.util.OrderHttpUtil;
import com.amaker.wlo.util.OrderStringUtil;
import com.amaker.wlo.util.OrderUrlUtil;

public class RegisterActivity extends Activity {
	private ImageButton m_register;
	private ImageButton m_reset;
	private ImageButton m_go_back;
	
	private EditText username;
	private EditText password;
	private EditText email;

	private RadioGroup m_gender;
	private RadioButton m_boy;
	private RadioButton m_gril;

	private CheckBox m_accept;
	
	private String gender = "";
	private boolean accept = false;
	
	private ProgressDialog proDlg;
	private String res;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		m_register = (ImageButton)findViewById(R.id.imb_reg_register);
		m_reset = (ImageButton)findViewById(R.id.imb_reg_reset);
		m_go_back = (ImageButton)findViewById(R.id.imb_reg_go_back);
		
		username = (EditText)findViewById(R.id.text_reg_username);
		password = (EditText)findViewById(R.id.text_reg_password);
		email = (EditText)findViewById(R.id.text_reg_email);
		
		m_gender = (RadioGroup)findViewById(R.id.gender_radio);
		m_boy = (RadioButton)findViewById(R.id.radio_boy);
		m_gril = (RadioButton)findViewById(R.id.radio_gril);
		
		m_accept = (CheckBox)findViewById(R.id.cb_accept);
		m_register.setOnClickListener(registenerListener);
		m_reset.setOnClickListener(resetListener);
		m_go_back.setOnClickListener(backListner);
		m_gender.setOnCheckedChangeListener(genderListener);
		m_accept.setOnClickListener(new RadioGroup.OnClickListener() {
			public void onClick(View v) {
				if(m_accept.isChecked())
					accept = true;
				else
					accept = false;
			}
		});
	}
	/**这个方法目的在于提示注册信息的情况，包括注册失败/成功等**/
	protected void showRegisterMsg(String res){
		if ("0".equals(res)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			builder.setIcon(R.drawable.alert_error)
					.setTitle("注册失败")
					.setMessage("注册失败，请稍后再试！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						// 点击确定按钮
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
			return ;
		}
		if ("1".equals(res)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			builder.setIcon(R.drawable.alert_add)
					.setTitle("注册成功")
					.setMessage("恭喜你,注册成功,请你牢记你的密码！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						// 点击确定按钮
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
							startActivity(intent);
						}
					}).show();
			return ;
		}
		if("2".equals(res)){
			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			builder.setIcon(R.drawable.alert_error)
					.setTitle("邮箱已存在")
					.setMessage("邮箱已存在，请使用其它邮箱！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						// 点击确定按钮
						public void onClick(DialogInterface dialog, int which) {
							email.setText("");
						}
					}).show();
			return ;
		}
		if("3".equals(res)){
			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			builder.setIcon(R.drawable.alert_error)
					.setTitle("登陆账号已存在")
					.setMessage("登陆账号已存在，请使用其它账号！")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						// 点击确定按钮
						public void onClick(DialogInterface dialog, int which) {
							username.setText("");
						}
					}).show();
			return ;
		}
	}
	
	/**
	 * 按下注册点击事件
	 */
	OnClickListener  registenerListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final String name=username.getText().toString();//获取用户名
			final String upwd = password.getText().toString();
			final String umail = email.getText().toString();
			/**用户名为空*/
			if ("".equals(name.trim())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle(R.string.login_account_null)
						.setMessage(R.string.login_account_null)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							// 点击确定按钮
							public void onClick(DialogInterface dialog, int which) {
								username.setText("");
								password.setText("");
								email.setText("");
							}
						}).show();
				return ;
			}
			if("".equals(upwd)){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle(R.string.login_password_null)
						.setMessage(R.string.login_password_null)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							// 点击确定按钮
							public void onClick(DialogInterface dialog, int which) {
								password.setText("");
								email.setText("");
							}
						}).show();
				return ;
			} 
			if("".equals(umail.trim())){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle("邮箱不能为空")
						.setMessage("邮箱不能为空")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							// 点击确定按钮
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							// 点击取消按钮
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).show();
				return ;
			} 
			if(!OrderStringUtil.emailRule(umail)){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle("邮箱地址错误")
						.setMessage("邮箱地址错误")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							// 点击确定按钮
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							// 点击取消按钮
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).setNeutralButton("还是取消", new DialogInterface.OnClickListener() {
							// 点击取消按钮
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).show();
				return ;
			} 
			if("".equals(gender)){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle("请选年龄")
						.setMessage("请选年龄")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							// 点击确定按钮
							public void onClick(DialogInterface dialog, int which) {									
							}
						}).show();
				return ;
			} 
			if(!accept){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle("不接收")
						.setMessage("不接受")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							// 点击确定按钮
							public void onClick(DialogInterface dialog, int which) {									
							}
						}).show();
				return ;
			}
			proDlg=OrderStringUtil.createProgressDialog(RegisterActivity.this, "请稍候....", "正在注册中.....", true, true);
			proDlg.show();
			new Thread(){

				@Override
				public void run() {
					/**验证用户是否存在,不存在，注册,如果成功了，返回用户密码显示，最后登录即可**/
					String registerString = "loginId=" + name + "&password=" + upwd + "&email=" + umail + "&gender=" + gender;
					String url = OrderHttpUtil.BASE_URL + OrderUrlUtil.REGISTER_URL + registerString;					
					
					res = OrderHttpUtil.getHttpPostResultForUrl(url);
					handler.sendEmptyMessage(1);
				}
				
			}.start();
		}
	};
	OnClickListener resetListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			username.setText("");
			password.setText("");
			email.setText("");
			m_accept.setChecked(false);
			m_boy.setChecked(false);
			m_gril.setChecked(false);
		}
	};
	OnClickListener backListner=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
			startActivity(intent);
			
		}
	};
	OnCheckedChangeListener genderListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(m_boy.getId() == checkedId)
				gender = "M";
			else if(m_gril.getId() == checkedId)
				gender = "F";
			else 
				gender = "";
		
			
		}
	};
	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg) {
			proDlg.dismiss();
			showRegisterMsg(res);
		};
	};
}
