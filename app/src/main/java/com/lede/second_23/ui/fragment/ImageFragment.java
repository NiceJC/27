package com.lede.second_23.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lede.second_23.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFragment extends Fragment {


    private static final String IMAGE_URL = "image";
    PhotoView image;
    private String imageUrl;
    private FragmentActivity mActivity;
    private Dialog mDialog;
    private Bitmap bitmap;
    private PopupWindow popupWindow;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String param1) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        image = (PhotoView) view.findViewById(R.id.image);
        Glide.with(getContext()).load(imageUrl).into(image);
        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                mActivity.finish();
            }


            public void onOutsidePhotoTap() {
                mActivity.finish();
            }
        });
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Toast.makeText(getActivity(), "图片被长按", Toast.LENGTH_SHORT).show();
                bitmap=null;
                bitmap=image.getDrawingCache();
                showPopwindow();
//                image.getDrawingCache()
                return false;
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 提示保存图片底部弹窗
     */
    private void showPopwindow() {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_save_or_report, null);

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        view.findViewById(R.id.btn_save).setOnClickListener(btnlistener);
//        root.findViewById(R.id.btn_report).setOnClickListener(btnlistener);
        view.findViewById(R.id.btn_cancel).setOnClickListener(btnlistener);
        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.7f;

        getActivity().getWindow().setAttributes(params);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);


        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
//        window.showAtLocation(getActivity().findViewById(R.id.ll_prv_forum_detail_bottom),
//                Gravity.BOTTOM, 0, 0);
        popupWindow.showAtLocation(image,
                Gravity.BOTTOM, 0, 0);
        //popWindow消失监听方法
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1.0f;

                getActivity().getWindow().setAttributes(params);
            }
        });

    }

    private View.OnClickListener btnlistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save: // 保存图片
                    popupWindow.dismiss();
                    checkLocatePermission();

                    break;
                // 举报
//                case R.id.btn_report:
//                    Intent intent=new Intent(getActivity(), ReportActivity.class);
//                    getActivity().startActivity(intent);
//                    break;
                // 取消
                case R.id.btn_cancel:
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    break;
            }
        }
    };



    //适用于安卓6.0以上  的运行时权限适配
    private void checkLocatePermission() {

        boolean isGranted = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {
            saveBitmap(bitmap);

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 666);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 666:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权
                    saveBitmap(bitmap);
//                    DialogUtil.showItemSelectDialog(SetUpActivity.this, screenWidth / 25 * 24, onItemSelectedListener, FROM_NATIVE, FROM_CAMERA);

                } else {
                    //用户拒绝授权
                    Toast.makeText(getActivity(), "没有权限将可能出现异常，用户可以前往应用权限进行设置", Toast.LENGTH_SHORT).show();

//                    ToastUtils.toast(this, "没有权限将可能出现异常，用户可以前往应用权限进行设置");
                }
                break;
        }
    }
    /**
     * 保存图片方法
     * @param bm
     */
    public void saveBitmap(Bitmap bm) {

        //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的27文件夹
        File nf = new File(Environment.getExternalStorageDirectory() + "/27");

        if(!nf.exists()){
            nf.mkdir();
        }


        //在根目录下面的photo文件夹下 创建jpg文件
        String fileName="/"+System.currentTimeMillis()+"photo.jpg";
        File f = new File(nf, fileName);

        try {

            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Uri uri = Uri.fromFile(f);
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(getActivity(), "图片已保存到27文件夹中", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            bm.recycle();
            bitmap.recycle();
//            bitmap1.recycle();
        }

    }

}