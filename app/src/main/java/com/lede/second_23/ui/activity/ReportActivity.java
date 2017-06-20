package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lede.second_23.R;
import com.lede.second_23.bean.ReportBean;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.L;
import com.lede.second_23.utils.SPUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends AppCompatActivity {

    @Bind(R.id.et_report_activity_text)
    EditText et_text;
    @Bind(R.id.tv_report_activity_num)
    TextView tv_num;
    @Bind(R.id.rg_report_activity_type)
    RadioGroup rg_type;
    private String[] strs={"色情","暴力","广告","欺诈","违法","头像","用户名","其他"};
    private int current;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        //获取服务器队列
        requestQueue = GlobalConstants.getRequestQueue();

        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                current=i;
            }
        });
        rg_type.check(rg_type.getChildAt(7).getId());
        et_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_num.setText(charSequence.toString().length()+"/500");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick({R.id.iv_report_activity_cancel,R.id.tv_report_activity_commit})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_report_activity_cancel:
                finish();
                break;
            case R.id.tv_report_activity_commit:

                userReport();
                break;
        }
    }

    private void userReport() {
        Request<String> userReport = NoHttp.createStringRequest(GlobalConstants.URL + "/homes/userReport", RequestMethod.POST);
        userReport.add("userId",(String) SPUtils.get(this,GlobalConstants.CURRENT_USERID,""));
        userReport.add("forumId",(String) SPUtils.get(this,GlobalConstants.CURRENT_FORUMID,""));
        userReport.add("reportTyep",strs[current-1]);
        userReport.add("reportText",et_text.getText().toString().trim());
        requestQueue.add(100, userReport, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                L.i("TAB",response.get());
                parseJson(response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Toast.makeText(ReportActivity.this, "网络出错,请检查网络后,重新举报", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private void parseJson(String json) {
        Gson mGson=new Gson();
        ReportBean reportBean=mGson.fromJson(json,ReportBean.class);
        if (reportBean.getResult()==10000) {
            Toast.makeText(this, "举报成功", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, "网络出错,请检查网络后,重新举报", Toast.LENGTH_SHORT).show();
        }
    }
}
