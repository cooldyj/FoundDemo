package com.chinayszc.mobile.module.me.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.assets.activity.WithdrawActivity;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.me.model.MyBankCardModel;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.chinayszc.mobile.widget.CustomDialog;
import com.chinayszc.mobile.widget.wheelview.OnButtonClickedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;

/**
 * 我的银行卡Activity
 * Created by Jerry on 2017/4/4.
 */

public class MyBankCardActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout addLayout;
    private ListView bankLV;
    private BaseAdapter adapter;
    private List<MyBankCardModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bank_card_activity);
        initView();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initView() {
        modelList = new ArrayList<>();
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.my_bank_card_title);
        titleView.setTitle(getResources().getString(R.string.my_bank_card));
        titleView.getBackIV().setOnClickListener(this);

        addLayout = (FrameLayout) findViewById(R.id.my_bank_card_add_layout);
        bankLV = (ListView) findViewById(R.id.my_bank_card_recycler_view);

        addLayout.setOnClickListener(this);

//        LRecyclerView bankCardRV = (LRecyclerView) findViewById(R.id.my_bank_card_recycler_view);
//        MyBankCardListView listView = new MyBankCardListView(bankCardRV,
//                MyBankCardActivity.this);
//        bankCardRV.setLoadMoreEnabled(false);
//        bankCardRV.setPullRefreshEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.my_bank_card_add_layout:
                IntentUtils.startActivity(MyBankCardActivity.this, AddBankCardActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 获取接口数据
     */
    private void loadData() {

        OkHttpUtils.post().url(Urls.MY_BANK_CARD)
                .addCommonHeaderAndBody()
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        addLayout.setVisibility(View.VISIBLE);
                        bankLV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        Logs.i("BANK_CARD_LIST onResponse---" + response);
                        if(null != response){
                            int isLogin = response.optInt("isLogin", -1);
                            //未登录
                            if(isLogin == 0){
                                Toast.makeText(MyBankCardActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(MyBankCardActivity.this, LoginActivity.class);
                                return;
                            }

                            int isBank = response.optInt("isBank");
                            if(isBank == 1){    //已绑定银行卡
                                JSONObject bankCardJO = response.optJSONObject("bank");
                                if(bankCardJO == null){
                                    addLayout.setVisibility(View.VISIBLE);
                                    bankLV.setVisibility(View.GONE);
                                    return;
                                }
                                MyBankCardModel bankCardModel = new MyBankCardModel();
                                bankCardModel.setAccount(bankCardJO.optString("account"));
                                bankCardModel.setBankImg(bankCardJO.optString("bankImg"));
                                bankCardModel.setBankName(bankCardJO.optString("bankName"));
                                bankCardModel.setId(bankCardJO.optString("id"));
                                if(!modelList.isEmpty()){
                                    modelList.clear();
                                }
                                modelList.add(bankCardModel);
                                adapter.notifyDataSetChanged();
                                addLayout.setVisibility(View.GONE);
                                bankLV.setVisibility(View.VISIBLE);
                            } else if(isBank == 0){  //绑定中
                                showDialog("您的银行卡正在绑定中，请稍后重试");
                            } else {  //未绑定
                                addLayout.setVisibility(View.VISIBLE);
                                bankLV.setVisibility(View.GONE);
                            }

//                            JSONArray list = response.optJSONArray("list");
//                            if(list == null){
//                                addLayout.setVisibility(View.VISIBLE);
//                                bankLV.setVisibility(View.GONE);
//                                return;
//                            }
//                            List<MyBankCardModel> datas = ParseJason.convertToList(list.toString(), MyBankCardModel.class);
//                            if(null != datas){
//                                if(!modelList.isEmpty()){
//                                    modelList.clear();
//                                }
//                                modelList.addAll(datas);
//                                adapter.notifyDataSetChanged();
//                                addLayout.setVisibility(View.GONE);
//                                bankLV.setVisibility(View.VISIBLE);
//                            } else {
//                                addLayout.setVisibility(View.VISIBLE);
//                                bankLV.setVisibility(View.GONE);
//                            }

                        } else {
                            addLayout.setVisibility(View.VISIBLE);
                            bankLV.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void initListView(){
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return modelList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.my_bank_card_item, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                MyBankCardModel model = modelList.get(position);
                if (null != model) {
                    viewHolder.myBankCardName.setText(model.getBankName());
                    viewHolder.myBankCardNum.setText(model.getAccount());
                    Glide.with(MyBankCardActivity.this).load(model.getBankImg())
                            .bitmapTransform(new CropCircleTransformation(MyBankCardActivity.this))
                            .into(viewHolder.myBankCardIcon);
                }
                return convertView;
            }
        };
        bankLV.setAdapter(adapter);
    }

    private class ViewHolder {

        private ImageView myBankCardIcon;
        private TextView myBankCardName;
        private TextView myBankCardNum;

        ViewHolder(View itemView) {
            myBankCardIcon = (ImageView) itemView.findViewById(R.id.my_bank_card_icon);
            myBankCardName = (TextView) itemView.findViewById(R.id.my_bank_card_name);
            myBankCardNum = (TextView) itemView.findViewById(R.id.my_bank_card_num);
        }
    }

    private CustomDialog verifyCodeDialog;

    /**
     * 显示正在绑定中对话框
     */
    private void showDialog(String hintText){
        if(verifyCodeDialog != null && verifyCodeDialog.isShowing()){
            return;
        }
        if(verifyCodeDialog == null){
            View verifyCodeLayout = getLayoutInflater().inflate(R.layout.bank_card_dialog_view, null);
            TextView hintTV = (TextView) verifyCodeLayout.findViewById(R.id.bank_card_hint);
            hintTV.setText(hintText);
            verifyCodeDialog = new CustomDialog.Builder(MyBankCardActivity.this)
                    .setContentView(verifyCodeLayout)
                    .setPositiveBtn(getResources().getString(R.string.confirm), new OnButtonClickedListener() {
                        @Override
                        public void onButtonClicked(DialogInterface dialog, String returnMsg1, String returnMsg2) {
                            dialog.dismiss();
                            MyBankCardActivity.this.finish();
                        }
                    }).build();
        }
        verifyCodeDialog.show();
    }
}
