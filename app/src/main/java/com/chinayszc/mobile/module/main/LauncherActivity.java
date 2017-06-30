package com.chinayszc.mobile.module.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.utils.IntentUtils;

/**
 * 启动图
 * Created by Jerry Yang on 2017/6/21.
 */

public class LauncherActivity extends BaseActivity {

	final int DELAY_SECONDS = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher_activity);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				IntentUtils.startActivity(LauncherActivity.this, MainNavigationActivity.class);
				LauncherActivity.this.finish();
			}
		}, DELAY_SECONDS);
	}

}
