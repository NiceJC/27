package com.lede.second_23.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lede.second_23.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginOrRegisterActivity extends AppCompatActivity {

    @Bind(R.id.iv_login_or_register_pic01)
    ImageView ivLoginOrRegisterPic01;
    @Bind(R.id.iv_login_or_register_pic02)
    ImageView ivLoginOrRegisterPic02;
    @Bind(R.id.iv_login_or_register_pic03)
    ImageView ivLoginOrRegisterPic03;
    @Bind(R.id.tv_login_or_register_login)
    TextView tvLoginOrRegisterLogin;
    @Bind(R.id.tv_login_or_register_register)
    TextView tvLoginOrRegisterRegister;
    private float curTranslationX;
    private float curTranslationY;
    private ObjectAnimator pic01MoveX;
    private ObjectAnimator pic01MoveY;
    private ObjectAnimator pic02MoveX;
    private ObjectAnimator pic02MoveY;
    private ObjectAnimator pic03MoveX;
    private ObjectAnimator pic03MoveY;
    private AnimatorSet animatorSet01;
    private AnimatorSet animatorSet02;
    private AnimatorSet animatorSet03;
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    animatorSet01.start();
                    break;
                case 2:
                    animatorSet02.start();
                    break;
                case 3:
                    animatorSet03.start();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private ObjectAnimator pic01rotation;
    private ObjectAnimator pic02rotation;
    private ObjectAnimator pic03rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        ButterKnife.bind(this);
        initAnimation();
    }

    private void initAnimation() {
        curTranslationX = ivLoginOrRegisterPic01.getTranslationX();
        curTranslationY = ivLoginOrRegisterPic01.getTranslationY();
        pic01MoveX = ObjectAnimator.ofFloat(ivLoginOrRegisterPic01, "translationX", curTranslationX, -1500f, 1500f, curTranslationX);
        pic01MoveY = ObjectAnimator.ofFloat(ivLoginOrRegisterPic01, "translationY", curTranslationY, 2000f, curTranslationY);
        pic01rotation = ObjectAnimator.ofFloat(ivLoginOrRegisterPic01, "rotation", 15f, -345f);
        pic02MoveX = ObjectAnimator.ofFloat(ivLoginOrRegisterPic02, "translationX", curTranslationX, -1500f, 1500f, curTranslationX);
        pic02MoveY = ObjectAnimator.ofFloat(ivLoginOrRegisterPic02, "translationY", curTranslationY, 2000f, curTranslationY);
        pic02rotation = ObjectAnimator.ofFloat(ivLoginOrRegisterPic02, "rotation", 7.5f, -352.5f);
        pic03MoveX = ObjectAnimator.ofFloat(ivLoginOrRegisterPic03, "translationX", curTranslationX, -1500f, 1500f, curTranslationX);
        pic03MoveY = ObjectAnimator.ofFloat(ivLoginOrRegisterPic03, "translationY", curTranslationY, 2000f, curTranslationY);
        pic03rotation = ObjectAnimator.ofFloat(ivLoginOrRegisterPic03, "rotation", 0f,-360f);


        animatorSet01 = new AnimatorSet();
        animatorSet02 = new AnimatorSet();
        animatorSet02.play(pic02MoveX).with(pic02MoveY).with(pic02rotation);
        animatorSet03 = new AnimatorSet();
        animatorSet03.play(pic01MoveX).with(pic01MoveY).with(pic01rotation);
        animatorSet01.play(pic03MoveX).with(pic03MoveY).with(pic03rotation);
        animatorSet02.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Message msg=new Message();
                msg.what=3;
                mHandler.sendMessageDelayed(msg,1000);
            }
        });
        animatorSet03.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet01.start();
                Message msg=new Message();
                msg.what=1;
                mHandler.sendMessage(msg);

            }
        });
        animatorSet01.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Message msg=new Message();
                msg.what=2;
                mHandler.sendMessageDelayed(msg,1000);
            }


        });
        animatorSet02.setDuration(3000);
        animatorSet03.setDuration(3000);
        animatorSet01.setDuration(3000);
        animatorSet01.start();
    }

    @OnClick({R.id.tv_login_or_register_login,R.id.tv_login_or_register_register})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.tv_login_or_register_login:
                startActivity(new Intent(this,LoginAndRegisterActivity.class));
                finish();
                break;
            case R.id.tv_login_or_register_register:
                startActivity(new Intent(this,RegisterActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animatorSet01!=null) {
            animatorSet01.cancel();
        }
        if (animatorSet02!=null) {
            animatorSet02.cancel();
        }
        if (animatorSet03!=null) {
            animatorSet03.cancel();
        }
        if (mHandler!=null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
