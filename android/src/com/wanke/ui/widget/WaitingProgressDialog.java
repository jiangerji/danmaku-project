package com.wanke.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wanke.tv.R;

public class WaitingProgressDialog extends Dialog {

    public WaitingProgressDialog(Context context) {
        super(context, R.style.ProgressingDialog);

        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private String mText = null;
    private int mTextId;

    public void setText(String text) {
        mText = text;
    }

    public void setText(int textId) {
        mTextId = textId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProgressBar progressBar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleLarge);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, 0, 0);

        View contentView = progressBar;
        if (!TextUtils.isEmpty(mText) || (mTextId != 0)) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(progressBar);

            TextView textView = new TextView(getContext());
            textView.setTextColor(Color.WHITE);
            textView.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            if (!TextUtils.isEmpty(mText)) {
                textView.setText(mText);
            } else {
                textView.setText(mTextId);
            }
            linearLayout.addView(textView);

            contentView = linearLayout;
        }

        setContentView(contentView);
    }
}
