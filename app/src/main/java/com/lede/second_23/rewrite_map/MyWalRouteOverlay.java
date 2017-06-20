package com.lede.second_23.rewrite_map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.WalkPath;
import com.lede.second_23.MyApplication;
import com.lede.second_23.R;

/**
 * Created by ld on 17/6/9.
 */

public class MyWalRouteOverlay extends WalkRouteOverlay {
    private Bitmap myBitmap;
    public MyWalRouteOverlay(Context context, AMap aMap, WalkPath walkPath, LatLonPoint latLonPoint, LatLonPoint latLonPoint1) {
        super(context, aMap, walkPath, latLonPoint, latLonPoint1);
    }

//    public  void setStartBitmap(Bitmap bitmap){
//        myBitmap=bitmap;
//        getStartBitmapDescriptor();
//    }
    @Override
    protected BitmapDescriptor getStartBitmapDescriptor() {
        Bitmap bitmap=null;
        bitmap= BitmapFactory.decodeResource(MyApplication.instance.getResources(), R.mipmap.location_start);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    protected BitmapDescriptor getEndBitmapDescriptor() {
        Bitmap bitmap=null;
        bitmap= BitmapFactory.decodeResource(MyApplication.instance.getResources(), R.mipmap.location_end);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
