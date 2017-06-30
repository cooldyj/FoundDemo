package com.chinayszc.mobile.module.me.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.base.BaseActivity;
import com.chinayszc.mobile.module.common.Urls;
import com.chinayszc.mobile.module.home.activity.CommonH5Activity;
import com.chinayszc.mobile.module.me.model.MyBankCardModel;
import com.chinayszc.mobile.okhttp.OkHttpUtils;
import com.chinayszc.mobile.okhttp.callback.JsonObjectCallback;
import com.chinayszc.mobile.utils.IntentUtils;
import com.chinayszc.mobile.utils.Logs;
import com.chinayszc.mobile.utils.ParseJason;
import com.chinayszc.mobile.utils.SoftInputUtils;
import com.chinayszc.mobile.widget.ChooseBankDialog;
import com.chinayszc.mobile.widget.CommonTitleView;
import com.chinayszc.mobile.widget.CustomDialog;
import com.chinayszc.mobile.widget.wheelview.OnButtonClickedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 添加银行卡
 * Created by Jerry Yang on 2017/6/18.
 */

public class AddBankCardActivity extends BaseActivity implements View.OnClickListener {

	private EditText nameET;
	private EditText bankCardET;
	private EditText idET;
	private EditText phoneET;
	private TextView bankTV;
	private TextView provinceTV;
	private EditText amountET;
	private EditText siteET;
	private TextView addBankcardBtn;
	private String areaId;
	private ChooseBankDialog chooseBankDialog;
	private CustomDialog verifyCodeDialog;
	private List<MyBankCardModel> bankCardList;
	private View verifyCodeLayout;
	private EditText codeET;

