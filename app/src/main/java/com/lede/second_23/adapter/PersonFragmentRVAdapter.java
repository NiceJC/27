package com.lede.second_23.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lede.second_23.R;
import com.lede.second_23.bean.PersonUtilsBean;

import java.util.List;

/**
 * Created by ld on 17/5/4.
 */

public class PersonFragmentRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<PersonUtilsBean.DataBean> dataList;
    private LayoutInflater layoutInflater;
    private final int TIMETYPE = 1;
    private final int SHOWTYPE = 2;
    private String timeTag;
    private View view;
    private ImageView showView_show;
    private ImageView showView_play;

    public PersonFragmentRVAdapter(Context context, List<PersonUtilsBean.DataBean> dataList) {
        this.context = context;
        this.dataList = dataList;

        if (dataList.size() != 0 && dataList != null) {
            timeTag = dataList.get(1).getCreateTime().substring(0, 10);
            Log.i("TAG", "PersonFragmentRVAdapter: " + timeTag);
        }

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        Log.i("TAG", "onCreateViewHolder: ");
        RecyclerView.ViewHolder viewHolder = null;
//        switch (viewType) {
//            case TIMETYPE:
        View itemView0 = layoutInflater.inflate(R.layout.item_person_fragment_time, parent, false);
        viewHolder = new TimeViewHolder(itemView0);
//                break;
//            case SHOWTYPE:
//                View itemView1 = layoutInflater.inflate(R.layout.item_person_fragment_show, parent, false);
//                viewHolder = new ShowViewHolder(itemView1);
//                break;
//        }
//
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("TAG", "onBindViewHolder:  创建item视图" + position + "type" + getItemViewType(position));
        TimeViewHolder timeViewHolder;
//        ShowViewHolder showViewHolder;
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if (getItemViewType(position)==TIMETYPE) {
//                    return 3;
//                }
//                return 1;
//            }
//        });
//        switch (getItemViewType(position)) {
//            case TIMETYPE:
        timeViewHolder = (TimeViewHolder) holder;
//                timeTag= dataList.get(position+1).getCreateTime().substring(0,10);
        timeViewHolder.tv_time.setText(dataList.get(position).getCreateTime());
        int width=((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        timeViewHolder.gl_show.removeAllViews();
        for (int i = 0; i < dataList.get(position).getBeanList().size(); i++) {
            view = layoutInflater.inflate(R.layout.item_person_fragment_show, null);

//                    showViewHolder=new ShowViewHolder(view);
            showView_show = (ImageView) view.findViewById(R.id.iv_person_fragment_item_show);
            showView_play = (ImageView) view.findViewById(R.id.iv_person_fragment_item_play);
            if (dataList.get(position).getBeanList().get(i).getForumMedia().getPic().equals("http://7xr1tb.com1.z0.glb.clouddn.com/null")) {
                Glide.with(context)
                        .load(dataList.get(position).getBeanList().get(i).getImgs().get(0).getUrl())
                        .error(R.mipmap.loading)
                        .placeholder(R.mipmap.loading)
                        .into(showView_show);
                showView_play.setVisibility(View.GONE);
            } else {
                Glide.with(context)
                        .load(dataList.get(position).getBeanList().get(i).getForumMedia().getPic())
                        .error(R.mipmap.loading)
                        .placeholder(R.mipmap.loading)
                        .into(showView_show);

            }

            timeViewHolder.gl_show.addView(view,width/ 3, width/ 3);
        }

//                break;
//
//            case SHOWTYPE:
//                showViewHolder = (ShowViewHolder) holder;

//                if (dataList.get(position).getForumMedia().getPic().equals("http://7xr1tb.com1.z0.glb.clouddn.com/null")) {
//                    Glide.with(context)
//                            .load(dataList.get(position).getImgs().get(0).getUrl())
//                            .into(showViewHolder.iv_show);
//                    showViewHolder.iv_play.setVisibility(View.GONE);
//                }else {
//                    Glide.with(context)
//                            .load(dataList.get(position).getForumMedia().getPic())
//                            .into(showViewHolder.iv_show);
//
//                }
//
//                break;
//        }
    }

    @Override
    public int getItemCount() {
//        int num=1;
//        String timeTag1="";
//        if (dataList.size()!=0&&dataList!=null) {
//            for (int i = 0; i < dataList.size(); i++) {
//                if (dataList.get(i)!=null) {
//                    timeTag1=dataList.get(i).getCreateTime().substring(0,10);
//                    break;
//                }
//            }
////            timeTag1=timeTag;
//            for (int i = 0; i < dataList.size(); i++) {
//                if (dataList.get(i)!=null) {
//                    Log.i("TAG", "getItemCount: "+timeTag1);
//                    if (timeTag1.equals(dataList.get(i).getCreateTime().substring(0,10))) {
//
//                    }else {
//                        timeTag1=dataList.get(i).getCreateTime().substring(0,10);
//                        num++;
//                    }
//                }
//
//            }
//            return dataList.size()+num;
//        }
        Log.i("TAG", "getItemCount: " + dataList.size());
        return dataList.size();

    }

    @Override
    public int getItemViewType(int position) {
//        if (dataList.size()!=0&&dataList!=null) {
//                if (position==0) {
//                    dataList.add(position,null);
//                    return TIMETYPE;
//                }
//            for (int i = 0; i < dataList.size(); i++) {
//                if (dataList.get(i)!=null) {
//                    timeTag=dataList.get(i).getCreateTime().substring(0,10);
//                    break;
//                }
//            }
//
//                for (int i = 0; i < dataList.size(); i++) {
//                    if (dataList.get(i)!=null) {
//                        if (timeTag.equals(dataList.get(i).getCreateTime().substring(0, 10))) {
//                            return SHOWTYPE;
//                        } else {
//                            timeTag = dataList.get(i).getCreateTime().substring(0, 10);
//                            dataList.add(position, null);
//                            return TIMETYPE;
//                        }
//                    }
//                }
//
//        }
        if (dataList.get(position) != null) {
            return SHOWTYPE;
        }
        return TIMETYPE;
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_time;
        private final GridLayout gl_show;

        public TimeViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_person_fragment_item_time);
            gl_show = (GridLayout) itemView.findViewById(R.id.gl_person_fragment_item_show);
        }
    }

    class ShowViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iv_show;
        private final ImageView iv_play;

        public ShowViewHolder(View itemView) {
            super(itemView);
            iv_show = (ImageView) itemView.findViewById(R.id.iv_person_fragment_item_show);
            iv_play = (ImageView) itemView.findViewById(R.id.iv_person_fragment_item_play);
        }
    }

}
