package com.lede.second_23.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lede.second_23.R;
import com.lede.second_23.global.GlobalConstants;
import com.lede.second_23.utils.SPUtils;
import com.lede.second_23.utils.glide.GlideCatchUtil;

import org.ielse.widget.RangeSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends AppCompatActivity {

    @Bind(R.id.tv_set_activity_age)
    TextView tv_age;
    @Bind(R.id.rsb_set_activity_age)
    RangeSeekBar rsb_age;
    @Bind(R.id.tv_set_activity_save)
    TextView tv_save;
    @Bind(R.id.sb_set_activity_distance)
    SeekBar sb_distance;
    @Bind(R.id.rl_set_activity_sex)
    RelativeLayout rl_sex;
    @Bind(R.id.tv_set_activity_sex)
    TextView tv_sex;
    @Bind(R.id.tv_set_activity_distance)
    TextView tv_distance;
    @Bind(R.id.tv_set_activity_catchsize)
    TextView tv_catchSize;
    private Context mContext;
    private float minAge=0;
    private float maxAge=41;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        mContext=this;
        initController();
        initData();
    }

    private void initController() {
        rsb_age.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max) {
                minAge=min;
                maxAge=max;
                if ((int)max==41) {
                    tv_age.setText((int)min+"-40+");
                }else {
                    tv_age.setText((int)min+"-"+(int)max);
                }
            }
        });
        sb_distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv_distance.setText(i+1+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initData() {
        tv_catchSize.setText(GlideCatchUtil.getInstance().getCacheSize());
        if (SPUtils.contains(mContext, GlobalConstants.SET_SEX)) {
            tv_sex.setText((String)SPUtils.get(mContext,GlobalConstants.SET_SEX,""));
        }else {
            tv_sex.setText("全部人");
        }
        if (SPUtils.contains(mContext, GlobalConstants.SET_MINAGE)) {
            rsb_age.setValue((float)SPUtils.get(mContext,GlobalConstants.SET_MINAGE,0.0f),(float)SPUtils.get(mContext,GlobalConstants.SET_MAXAGE,41.0f));
            minAge=(float)SPUtils.get(mContext,GlobalConstants.SET_MINAGE,0.0f);
            maxAge=(float)SPUtils.get(mContext,GlobalConstants.SET_MAXAGE,41.0f);
            if ((int)maxAge==41) {
                tv_age.setText((int)minAge+"-40+");
            }else {
                tv_age.setText((int)minAge+"-"+(int)maxAge);
            }
        }else {
            rsb_age.setValue(0.0f,41.0f);
        }
        if (SPUtils.contains(mContext, GlobalConstants.SET_DISTANCE)) {
            sb_distance.setProgress((int)SPUtils.get(mContext,GlobalConstants.SET_DISTANCE,9)-1);
        }else {
            sb_distance.setProgress(9);
        }
    }
    @OnClick({R.id.rl_set_activity_sex,R.id.tv_set_activity_save,R.id.ll_set_activity_exit
            ,R.id.ll_set_activity_clear,R.id.ll_set_activity_about,R.id.iv_set_activity_back})
    public void onclick(View view){
        switch (view.getId()) {
            case R.id.rl_set_activity_sex:
                showDialog();
                break;
            case R.id.tv_set_activity_save:
                SPUtils.put(mContext,GlobalConstants.SET_SEX,tv_sex.getText().toString());
                SPUtils.put(mContext,GlobalConstants.SET_DISTANCE,Integer.parseInt(tv_distance.getText().toString()));
                SPUtils.put(mContext,GlobalConstants.SET_MINAGE,minAge);
                SPUtils.put(mContext,GlobalConstants.SET_MAXAGE,maxAge);
                finish();
                break;
            case R.id.ll_set_activity_exit:
                /**
                 * //设置中筛选条件性别
                 public static final String SET_SEX="SET_SEX";
                 //设置中筛选条件最小年龄
                 public static final String SET_MINAGE="SET_MINAGE";
                 //设置中筛选条件最大年龄
                 public static final String SET_MAXAGE="SET_MAXAGE";
                 //设置中筛选的距离
                 public static final String SET_DISTANCE="SET_DISTANCE";
                 */
//                SPUtils.remove(this,GlobalConstants.TOKEN);
//                SPUtils.remove(this,GlobalConstants.USERID);
//                SPUtils.remove(this,GlobalConstants.HEAD_IMG);
//                SPUtils.remove(this,GlobalConstants.SET_SEX);
//                SPUtils.remove(this,GlobalConstants.SET_MINAGE);
//                SPUtils.remove(this,GlobalConstants.SET_MAXAGE);
//                SPUtils.remove(this,GlobalConstants.SET_DISTANCE);
                SPUtils.clear(this);
                startActivity(new Intent(this,LoginActivity.class));
//                MainActivity.instance.onDestroy();
                MainActivity.instance.finish();
                finish();
                break;
            case R.id.ll_set_activity_clear:
                GlideCatchUtil.getInstance().clearCacheDiskSelf();
                tv_catchSize.setText(GlideCatchUtil.getInstance().getCacheSize());
                break;
            case R.id.ll_set_activity_about:
                startActivity(new Intent(this,AboutWeActivity.class));
                break;
            case R.id.iv_set_activity_back:
                finish();
                break;
        }
    }

    private void showDialog(){
        final String items[]={"全部人","男","女"};
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("选择性别"); //设置标题
        builder.setIcon(R.mipmap.logo);//设置图标，图片id即可
        //设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(mContext, items[which], Toast.LENGTH_SHORT).show();
                tv_sex.setText(items[which]);
            }
        });
//        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Toast.makeText(mContext, "确定", Toast.LENGTH_SHORT).show();
//            }
//        });
        builder.create().show();
    }

}

