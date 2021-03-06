package com.aura.smartschool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.aura.smartschool.Constant;
import com.aura.smartschool.Interface.LoginDialogListener;
import com.aura.smartschool.MainActivity;
import com.aura.smartschool.R;
import com.aura.smartschool.utils.SchoolLog;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MemberVO;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginDialog extends Dialog {
	
	private Button btn_login;
	private TextView tv_register;
	private TextView tv_guest;
	private TextView tv_find_family_name;
	private EditText et_id;
	
	private Context mContext;
	LoginDialogListener mListener;
	
	public LoginDialog(Context context, LoginDialogListener listener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		mListener = listener;
	}
	
	@Override
	public void onBackPressed() {
		((MainActivity)mContext).onBackPressed();;
		return;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.6f;
		getWindow().setAttributes(lpWindow);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		setContentView(R.layout.dialog_login);
		
		btn_login = (Button)findViewById(R.id.btn_login);
		tv_register = (TextView)findViewById(R.id.tv_register);
		tv_guest = (TextView)findViewById(R.id.tv_guest);
		tv_find_family_name = (TextView)findViewById(R.id.tv_find_family_name);

		btn_login.setOnClickListener(mClick);
		tv_register.setOnClickListener(mClick);
		tv_guest.setOnClickListener(mClick);
		tv_find_family_name.setOnClickListener(mClick);

		et_id = (EditText)findViewById(R.id.et_id);

	}

	View.OnClickListener mClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_login:
				
				String id = et_id.getText().toString();
				if(TextUtils.isEmpty(id)){
					Util.showToast(mContext, "가족명을 입력하세요.");
					return;
				}
				
				//로그인 버튼 클릭시 id는 항상 저장
				//로그인 이후 저장해야함
				//PreferenceUtil.getInstance(mContext).putHomeId(id);
				
				MemberVO member = new MemberVO();
				member.home_id = et_id.getText().toString();
				member.mdn = Util.getMdn(mContext);
				
				//로그인 처리
				mListener.doLogin(member);
				break;

			case R.id.tv_register:
				mListener.gotoRegister();
				break;
				
			case R.id.tv_guest:
				mListener.onPreView();
				break;

			case R.id.tv_find_family_name:
				findFamilyName();
				break;
				
			default:
				break;
			}
		}
	};

	private void findFamilyName() {
		LoadingDialog.showLoading(mContext);
		try {
			String url = Constant.HOST + Constant.API_GET_HOMEBYNUMBER;

			JSONObject json = new JSONObject();
			json.put("mdn", Util.getMdn(mContext));

			SchoolLog.d("LDK", "url:" + url);
			SchoolLog.d("LDK", "input parameter:" + json.toString(1));

			new AQuery(mContext).post(url, json, JSONObject.class, new AjaxCallback<JSONObject>() {
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					try {
						LoadingDialog.hideLoading();
						SchoolLog.d("LDK", "result:" + object.toString(1));

						if (status.getCode() != 200) {
							return;
						}

						if ("0".equals(object.getString("result"))) {
							JSONObject data = object.getJSONObject("data");
							et_id.setText(data.getString("home_id"));
						} else {
							Util.showToast(mContext, object.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}