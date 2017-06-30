package com.chinayszc.mobile.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinayszc.mobile.R;
import com.chinayszc.mobile.module.common.Env;
import com.chinayszc.mobile.utils.DisplayUtils;
import com.chinayszc.mobile.widget.wheelview.OnButtonClickedListener;

/**
 * 选择银行Dialog
 *
 * <p>
 *    CustomDialog.Buidler builder = new CustomDialog.Buidler(context);
 *    builder.setTitle("title").setMessage("message").create().show();
 * </p>
 * Created by jerry on 2016/7/29.
 */
public class ChooseBankDialog extends Dialog {

    public ChooseBankDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void resizeDialog(Dialog dialog){
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lps = window.getAttributes();
        lps.width = Env.screenWidth;
        lps.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lps);
    }

    @Override
    public void show() {
        resizeDialog(this);
        super.show();
    }

    public static class Builder {
        private Context context;
        private String negativeBtnText;    //取消按钮文字
        private String positiveBtnText;    //确认按钮文字
        private View contentView;          //对话框内容View
        private OnButtonClickedListener onNegativeClick;  //取消按钮回调
        private OnButtonClickedListener onPositiveClick;  //确认按钮回调
        private String returnMsg1 = "";   //回调内容
        private String returnMsg2 = "";   //回调内容

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setNegativeBtn(String negativeBtnText, OnButtonClickedListener onNegativeClick) {
            this.negativeBtnText = negativeBtnText;
            this.onNegativeClick = onNegativeClick;
            return this;
        }

        public Builder setPositiveBtn(String positiveBtnText, OnButtonClickedListener onPositiveClick) {
            this.positiveBtnText = positiveBtnText;
            this.onPositiveClick = onPositiveClick;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        private void buildDialog(final ChooseBankDialog dialog) {
            View view = LayoutInflater.from(context).inflate(R.layout.choose_bank_dialog, null);
            LinearLayout contentLL = (LinearLayout) view.findViewById(R.id.bank_dialog_content);
            TextView negativeTV = (TextView) view.findViewById(R.id.bank_dialog_cancel);
            TextView positiveTV = (TextView) view.findViewById(R.id.bank_dialog_save);

            if (TextUtils.isEmpty(negativeBtnText)) {
                negativeTV.setVisibility(View.GONE);
            } else {
                negativeTV.setText(negativeBtnText);
                negativeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onNegativeClick != null){
                            onNegativeClick.onButtonClicked(dialog, returnMsg1, returnMsg2);
                        }else {
                            dialog.dismiss();
                        }
                    }
                });
            }
            if (TextUtils.isEmpty(positiveBtnText)) {
                positiveTV.setVisibility(View.GONE);
            } else {
                positiveTV.setText(positiveBtnText);
                positiveTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onPositiveClick != null){
                            onPositiveClick.onButtonClicked(dialog, returnMsg1, returnMsg2);
                        }else {
                            dialog.dismiss();
                        }
                    }
                });
            }
            if(contentView != null){
                contentLL.removeAllViews();
                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                contentLL.addView(contentView, lps);
            }
            dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
        }

        public void setReturnMsg(String returnMsg1, String returnMsg2) {
            this.returnMsg1 = returnMsg1;
            this.returnMsg2 = returnMsg2;
        }

        public ChooseBankDialog build(){
            ChooseBankDialog dialog = new ChooseBankDialog(context, R.style.ChooseBankDialog);
            buildDialog(dialog);
            return dialog;
        }
    }


}