	private String bankId;

	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bank_card_activity);
		initView();
		loadData();
	}

	private void initView() {
		bankCardList = new ArrayList<>();
		CommonTitleView titleView = (CommonTitleView) findViewById(R.id.add_bank_card_title);
		titleView.setTitle(getResources().getString(R.string.add_card_title));
		titleView.getBackIV().setOnClickListener(this);

		nameET = (EditText) findViewById(R.id.add_bankcard_name);
		bankCardET = (EditText) findViewById(R.id.add_bankcard_bank_card);
		idET = (EditText) findViewById(R.id.add_bankcard_id);
		phoneET = (EditText) findViewById(R.id.add_bankcard_phone);
		bankTV = (TextView) findViewById(R.id.add_bankcard_bank);
		provinceTV = (TextView) findViewById(R.id.add_bankcard_province);
		addBankcardBtn = (TextView) findViewById(R.id.add_bankcard_btn);
		TextView instruction1TV = (TextView) findViewById(R.id.add_bankcard_instruction1);
		TextView instruction2TV = (TextView) findViewById(R.id.add_bankcard_instruction2);
		TextView instruction3TV = (TextView) findViewById(R.id.add_bankcard_instruction3);
		siteET = (EditText) findViewById(R.id.add_bankcard_website);
		amountET = (EditText) findViewById(R.id.add_bankcard_amount);

		bankTV.setOnClickListener(this);
		provinceTV.setOnClickListener(this);
		addBankcardBtn.setOnClickListener(this);
		instruction1TV.setOnClickListener(this);
		instruction2TV.setOnClickListener(this);
		instruction3TV.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.app_title_back:
				onBackPressed();
				break;
			case R.id.add_bankcard_bank:
				selectBank();
				break;
			case R.id.add_bankcard_province:
				selectAdd();
				break;
			case R.id.add_bankcard_btn:
				attemptNext();
				break;
			case R.id.add_bankcard_instruction1:
				Bundle b2 = new Bundle();
				b2.putString("url", "http://m.azhongzhi.com/client/info/addcardinfo.htm");
				b2.putString("title", "个人存管账户开户说明");
				IntentUtils.startActivity(AddBankCardActivity.this, CommonH5Activity.class, b2);
				break;
			case R.id.add_bankcard_instruction2:
				Bundle b3 = new Bundle();
				b3.putString("url", "http://m.azhongzhi.com/client/info/moneyinpromise.htm");
				b3.putString("title", "资金来源合法承诺书");
				IntentUtils.startActivity(AddBankCardActivity.this, CommonH5Activity.class, b3);
				break;
			case R.id.add_bankcard_instruction3:
				Bundle b4 = new Bundle();
				b4.putString("url", "http://m.azhongzhi.com/client/info/addcardformoney.htm");
				b4.putString("title", "关于支付金额的说明");
				IntentUtils.startActivity(AddBankCardActivity.this, CommonH5Activity.class, b4);
				break;
			default:
				break;
		}
	}

	/**
	 * 获取银行列表数据
	 */
	private void loadData() {
		OkHttpUtils.post().url(Urls.BANK_CARD_LIST)
				.addCommonHeaderAndBody()
				.build()
				.execute(new JsonObjectCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Logs.i("BANK_CARD_LIST onError---" + e);
						initListView();
					}

					@Override
					public void onResponse(JSONObject response, int id) {
						Logs.i("BANK_CARD_LIST onResponse---" + response);
						if(null != response){
							JSONArray list = response.optJSONArray("list");
							List<MyBankCardModel> datas = ParseJason.convertToList(list.toString(), MyBankCardModel.class);
							if(null != datas){
								if(!bankCardList.isEmpty()){
									bankCardList.clear();
								}
								MyBankCardModel model0 = new MyBankCardModel();
								MyBankCardModel model1 = new MyBankCardModel();
								bankCardList.add(model0);
								bankCardList.addAll(datas);
								bankCardList.add(model1);
							}
						}
						initListView();
					}
				});

	}

	/**
	 * 进入下一步
	 */
	private void goNext() {
		OkHttpUtils.post().url(Urls.BANK_CARD_SETP1)
				.addCommonHeaderAndBody()
				.addParams("name", nameET.getText().toString())
				.addParams("areaId", areaId)
				.addParams("bankId", bankId)
				.addParams("bankBranchName", siteET.getText().toString())
				.addParams("bankAcctNum", bankCardET.getText().toString())
				.addParams("certNo", idET.getText().toString())
				.addParams("money", amountET.getText().toString())
				.addParams("mobile", phoneET.getText().toString())
				.build()
				.execute(new JsonObjectCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Toast.makeText(AddBankCardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onResponse(JSONObject response, int id) {
						Logs.i("BANK_CARD_SETP1 onResponse---" + response);
						showVerifyCodeDialog();
					}
				});
	}

	/**
	 * 显示验证码输入框
	 */
	private void showVerifyCodeDialog() {
		if(verifyCodeDialog == null){
			verifyCodeLayout = getLayoutInflater().inflate(R.layout.verify_code_view, null);
			codeET = (EditText) verifyCodeLayout.findViewById(R.id.add_bankcard_name);
			verifyCodeDialog = new CustomDialog.Builder(AddBankCardActivity.this)
					.setContentView(verifyCodeLayout)
					.setNegativeBtn(getResources().getString(R.string.cancel), new OnButtonClickedListener() {
						@Override
						public void onButtonClicked(DialogInterface dialog, String returnMsg1, String returnMsg2) {
							dialog.dismiss();
						}
					})
					.setPositiveBtn(getResources().getString(R.string.confirm), new OnButtonClickedListener() {
						@Override
						public void onButtonClicked(DialogInterface dialog, String returnMsg1, String returnMsg2) {
							if(TextUtils.isEmpty(codeET.getText().toString())){
								Toast.makeText(AddBankCardActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
							} else {
								goStep2();
							}

						}
					}).build();
		}
		verifyCodeDialog.show();
	}

	/**
	 * 进入第二步
	 */
	private void goStep2() {
		OkHttpUtils.post().url(Urls.BANK_CARD_SETP2)
				.addCommonHeaderAndBody()
				.addParams("smsCode", codeET.getText().toString())
				.build()
				.execute(new JsonObjectCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Toast.makeText(AddBankCardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onResponse(JSONObject response, int id) {
						Logs.i("BANK_CARD_SETP2 onResponse---" + response);
						Toast.makeText(AddBankCardActivity.this, "绑定银行卡成功", Toast.LENGTH_LONG).show();
						AddBankCardActivity.this.finish();
					}
				});
	}

	/**
	 * 选择银行
	 */
	private void selectBank() {
		if(chooseBankDialog == null){
			chooseBankDialog = new ChooseBankDialog.Builder(AddBankCardActivity.this)
					.setContentView(listLayout)
					.setNegativeBtn(getResources().getString(R.string.cancel), new OnButtonClickedListener() {
						@Override
						public void onButtonClicked(DialogInterface dialog, String returnMsg1, String returnMsg2) {
							dialog.dismiss();
						}
					})
					.setPositiveBtn(getResources().getString(R.string.confirm), new OnButtonClickedListener() {
						@Override
						public void onButtonClicked(DialogInterface dialog, String returnMsg1, String returnMsg2) {
							if(bankCardList != null && bankCardList.size() > selectedItem){
								if(selectedItem == -1 && bankCardList.size() > 2){ //如果没选择，则默认是第一个
									selectedItem = 1;
								}
								MyBankCardModel model = bankCardList.get(selectedItem);
								if(model != null){
									bankId = model.getId();
									bankTV.setText(model.getBankName());
								}
							}
							dialog.dismiss();
						}
					}).build();
		}
		chooseBankDialog.show();
	}

	/**
	 * 选择地址
	 */
	private void selectAdd() {
		Intent intent = new Intent();
		intent.setClass(AddBankCardActivity.this, ChooseProvinceActivity.class);
		startActivityForResult(intent, 4);
	}

	private View listLayout;
	private ListView listView;
	private int firstVisibleItem;
	private int selectedItem = -1;

	/**
	 * 初始化银行滚轮view
	 */
	private void initListView() {
		BaseAdapter listAdapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return bankCardList.size();
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
					convertView = LayoutInflater.from(AddBankCardActivity.this).inflate(R.layout.bank_item_view, parent, false);
					viewHolder = new ViewHolder(convertView);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				if (bankCardList != null && bankCardList.size() > position) {
					Glide.with(AddBankCardActivity.this).load(bankCardList.get(position).getBankImg())
							.into(viewHolder.myBankCardIcon);
				}
				return convertView;
			}
		};


		listLayout = getLayoutInflater().inflate(R.layout.bank_list_layout, null);
		listView = (ListView) listLayout.findViewById(R.id.bank_list_view);
		listView.setAdapter(listAdapter);

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == SCROLL_STATE_IDLE){
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							listView.setSelection(firstVisibleItem);
							selectedItem = firstVisibleItem + 1;
						}
					}, 300);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				AddBankCardActivity.this.firstVisibleItem = firstVisibleItem;
			}
		});
	}

	class ViewHolder{
		ImageView myBankCardIcon;
		ViewHolder(View itemView) {
			myBankCardIcon = (ImageView) itemView.findViewById(R.id.bank_item_img);
		}
	}

	/**
	 * 校验输入格式
	 */
	private void attemptNext() {
		SoftInputUtils.closedSoftInput(AddBankCardActivity.this);

		nameET.setError(null);
		bankCardET.setError(null);
		idET.setError(null);
		phoneET.setError(null);
		siteET.setError(null);
		amountET.setError(null);

		boolean cancel = false;
		View focusView = null;

		String nameETStr = nameET.getText().toString();
		String bankCardETStr = bankCardET.getText().toString();
		String idETStr = idET.getText().toString();
		String phoneETStr = phoneET.getText().toString();
		String siteETStr = siteET.getText().toString();
		String amountETStr = amountET.getText().toString();

		if (TextUtils.isEmpty(amountETStr)) {
			amountET.setError("支付金额不能为空");
			focusView = amountET;
			cancel = true;
		}

		if (TextUtils.isEmpty(siteETStr)) {
			siteET.setError("网点不能为空");
			focusView = siteET;
			cancel = true;
		}

		if (TextUtils.isEmpty(phoneETStr)) {
			phoneET.setError(getString(R.string.phone_num_not_null));
			focusView = phoneET;
			cancel = true;
		} else if (phoneET.length() < 11) {
			phoneET.setError(getString(R.string.phone_num_should_correct));
			focusView = phoneET;
			cancel = true;
		}

		if (TextUtils.isEmpty(idETStr)) {
			idET.setError("身份证不能为空");
			focusView = idET;
			cancel = true;
		}

		if (TextUtils.isEmpty(bankCardETStr)) {
			bankCardET.setError("银行卡不能为空");
			focusView = bankCardET;
			cancel = true;
		}

		if (TextUtils.isEmpty(nameETStr)) {
			nameET.setError("姓名不能为空");
			focusView = nameET;
			cancel = true;
		}

		if (TextUtils.isEmpty(bankId)) {
			Toast.makeText(this, "银行不能为空", Toast.LENGTH_LONG).show();
			focusView = null;
			cancel = true;
		}

		if (TextUtils.isEmpty(areaId)) {
			Toast.makeText(this, "省市不能为空", Toast.LENGTH_LONG).show();
			focusView = null;
			cancel = true;
		}

		if (cancel) {
			if(focusView != null){
				focusView.requestFocus();
			}
		} else {
			goNext();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 4 && resultCode == 5) {
			if (data != null) {
				areaId = data.getStringExtra("cityId");
				String cityName = data.getStringExtra("cityName");
				String provinceName = data.getStringExtra("provinceName");
				String address = provinceName + cityName;
				provinceTV.setText(address);
			}
		}
	}
}
