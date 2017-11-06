package com.lede.second_23.utils;

/**
 * Created by ld on 17/2/23.
 */


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


/**
 * 重写，添加了判定滑动方向的方法
 * @author zxya
 *
 */
public class MyViewPager extends ViewPager {
    private boolean left = false;
    private boolean right = false;
    private boolean isScrolling = false;
    private int lastValue = -1;
    private ChangeViewCallback changeViewCallback = null;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyViewPager(Context context) {
        super(context);
        init();
    }

    /**
     * init method .
     */
    private void init() {
        addOnPageChangeListener(listener);
    }

    /**
     * listener ,to get move direction .
     */
    public OnPageChangeListener listener = new OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 1) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }

//            Debug.infoByTag("meityitianViewPager",
//                    "meityitianViewPager  onPageScrollStateChanged : arg0:"
//                            + arg0);
            if (arg0 == 2) {
//                Debug.infoByTag("meityitianViewPager",
//                        "meityitianViewPager  onPageScrollStateChanged  direction left ? "
//                                + left);
//                Debug.infoByTag("meityitianViewPager",
//                        "meityitianViewPager  onPageScrollStateChanged  direction right ? "
//                                + right);
                //notify ....
                if(changeViewCallback!=null){
                    changeViewCallback.changeView(left, right);
                }
                right = left = false;
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (isScrolling) {
                if (lastValue > arg2) {
                    // 递减，向右侧滑动
                    right = true;
                    left = false;
                } else if (lastValue < arg2) {
                    // 递减，向右侧滑动
                    right = false;
                    left = true;
                } else if (lastValue == arg2) {
                    right = left = false;
                }
            }
            Log.i("meityitianViewPager",
                    "meityitianViewPager onPageScrolled  last :arg2  ,"
                            + lastValue + ":" + arg2);
            lastValue = arg2;
        }

        @Override
        public void onPageSelected(int arg0) {
            if(changeViewCallback!=null){
                changeViewCallback.getCurrentPageIndex(arg0);
            }
        }
    };

    /**
     * 得到是否向右侧滑动
     * @return true 为右滑动
     */
    public boolean getMoveRight(){
        return right;
    }

    /**
     * 得到是否向左侧滑动
     * @return true 为左做滑动
     */
    public boolean getMoveLeft(){
        return left;
    }

    /**
     *  滑动状态改变回调
     * @author zxy
     *
     */
    public interface ChangeViewCallback{
        /**
         * 切换视图 ？决定于left和right 。
         * @param left
         * @param right
         */
        public  void changeView(boolean left, boolean right);
        public  void  getCurrentPageIndex(int index);
    }

    /**
     * set ...
     * @param callback
     */
    public void  setChangeViewCallback(ChangeViewCallback callback){
        changeViewCallback = callback;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

//        float DownX=0f;
//        float DownY=0f;
//        float moveX=0f;
//        float moveY=0f;
//        long currentMS=0;
//
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                DownX = event.getX();//float DownX
//                DownY = event.getY();//float DownY
//                moveX = 0;
//                moveY = 0;
//                currentMS = System.currentTimeMillis();//long currentMS     获取系统时间
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveX += Math.abs(event.getX() - DownX);//X轴距离
//                moveY += Math.abs(event.getY() - DownY);//y轴距离
//                DownX = event.getX();
//                DownY = event.getY();
//                break;
//            case MotionEvent.ACTION_UP:
//                long moveTime = System.currentTimeMillis() - currentMS;//移动时间
//                //判断是否继续传递信号
////                if(moveTime>200&&(moveX>20||moveY>20)){
////                    if(moveX>20||moveX<-20){
////                        return true;
////                    }
////
////
//////                    return true; //不再执行后面的事件，在这句前可写要执行的触摸相关代码。点击事件是发生在触摸弹起后
////                }
//
//                if(moveX>20||moveY>20){
//                    return  true;
//                }
//
//                break;
//        }
//



        return super.onInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }
}