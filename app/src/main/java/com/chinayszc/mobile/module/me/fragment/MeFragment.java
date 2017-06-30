package com.chinayszc.mobile.module.me.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.module.common.GoBuyConfig;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.activity.AboutActivity;
import com.chinayszc.mobile.module.main.MainNavigationActivity;
import com.chinayszc.mobile.module.me.activity.AccountActivity;
import com.chinayszc.mobile.module.me.activity.FeedbackActivity;
import com.chinayszc.mobile.module.me.activity.MyBankCardActivity;
import com.chinayszc.mobile.module.me.activity.NotificationCenterActivity;
import com.chinayszc.mobile.module.me.activity.SecondAboutActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.okhttp.callback.NormalStringCallback;
import com.chinayszc.mobile.utils.DateUtils;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.MD5Utils;
import com.chinayszc.mobile.utils.PhoneUtils;
import com.chinayszc.mobile.utils.PreferencesUtils;
import com.gopay.mobilepaybygopay_wap.GopayByWap;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 更多Fragment
 * Created by Jerry on 2017/3/25.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {

	private TextView myCodeTV;
	private TextView mySloganTV;
	private String phoneNum;
	private FrameLayout logoutFL;
	private MainNavigationActivity mActivity;

	public static MeFragment newInstance() {
		Bundle args = new Bundle();
		MeFragment moreFragment = new MeFragment();
		moreFragment.setArguments(args);
		return moreFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Logs.d("MeFragment--onCreateView");
		View mView = inflater.inflate(R.layout.me_fragment, null);
		FrameLayout accountFL = (FrameLayout) mView.findViewById(R.id.me_my_account);
		FrameLayout bankCardFL = (FrameLayout) mView.findViewById(R.id.me_bank_card);
		FrameLayout notificationFL = (FrameLayout) mView.findViewById(R.id.me_notification);
		FrameLayout aboutFL = (FrameLayout) mView.findViewById(R.id.me_about_yisheng);
		TextView logoutTV = (TextView) mView.findViewById(R.id.me_logout);
		logoutFL = (FrameLayout) mView.findViewById(R.id.me_logout_layout);
		myCodeTV = (TextView) mView.findViewById(R.id.me_code);
		mySloganTV = (TextView) mView.findViewById(R.id.me_slogan);
		mActivity = (MainNavigationActivity) getActivity();

		accountFL.setOnClickListener(this);
		bankCardFL.setOnClickListener(this);
		notificationFL.setOnClickListener(this);
		aboutFL.setOnClickListener(this);
		logoutTV.setOnClickListener(this);

		String versionName = getResources().getString(R.string.version) + Env.versionName;
//		versionTV.setText(versionName);

        myCodeTV.setVisibility(View.GONE);
        mySloganTV.setVisibility(View.GONE);

		return mView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.me_my_account:
				IntentUtils.startActivity(getActivity(), AccountActivity.class);
				break;
			case R.id.me_bank_card:
				IntentUtils.startActivity(getActivity(), MyBankCardActivity.class);
				break;
			case R.id.me_notification:
				IntentUtils.startActivity(getActivity(), NotificationCenterActivity.class);
				break;
			case R.id.me_about_yisheng:
			    Bundle bu = new Bundle();
                bu.putString("phoneNum", phoneNum);
				IntentUtils.startActivity(getActivity(), SecondAboutActivity.class, bu);
				break;
			case R.id.me_feed_back:
				IntentUtils.startActivity(getActivity(), FeedbackActivity.class);
				break;
			case R.id.me_version:
				break;
			case R.id.me_phone:
				if (!TextUtils.isEmpty(phoneNum)) {
					PhoneUtils.dial(getActivity(), phoneNum);
				}
				break;
			case R.id.me_logout:
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).
						setTitle("退出？").
						setMessage("您确定退出吗？").
						setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								logout();
							}
						}).
						setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						}).
						create();
				alertDialog.show();
				break;
			default:
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loadPoint();
		if (Env.isLoggedIn) {
			logoutFL.setVisibility(View.VISIBLE);
			mActivity.titleBar.getActionTV().setVisibility(View.GONE);
		} else {
			logoutFL.setVisibility(View.GONE);
			mActivity.titleBar.getActionTV().setText("登录");
			mActivity.titleBar.getActionTV().setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 加载个人信息
	 */
	private void loadPoint() {
		OkHttpUtils.post().url(Urls.ME_MAIN)
				.addCommonHeaderAndBody()
				.build()
				.execute(new JsonObjectCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Logs.i("Point onError---" + e.getMessage());
					}

					@Override
					public void onResponse(JSONObject response, int id) {
						if (null != response) {
							int isLogin = response.optInt("isLogin", 0);
							phoneNum = response.optString("phone");
							String phone = "客服电话：" + response.optString("phoneShow");
							if (isLogin == 1) {
								String slogan = response.optString("slogan");
								String code = getResources().getString(R.string.my_code) + response.optString("code");
								myCodeTV.setText(code);
								mySloganTV.setText(slogan);
							}
						}
					}
				});
	}

	/**
	 * 退出
	 */
	private void logout() {
		OkHttpUtils.post().url(Urls.LOG_OUT)
				.addCommonHeaderAndBody()
				.build()
				.execute(new JsonObjectCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Toast.makeText(getActivity(), "退出失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onResponse(JSONObject response, int id) {
						if (null != response) {
							int isLogin = response.optInt("isLogin", 0);
							if (isLogin == 0) {
								PreferencesUtils.setPreferences(getActivity(),
										PreferencesUtils.USER_CONTENT, "isLoggedIn", false);
								PreferencesUtils.setPreferences(getActivity(),
										PreferencesUtils.USER_CONTENT, "userAccount", "");
								Env.isLoggedIn = false;
								Env.token = null;

								logoutFL.setVisibility(View.GONE);
								mActivity.titleBar.getActionTV().setText("登录");
								mActivity.titleBar.getActionTV().setVisibility(View.VISIBLE);

								Toast.makeText(getActivity(), "退出成功", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getActivity(), "退出失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
	}

	/**
	 * 国付宝支付
	 */
	private void goPay() {

		// TODO: 2017/6/12
		String merOrderNum = "1010101111222";
		String tranAmt = "100";
		String feeAmt = "0";
		String tranDateTime = DateUtils.getTime(System.currentTimeMillis(), "yyyyMMddHHmmss");
		String frontMerUrl = "http://api.azhongzhi.com/api/home/imageList.htm";
		String tranIP = "127.0.0.1";
		String verify_code = "";

		String buyerRealMobile = "18923456789";
		String buyerRealBankAcctNum = "12344561234561234";

		HashMap<String, String> authInfo = new HashMap<>();
		authInfo.put("version", GoBuyConfig.VERSION);					//网关接口版本号
		authInfo.put("charset", GoBuyConfig.CHAR_SET);                 //1 GBK 2 UTF-8
		authInfo.put("language", GoBuyConfig.LANGUAGE);                //1 中文 2 英文
		authInfo.put("signType", GoBuyConfig.SIGN_TYPE);			   	//1 MD5 2 SHA
		authInfo.put("tranCode", GoBuyConfig.TRAN_CODE);			//本域指明了交易的类型，支付网关接口必须为8888
		authInfo.put("merchantID", GoBuyConfig.MERCHANT_ID);			//签约国付宝商户唯一用户ID
		authInfo.put("merOrderNum", merOrderNum);		//用于传送商户订单号信息，每笔新的交易需生成一笔新的订单号，如果isRepeatSubmit为1，则未支付的订单号可以重复提交，但要保证交易金额一致。
		authInfo.put("tranAmt", tranAmt);			//本域值仅包含交易本金，不包含任何服务费，且其值在交易的整个过程中保持不变
		authInfo.put("feeAmt", feeAmt);				//商户提取佣金金额
		authInfo.put("currencyType", GoBuyConfig.CURRENCY_TYPE);		//多币种预留字段，暂只能为156，代表人民币
		authInfo.put("frontMerUrl", frontMerUrl);			//商户前台通知地址
		authInfo.put("backgroundMerUrl", GoBuyConfig.BACKGROUND_MERURL);			//商户后台通知地址
		authInfo.put("tranDateTime", tranDateTime);			//订单发起的交易时间
		// TODO: 2017/6/11 virCardNoIn
		authInfo.put("virCardNoIn", GoBuyConfig.VIR_CARDNUM_INFO);			//卖家在国付宝平台开设的国付宝账户号。
		authInfo.put("tranIP", tranIP);				//发起交易的客户IP地址。
		// TODO: 2017/6/11 isRepeatSubmit
		authInfo.put("isRepeatSubmit", "");			//订单是否允许重复提交。0不允许重复,1 允许重复 默认
		// TODO: 2017/6/11 goodsName
		authInfo.put("goodsName", "");			//参与交易的商品名称。
		// TODO: 2017/6/11 goodsDetail
		authInfo.put("goodsDetail", "");		//参与交易商品的详细信息
		// TODO: 2017/6/11 buyerName
		authInfo.put("buyerName", "");			//移动支付标记
		// TODO: 2017/6/11 buyerContact
		authInfo.put("buyerContact", "");		//买方联系方式
		// TODO: 2017/6/11 buyerRealMobile
		authInfo.put("buyerRealMobile", buyerRealMobile);		//买方联系方式
		// TODO: 2017/6/11 buyerRealBankAcctNum
		authInfo.put("buyerRealBankAcctNum", buyerRealBankAcctNum);			//银行卡号
		// TODO: 2017/6/11 merRemark1
		authInfo.put("merRemark1", "");			//商户备注
		// TODO: 2017/6/11 merRemark2
		authInfo.put("merRemark2", "");			//商户备注

		String signValue = MD5Utils.getMD5("version=["+GoBuyConfig.VERSION+"]tranCode=["+GoBuyConfig.TRAN_CODE+"]merchantID=["+GoBuyConfig.MERCHANT_ID+"]merOrderNum=["+merOrderNum+"]tranAmt=["+tranAmt+"]feeAmt=["+feeAmt+"]"+"tranDateTime=["+tranDateTime+"]frontMerUrl=["+frontMerUrl+"]backgroundMerUrl=["+GoBuyConfig.BACKGROUND_MERURL+"]orderId=["+""+"]gopayOutOrderId=["+""+"]tranIP=["+tranIP+"]"+"respCode=["+""+"]gopayServerTime=["+""+"]VerficationCode=["+verify_code+"]");

		authInfo.put("signValue",signValue);

		Bundle bundle = new Bundle();
		bundle.putSerializable("AuthInfo", authInfo);
		IntentUtils.startActivity(getActivity(), GopayByWap.class, bundle);
	}

}
