package com.lede.second_23.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.lede.second_23.R;

/**
 * Created by ld on 17/11/10.
 */

public class SnackBarUtil {

    public static Snackbar getInstance(View view ,String text){
        Snackbar instance;
        instance=Snackbar.make(view,text,Snackbar.LENGTH_SHORT);
        View snackbarView = instance.getView();//获取Snackbar的view

        if(snackbarView!=null){
            snackbarView.setBackgroundColor(Color.parseColor("#8080FF"));//修改view的背景色
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#ffffff"));//获取Snackbar的message控件，修改字体颜色
        }
        return  instance;
    }
    public static Snackbar getInstance(View view , Context context,int resId){
        Snackbar instance;
        instance=Snackbar.make(view,context.getResources().getText(resId),Snackbar.LENGTH_SHORT);
        View snackbarView = instance.getView();//获取Snackbar的view
        if(snackbarView!=null){
            snackbarView.setBackgroundColor(Color.parseColor("#8080FF"));//修改view的背景色

            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#ffffff"));//获取Snackbar的message控件，修改字体颜色
        }
        return  instance;
    }
    public static Snackbar getLongTimeInstance(View view ,String text){
        final Snackbar instance;
        instance=Snackbar.make(view,text,Snackbar.LENGTH_INDEFINITE);
        instance.setAction("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.dismiss();
            }
        });
        View snackbarView = instance.getView();//获取Snackbar的view

        if(snackbarView!=null){
            snackbarView.setBackgroundColor(Color.parseColor("#8080FF"));//修改view的背景色
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#ffffff"));//获取Snackbar的message控件，修改字体颜色
        }
        return  instance;
    }



}
