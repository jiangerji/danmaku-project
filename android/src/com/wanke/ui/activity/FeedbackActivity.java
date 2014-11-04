package com.wanke.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wanke.network.http.CommonHttpUtils;
import com.wanke.tv.R;
import com.wanke.ui.ToastUtil;
import com.wanke.util.PreferenceUtil;

public class FeedbackActivity extends BaseActivity {

    private EditText mFeedbackContent = null;
    private TextView mTextContent;
    private Button mSubmit;
    private MenuItem mCommit;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_feedback);

        mFeedbackContent = (EditText) findViewById(R.id.feedback_content);
        mTextContent = (TextView) findViewById(R.id.feedback_content_textview);
        mSubmit = (Button) findViewById(R.id.feedback_submit_button);
        mTextContent.getText().toString().trim();
        mSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                ToastUtil.showToast(getApplicationContext(),
                        R.string.need_data_hint);
            }
        });
        mFeedbackContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 10) {
                    if (mCommit != null) {
                        mCommit.setEnabled(true);
                    }
                } else {
                    if (mCommit != null) {
                        mCommit.setEnabled(false);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mCommit = menu.add("提交")
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mCommit.setEnabled(false);
        mCommit.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                publishFeedback();
                return true;
            }
        });
        return true;
    }

    private void publishFeedback() {
        String content = mFeedbackContent.getText().toString();

        showWaitingDialog();

        String action = "feedback";
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uid", PreferenceUtil.getUid());
        params.addQueryStringParameter("content",
                Base64.encodeToString(content.getBytes(), Base64.DEFAULT));
        CommonHttpUtils.get(action, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                dismissWaitingDialog();
                finish();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                dismissWaitingDialog();
            }
        });
    }
}
