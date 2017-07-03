package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lede.second_23.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutWeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_we);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.iv_about_activity_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_about_activity_back:
                finish();
                break;
        }
    }
}
