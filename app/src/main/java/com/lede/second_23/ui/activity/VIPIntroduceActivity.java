package com.lede.second_23.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.lede.second_23.R;
import com.lede.second_23.ui.base.BaseActivity;

/**
 * Created by ld on 17/11/23.
 */

public class VIPIntroduceActivity  extends BaseActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_vip_intro);

        View view=findViewById(R.id.back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
