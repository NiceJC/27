package com.lede.second_23.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lede.second_23.R;
import com.lede.second_23.interface_utils.OnTrimVideoListener;
import com.lede.second_23.ui.view.VideoTrimmerView;
import com.lede.second_23.utils.TrimVideoUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener {

    @Bind(R.id.trimmer_view)
    VideoTrimmerView trimmerView;
    private String path;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);
        ButterKnife.bind(this);
        intent = getIntent();
        path=intent.getStringExtra("path");
        initView();
    }

    private void initView() {
        trimmerView.setMaxDuration(TrimVideoUtil.VIDEO_MAX_DURATION);
        trimmerView.setOnTrimVideoListener(this);
        trimmerView.setVideoURI(Uri.parse(path));
    }

    @Override
    public void onPause() {
        super.onPause();
        trimmerView.onPause();
        trimmerView.setRestoreState(true);
    }

    @Override
    public void onStartTrim() {

    }

    @Override
    public void onFinishTrim(Uri uri) {
        Intent intent=new Intent();
        intent.putExtra("path",uri.getPath());
        Log.i("onFinishTrim", "onFinishTrim: "+uri.getPath());
        setResult(3333,intent);
        finish();
    }

    @Override
    public void onCancel() {
        trimmerView.destroy();
        finish();
    }
}
