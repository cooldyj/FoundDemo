package com.chinayszc.mobile.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinayszc.mobile.widget.wheelview.AbstractWheelTextAdapter;
import com.chinayszc.mobile.widget.wheelview.OnWheelScrollListener;
import com.chinayszc.mobile.widget.wheelview.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 自定义时间选择Dialog
 * Created by Jerry on 2016/12/12.
 */

public class ChooseTimeDialog extends CustomDialog {

    public ChooseTimeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

//    public static class MyBuilder extends Builder {
//        View chooseWheelView;
//
//        private ArrayList<String> hourArray = new ArrayList<>();
//        private int nowHourId = 0;  //系统当前小时数
//        private MyWheelTextAdapter mHourAdapter;
//
//        //常量
//        private final int MAX_TEXT_SIZE = 26;
//        private final int MIN_TEXT_SIZE = 22;
//
//        private String mHourStr;    //选择的小时数
//
//        private Context context;
//        private String currentTime;
//
//        public MyBuilder(Context context) {
//            super(context);
//            chooseWheelView = LayoutInflater.from(context).inflate(R.layout.leader_choose_time_view, null);
//            this.context = context;
//        }
//
//        public MyBuilder setView1() {
//            setHourWheelView(chooseWheelView);
//            setContentView(chooseWheelView);
//            return this;
//        }
//
//        /**
//         * 设置选择小时View
//         */
//        private void setHourWheelView(View view) {
//            WheelView hoursView = (WheelView) view.findViewById(R.id.choose_time_hours);
//            initHour();
//            mHourAdapter = new MyWheelTextAdapter(context, hourArray, nowHourId, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
//            hoursView.setVisibleItems(5);
//            hoursView.setViewAdapter(mHourAdapter);
//            hoursView.setCurrentItem(nowHourId);
//            mHourStr = hourArray.get(nowHourId) + "";
//            setTextViewStyle(mHourStr, mHourAdapter);
//            setReturnMsg(mHourStr, mMinuteStr1);
//
//            hoursView.addScrollingListener(new OnWheelScrollListener() {
//                @Override
//                public void onScrollingStarted(WheelView wheel) {
//
//                }
//
//                @Override
//                public void onScrollingFinished(WheelView wheel) {
//                    String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
//                    setTextViewStyle(currentText, mHourAdapter);
//                    mHourStr = hourArray.get(wheel.getCurrentItem()) + "";
//                    setReturnMsg(mHourStr, mMinuteStr1);
//                }
//            });
//        }
//
//        /**
//         * 初始化小时
//         */
//        private void initHour() {
//            Calendar nowCalendar = Calendar.getInstance();
//            int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
//            hourArray.clear();
//            for (int i = 0; i <= 23; i++) {
//                hourArray.add(i + "");
//                if (nowHour == i) {
//                    nowHourId = hourArray.size() - 1;
//                }
//            }
//        }
//
//        /**
//         * 设置文字的大小
//         */
//        private void setTextViewStyle(String currentItemText, MyWheelTextAdapter adapter) {
//            ArrayList<View> arrayList = adapter.getTextViewArray();
//            int size = arrayList.size();
//            String currentText;
//            for (int i = 0; i < size; i++) {
//                TextView textView = (TextView) arrayList.get(i);
//                currentText = textView.getText().toString();
//                if (currentItemText.equals(currentText)) {
//                    textView.setTextSize(MAX_TEXT_SIZE);
//                } else {
//                    textView.setTextSize(MIN_TEXT_SIZE);
//                }
//            }
//        }
//
//        /**
//         * 时间选择器Adapter
//         */
//        private class MyWheelTextAdapter extends AbstractWheelTextAdapter {
//            ArrayList<String> list;
//
//            MyWheelTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
//                super(context, R.layout.leader_chosse_time_text_item, R.id.temp_value, currentItem, maxsize, minsize);
//                this.list = list;
//            }
//
//            @Override
//            public View getItem(int index, View convertView, ViewGroup parent) {
//                return super.getItem(index, convertView, parent);
//            }
//
//            @Override
//            protected CharSequence getItemText(int index) {
//                return list.get(index) + "";
//            }
//
//            @Override
//            public int getItemsCount() {
//                return list.size();
//            }
//        }
//    }


}
