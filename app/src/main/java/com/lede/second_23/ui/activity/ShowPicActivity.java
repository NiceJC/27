package com.lede.second_23.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lede.second_23.AppConstant;
import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



//拍照所得照片展示页面
public class ShowPicActivity extends Activity {

    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.tv_showpic_activity_cancel)
    TextView tvShowpicActivityCancel;
    @Bind(R.id.tv_showpic_activity_next)
    TextView tvShowpicActivityNext;
    private Context context;
    private int picWidth;
    private int picHeight;
    private String img_path;
    private Intent intent;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);
        ButterKnife.bind(this);
        context = this;
        intent = getIntent();
        picWidth = intent.getIntExtra(AppConstant.KEY.PIC_WIDTH, 0);
        picHeight = intent.getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0);
        img_path = intent.getStringExtra(AppConstant.KEY.IMG_PATH);
        type = intent.getIntExtra("type", 0);
        img.setImageURI(Uri.parse(img_path));
        img.setLayoutParams(new RelativeLayout.LayoutParams(picWidth, picHeight));
    }

    @OnClick({R.id.tv_showpic_activity_cancel, R.id.tv_showpic_activity_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showpic_activity_cancel:
                finish();
                break;
            case R.id.tv_showpic_activity_next:
                Intent intent;
                if (type == 0) {

                    intent = new Intent(this, IssueActivity.class);
                    intent.putExtra("imgOrVideoType", 0);
                    intent.putExtra("img_path", img_path);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.add);
                    builder.setTitle("选择操作");
                    //    指定下拉列表的显示数据
                    final String[] cities = {"图层样式", "九宫格样式"};
                    //    设置一个下拉的列表选择项
                    builder.setItems(cities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            jump(which);

                        }
                    });
                    builder.show();


                }


                break;
        }
    }

    private void jump(int which) {
        Intent intent = new Intent(context, AllIssueTextActivity.class);
        if (which == 0) {
            intent.putExtra("isCrop", true);
        } else {
            intent.putExtra("isCrop", false);
        }
        intent.putExtra("imgOrVideoType", 0);
        intent.putExtra("img_path", img_path);
        startActivity(intent);
        finish();

    }
}
