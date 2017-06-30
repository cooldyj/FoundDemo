package com.chinayszc.mobile.module.demo;

import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.chinayszc.mobile.R;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.MultiActionsNotificationBuilder;
import cn.jpush.android.api.TagAliasCallback;


public class PushSetActivity extends InstrumentedActivity implements OnClickListener {
	private static final String TAG = "JPush";



	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		init();
		initListener();
	}

	private void init() {
	}

	private void initListener() {
	}

	@Override
	public void onClick(View v) {
	}

	private void setTag() {
		String tag = "normal_user";

		// 检查 tag 的有效性
		if (TextUtils.isEmpty(tag)) {
			Toast.makeText(PushSetActivity.this, "error_tag_empty", Toast.LENGTH_SHORT).show();
			return;
		}

		// ","隔开的多个 转换成 Set
		String[] sArray = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for (String sTagItme : sArray) {
			if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
				Toast.makeText(PushSetActivity.this, "error_tag_gs_empty", Toast.LENGTH_SHORT).show();
				return;
			}
			tagSet.add(sTagItme);
		}

		//调用JPush API设置Tag
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

	}

	private void setAlias() {
		String alias = "user_alias";
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(PushSetActivity.this, "error_alias_empty", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(PushSetActivity.this, "error_tag_gs_empty", Toast.LENGTH_SHORT).show();
			return;
		}

		//调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}


	/**
	 * 设置通知提示方式 - 基础属性
	 */
	private void setStyleBasic() {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(PushSetActivity.this);
		builder.statusBarDrawable = R.mipmap.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
		JPushInterface.setPushNotificationBuilder(1, builder);
		Toast.makeText(PushSetActivity.this, "Basic Builder - 1", Toast.LENGTH_SHORT).show();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setAddActionsStyle() {
		MultiActionsNotificationBuilder builder = new MultiActionsNotificationBuilder(PushSetActivity.this);
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "first", "my_extra1");
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "second", "my_extra2");
		builder.addJPushAction(R.drawable.jpush_ic_richpush_actionbar_back, "third", "my_extra3");
		JPushInterface.setPushNotificationBuilder(10, builder);

		Toast.makeText(PushSetActivity.this, "AddActions Builder - 10", Toast.LENGTH_SHORT).show();
	}


	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "Set tag and alias success";
					Log.i(TAG, logs);
					break;

				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.i(TAG, logs);
					if (ExampleUtil.isConnected(getApplicationContext())) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
					} else {
						Log.i(TAG, "No network");
					}
					break;

				default:
					logs = "Failed with errorCode = " + code;
					Log.e(TAG, logs);
			}

			ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
				case 0:
					logs = "Set tag and alias success";
					Log.i(TAG, logs);
					break;

				case 6002:
					logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
					Log.i(TAG, logs);
					if (ExampleUtil.isConnected(getApplicationContext())) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
					} else {
						Log.i(TAG, "No network");
					}
					break;

				default:
					logs = "Failed with errorCode = " + code;
					Log.e(TAG, logs);
			}

			ExampleUtil.showToast(logs, getApplicationContext());
		}

	};

	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;


	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SET_ALIAS:
					Log.d(TAG, "Set alias in handler.");
					JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
					break;

				case MSG_SET_TAGS:
					Log.d(TAG, "Set tags in handler.");
					JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
					break;

				default:
					Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};

}