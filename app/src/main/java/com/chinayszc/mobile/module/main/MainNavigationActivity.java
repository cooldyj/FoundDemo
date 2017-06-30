package com.chinayszc.mobile.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.base.BaseFragment;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.module.home.fragment.HomeFragment;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.module.me.fragment.MeFragment;
import com.chinayszc.mobile.module.assets.fragment.AssetFragment;
import com.chinayszc.mobile.module.products.fragment.ProductsFragment;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.widget.CommonTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面Activity
 * Created by Jerry on 2017/3/25.
 */
public class MainNavigationActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private List<BaseFragment> fragments;           //主页fragment集合
    private int currentIndex = -1;                  //当前选择的tab序号
    private Fragment mCurrentFragment;              //当前选择的tab页
    public CommonTitleView titleBar;               //标题栏
    private BottomNavigationView navigation;        //底部tab栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initData();
        initView();
        switchTab(0);
    }

    /**
     * 初始化4个fragment
     */
    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(ProductsFragment.newInstance());
        fragments.add(AssetFragment.newInstance());
        fragments.add(MeFragment.newInstance());
    }

    /**
     * 初始化view控件
     */
    private void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        titleBar = (CommonTitleView) findViewById(R.id.main_title);
        titleBar.getBackIV().setVisibility(View.GONE);
        titleBar.getActionTV().setText("登录");
        titleBar.getActionTV().setOnClickListener(this);
    }

    /**
     * navigation bar 条目点击事件
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                switchTab(0);
                return true;
            case R.id.navigation_product:
                switchTab(1);
                return true;
            case R.id.navigation_asset:
                switchTab(2);
                return true;
            case R.id.navigation_more:
                switchTab(3);
                return true;
        }
        return false;
    }

    /**
     * 切换不同fragment显示
     */
    private void switchTab(int index) {
        currentIndex = index;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //判断当前的Fragment是否为空，不为空则隐藏
        if (null != mCurrentFragment) {
            ft.hide(mCurrentFragment);
        }
        //先根据Tag从FragmentTransaction事物获取之前添加的Fragment
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragments.get(currentIndex).getClass().getName());
        if (null == fragment) {
            //如fragment为空，则之前未添加此Fragment。便从集合中取出
            fragment = fragments.get(index);
        }
        mCurrentFragment = fragment;
        //判断此Fragment是否已经添加到FragmentTransaction事物中
        if (!fragment.isAdded()) {
            ft.add(R.id.content, fragment, fragment.getClass().getName());
        } else {
            ft.show(fragment);
        }
        ft.commit();
        switchTitle();
    }

    /**
     * 切换不同fragment显示标题
     */
    private void switchTitle() {
        switch (currentIndex) {
            case 0:
                titleBar.setTitle(getResources().getString(R.string.title_main));
                titleBar.getActionTV().setVisibility(View.GONE);
                break;
            case 1:
                titleBar.setTitle(getResources().getString(R.string.title_product));
                titleBar.getActionTV().setVisibility(View.GONE);
                break;
            case 2:
                titleBar.setTitle(getResources().getString(R.string.title_asset));
                titleBar.getActionTV().setVisibility(View.GONE);
                break;
            case 3:
                titleBar.setTitle(getResources().getString(R.string.title_me));
                if (Env.isLoggedIn) {
                    titleBar.getActionTV().setVisibility(View.GONE);
                } else {
                    titleBar.getActionTV().setText("登录");
                    titleBar.getActionTV().setVisibility(View.VISIBLE);
                }
                break;
            default:
                titleBar.setTitle(getResources().getString(R.string.app_name));
                titleBar.getActionTV().setVisibility(View.GONE);
                break;
        }
    }

    public void jumpToTab(int id) {
        navigation.setSelectedItemId(id);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int tab = intent.getIntExtra("tab", -1);
            Logs.i("tab--" + tab);
            if (tab == 1) {
                jumpToTab(R.id.navigation_product);
            } else if (tab == 2) {
                jumpToTab(R.id.navigation_asset);
            }
        }
        setIntent(intent);
    }

    /**
     * 退出app的操作时间戳
     */
    private long exitTime = 0;

    /**
     * 重写按钮弹起事件
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(MainNavigationActivity.this,
                        getResources().getString(R.string.app_exit), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.app_title_action) {
            IntentUtils.startActivity(MainNavigationActivity.this, LoginActivity.class);
        }
    }
}
