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
	/**�������Ŀ��������ʾע����Ϣ�����������ע��ʧ��/�ɹ���**/
	protected void showRegisterMsg(String res){
		if ("0".equals(res)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			builder.setIcon(R.drawable.alert_error)
					.setTitle("ע��ʧ��")
					.setMessage("ע��ʧ�ܣ����Ժ����ԣ�")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						// ���ȷ����ť
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
			return ;
		}
		if ("1".equals(res)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			builder.setIcon(R.drawable.alert_add)
					.setTitle("ע��ɹ�")
					.setMessage("��ϲ��,ע��ɹ�,�����μ�������룡")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						// ���ȷ����ť
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
					.setTitle("�����Ѵ���")
					.setMessage("�����Ѵ��ڣ���ʹ���������䣡")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						// ���ȷ����ť
						public void onClick(DialogInterface dialog, int which) {
							email.setText("");
						}
					}).show();
			return ;
		}
		if("3".equals(res)){
			AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
			builder.setIcon(R.drawable.alert_error)
					.setTitle("��½�˺��Ѵ���")
					.setMessage("��½�˺��Ѵ��ڣ���ʹ�������˺ţ�")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						// ���ȷ����ť
						public void onClick(DialogInterface dialog, int which) {
							username.setText("");
						}
					}).show();
			return ;
		}
	}
	
	/**
	 * ����ע�����¼�
	 */
	OnClickListener  registenerListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final String name=username.getText().toString();//��ȡ�û���
			final String upwd = password.getText().toString();
			final String umail = email.getText().toString();
			/**�û���Ϊ��*/
			if ("".equals(name.trim())) {
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle(R.string.login_account_null)
						.setMessage(R.string.login_account_null)
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							// ���ȷ����ť
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
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							// ���ȷ����ť
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
						.setTitle("���䲻��Ϊ��")
						.setMessage("���䲻��Ϊ��")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							// ���ȷ����ť
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							// ���ȡ����ť
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).show();
				return ;
			} 
			if(!OrderStringUtil.emailRule(umail)){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle("�����ַ����")
						.setMessage("�����ַ����")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							// ���ȷ����ť
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							// ���ȡ����ť
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).setNeutralButton("����ȡ��", new DialogInterface.OnClickListener() {
							// ���ȡ����ť
							public void onClick(DialogInterface dialog, int which) {
								email.setText("");
							}
						}).show();
				return ;
			} 
			if("".equals(gender)){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle("��ѡ����")
						.setMessage("��ѡ����")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							// ���ȷ����ť
							public void onClick(DialogInterface dialog, int which) {									
							}
						}).show();
				return ;
			} 
			if(!accept){
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setIcon(R.drawable.alert_wanring)
						.setTitle("������")
						.setMessage("������")
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							// ���ȷ����ť
							public void onClick(DialogInterface dialog, int which) {									
							}
						}).show();
				return ;
			}
			proDlg=OrderStringUtil.createProgressDialog(RegisterActivity.this, "���Ժ�....", "����ע����.....", true, true);
			proDlg.show();
			new Thread(){

				@Override
				public void run() {
					/**��֤�û��Ƿ����,�����ڣ�ע��,����ɹ��ˣ������û�������ʾ������¼����**/
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
