package com.lede.second_23.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lede.second_23.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoView;

public class ImageFragment_2 extends Fragment {


    private static final String IMAGE_URL = "image";
    PhotoView image;
    private String imageUrl;
    private FragmentActivity mActivity;

    public ImageFragment_2() {
        // Required empty public constructor
    }

    public static ImageFragment_2 newInstance(String param1) {
        ImageFragment_2 fragment = new ImageFragment_2();
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
        Log.i("ImageFragment_2", "onCreateView: "+imageUrl);
        View view = inflater.inflate(R.layout.fragment_image_2, container, false);
        image = (PhotoView) view.findViewById(R.id.image);
        Glide.with(getContext()).load(imageUrl).into((PhotoView) view.findViewById(R.id.image));
//        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(View view, float x, float y) {
//                mActivity.finish();
//            }
//
//
//            public void onOutsidePhotoTap() {
//                mActivity.finish();
//            }
//        });
//        image.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
////                Toast.makeText(getActivity(), "图片被长按", Toast.LENGTH_SHORT).show();
//                showDialog(image.getDrawingCache());
////                image.getDrawingCache()
//                return false;
//            }
//        });
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
     * 提示保存图片对话框
     */
    protected void showDialog(final Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("保存图片到本地？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                saveBitmap(bitmap);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    /**
     * 保存图片方法
     * @param bm
     */
    public void saveBitmap(Bitmap bm) {
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        Log.e("TAG", "保存图片");
        File f = new File("/sdcard/27/", str+".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getActivity(), "图片已保存到27文件夹中", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}