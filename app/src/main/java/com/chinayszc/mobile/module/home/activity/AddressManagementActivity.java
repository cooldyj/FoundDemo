package com.chinayszc.mobile.module.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.AddressModel;
import com.chinayszc.mobile.module.me.activity.LoginActivity;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.widget.CommonTitleView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 地址管理Activity
 * Created by Jerry on 2017/4/1.
 */

public class AddressManagementActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private BaseAdapter listAdapter;
    private List<AddressModel> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manage_activity);
        initView();
        initListView();
    }

    private void initView() {
        addressList = new ArrayList<>();
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.address_manage_title);
        titleView.setTitle(getResources().getString(R.string.add_management));
        titleView.getBackIV().setOnClickListener(this);

        listView = (ListView) findViewById(R.id.address_manage_list_view);
        View footerView = getLayoutInflater().inflate(R.layout.common_submit_view, null);
        TextView submitTV = (TextView) footerView.findViewById(R.id.common_submit_btn);
        submitTV.setText(getResources().getString(R.string.add_new_address));
        submitTV.setOnClickListener(this);
        listView.addFooterView(footerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_back:
                onBackPressed();
                break;
            case R.id.common_submit_btn:
                Bundle bundle = new Bundle();
                bundle.putString("id", "0");
                IntentUtils.startActivity(AddressManagementActivity.this, EditAddressActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    private void loadAddList() {
        OkHttpUtils.post().url(Urls.GET_ADDRESS)
                .addCommonHeaderAndBody()
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(JSONObject jo, int id) {
                        if (jo != null) {
                            int isLogin = jo.optInt("isLogin");
                            if (isLogin == 1) {
                                String models = jo.optString("list");
                                List<AddressModel> modelList = ParseJason.convertToList(models, AddressModel.class);
                                if (null != modelList) {
                                    Logs.i("onResponse---" + modelList.size());
                                    if (!addressList.isEmpty()) {
                                        addressList.clear();
                                    }
                                    addressList.addAll(modelList);
                                    listAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(AddressManagementActivity.this, getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(AddressManagementActivity.this, LoginActivity.class);
                                AddressManagementActivity.this.finish();
                            }
                        }

                    }
                });
    }

    private void initListView() {
        listAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return addressList.size();
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
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.address_manage_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.nameTV = (TextView) convertView.findViewById(R.id.add_manage_name);
                    viewHolder.phoneTV = (TextView) convertView.findViewById(R.id.add_manage_phone);
                    viewHolder.addressTV = (TextView) convertView.findViewById(R.id.add_manage_add);
                    viewHolder.checkIcon = (ImageView) convertView.findViewById(R.id.add_manage_check_icon);
                    viewHolder.editIV = (ImageView) convertView.findViewById(R.id.add_manage_edit);
                    viewHolder.defaultTV = (TextView) convertView.findViewById(R.id.add_manage_set);
                    viewHolder.defaultLL = (LinearLayout) convertView.findViewById(R.id.add_manage_default_layout);
                    viewHolder.deleteLL = (LinearLayout) convertView.findViewById(R.id.add_manage_delete_layout);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                if (addressList.size() > position) {
                    final AddressModel model = addressList.get(position);
                    if (null != model) {
                        String addDetail = model.getDistrict() + model.getAddress();
                        viewHolder.nameTV.setText(model.getName());
                        viewHolder.phoneTV.setText(model.getPhone());
                        viewHolder.addressTV.setText(addDetail);
                        if (model.getIs_default() == 1) {
                            viewHolder.checkIcon.setImageResource(R.mipmap.checked_true);
                            viewHolder.defaultTV.setTextColor(getResources().getColor(R.color.text_bule));
                        } else {
                            viewHolder.checkIcon.setImageResource(R.mipmap.checked_false);
                            viewHolder.defaultTV.setTextColor(getResources().getColor(R.color.text_gray));
                        }
                        viewHolder.editIV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", model.getId());
                                bundle.putString("name", model.getName());
                                bundle.putString("phone", model.getPhone());
                                bundle.putString("district", model.getDistrict());
                                bundle.putString("address", model.getAddress());
                                bundle.putInt("districtId", model.getDistrict_id());
                                if (model.getIs_default() == 1) {    //（1：默认；0：普通）
                                    bundle.putBoolean("isDefault", true);
                                } else {
                                    bundle.putBoolean("isDefault", false);
                                }
                                IntentUtils.startActivity(AddressManagementActivity.this, EditAddressActivity.class, bundle);
                            }
                        });
                        viewHolder.defaultLL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                setDefaultAdd(model.getId());
                            }
                        });
                        viewHolder.deleteLL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chooseToDelet(model.getId());
                            }
                        });
                    }
                }
                return convertView;
            }
        };
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(addressList.size() > position){
                    AddressModel model = addressList.get(position);
                    if(model != null){
                        int adressId = model.getId();
                        String name = model.getName();
                        String phone = model.getPhone();
                        String distrcit = model.getDistrict();
                        String address = model.getAddress();
                        Intent intent = new Intent();
                        intent.putExtra("adressId", adressId);
                        intent.putExtra("name", name);
                        intent.putExtra("phone", phone);
                        intent.putExtra("distrcit", distrcit);
                        intent.putExtra("address", address);
                        setResult(3, intent);
                        AddressManagementActivity.this.finish();
                    }

                }
            }
        });
    }

    private class ViewHolder {
        TextView nameTV;
        TextView phoneTV;
        TextView addressTV;
        ImageView checkIcon;
        ImageView editIV;
        TextView defaultTV;
        LinearLayout defaultLL;
        LinearLayout deleteLL;
    }

    /**
     * 弹对话框，是否删除地址
     */
    private void chooseToDelet(final int id) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).
                setTitle("确定删除？").
                setMessage("您确定删除该地址吗？").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAdd(id);
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).
                create();
        alertDialog.show();
    }

    /**
     * 删除地址
     */
    private void deleteAdd(int id) {
        OkHttpUtils.post().url(Urls.DELETE_ADD)
                .addCommonHeaderAndBody()
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(AddressManagementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
                                int isDelete = response.optInt("isDelete", 0);
                                if (isDelete == 1) {
                                    Toast.makeText(AddressManagementActivity.this,
                                            AddressManagementActivity.this.getResources().getString(R.string.delete_address_success), Toast.LENGTH_SHORT).show();
                                    loadAddList();
                                } else {
                                    Toast.makeText(AddressManagementActivity.this,
                                            AddressManagementActivity.this.getResources().getString(R.string.delete_address_failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddressManagementActivity.this,
                                        AddressManagementActivity.this.getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(AddressManagementActivity.this, LoginActivity.class);
                                AddressManagementActivity.this.finish();
                            }
                        }
                    }
                });
    }

    /**
     * 设置默认地址
     */
    private void setDefaultAdd(int id) {
        OkHttpUtils.post().url(Urls.SET_DEFAULT_ADD)
                .addCommonHeaderAndBody()
                .addParams("id", String.valueOf(id))
                .build()
                .execute(new JsonObjectCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(AddressManagementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(JSONObject response, int id) {
                        if (null != response) {
                            int isLogin = response.optInt("isLogin", 0);
                            if (isLogin == 1) {
                                int isdefault = response.optInt("isdefault", 0);
                                if (isdefault == 1) {
                                    Toast.makeText(AddressManagementActivity.this,
                                            AddressManagementActivity.this.getResources().getString(R.string.set_default_success), Toast.LENGTH_SHORT).show();
                                    loadAddList();
                                } else {
                                    Toast.makeText(AddressManagementActivity.this,
                                            AddressManagementActivity.this.getResources().getString(R.string.set_default_failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddressManagementActivity.this,
                                        AddressManagementActivity.this.getResources().getString(R.string.pls_login), Toast.LENGTH_SHORT).show();
                                IntentUtils.startActivity(AddressManagementActivity.this, LoginActivity.class);
                                AddressManagementActivity.this.finish();
                            }
                        }
                    }
                });
    }

}
