package com.chinayszc.mobile.module.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.model.AreaModel;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.StringArrayCallback;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.widget.CommonTitleView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 选择区Activity
 * Created by Jerry on 2017/4/1.
 */

public class SelectDistrictActivity extends BaseActivity implements View.OnClickListener {

    private ListView listView;
    private BaseAdapter listAdapter;
    private List<AreaModel> areaList;
    private String areaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_district_activity);
        getData();
        initView();
        initListView();
        getArea();
    }

    private void getData(){
        Intent intent = getIntent();
        areaId = intent.getStringExtra("areaId");
    }

    private void initView(){
        areaList = new ArrayList<>();
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.select_district_title);
        titleView.setTitle(getResources().getString(R.string.select_district));
        titleView.getBackIV().setOnClickListener(this);

        listView = (ListView) findViewById(R.id.select_district_list_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_title_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    private void initListView() {
        listAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return areaList.size();
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
                    convertView = getLayoutInflater().inflate(R.layout.area_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.areaTV = (TextView) convertView.findViewById(R.id.area_item_name);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                if (areaList.size() > position) {
                    final AreaModel model = areaList.get(position);
                    if (null != model) {
                        viewHolder.areaTV.setText(model.getDistrict());
                    }
                }
                return convertView;
            }
        };
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(areaList.size() > position){
                    Intent intent = new Intent();
                    intent.putExtra("districtId", String.valueOf(areaList.get(position).getId()));
                    intent.putExtra("districtName", areaList.get(position).getDistrict());
                    setResult(9, intent);
                    SelectDistrictActivity.this.finish();
                }
            }
        });
    }

    private class ViewHolder {
        TextView areaTV;
    }

    private void getArea() {
        OkHttpUtils.post().url(Urls.GET_AREA)
                .addCommonHeaderAndBody()
                .addParams("type", "3")
                .addParams("areaId", areaId)
                .build()
                .execute(new StringArrayCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logs.i("onError---" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (null != response) {
                            List<AreaModel> modelList = ParseJason.convertToList(response, AreaModel.class);
                            if (null != modelList) {
                                if (!areaList.isEmpty()) {
                                    areaList.clear();
                                }
                                areaList.addAll(modelList);
                                listAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

}
