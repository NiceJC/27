package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by Administrator on 2016/9/20.
 */
public class PullToRrefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    private static final OnRefreshListener<RecyclerView> defaultOnRefreshListener =  new OnRefreshListener<RecyclerView>() {

        @Override
        public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
            refreshView.getRefreshableView().getAdapter().notifyDataSetChanged();
            refreshView.onRefreshComplete();
        }
    };


    public PullToRrefreshRecyclerView(Context context) {
        super(context);
        setOnRefreshListener(defaultOnRefreshListener);
    }

    public PullToRrefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(defaultOnRefreshListener);
    }

    public PullToRrefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
        setOnRefreshListener(defaultOnRefreshListener);
    }

    public PullToRrefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
        setOnRefreshListener(defaultOnRefreshListener);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        return new RecyclerView(context,attrs);
    }

    /**
     *
     *  什么时候上拉开始
     * @return
     */
    @Override
    protected boolean isReadyForPullEnd() {

        if (mRefreshableView.computeVerticalScrollExtent() + mRefreshableView.computeVerticalScrollOffset()
                >= mRefreshableView.computeVerticalScrollRange())
            return true;
        else
            return false;
    }

    /**
     *  下拉什么时候开始
     * @return
     */
    @Override
    protected boolean isReadyForPullStart() {
//        if (((LinearLayoutManager) mRefreshableView.getLayoutManager()).findFirstVisibleItemPosition() == 0) {
//            return true;
//        }else
//            return false;

        final RecyclerView.Adapter adapter = mRefreshableView.getAdapter();

        if (null == adapter) {
            if (DEBUG) {
                Log.d(LOG_TAG, "isFirstItemVisible. Empty View.");
            }
            return true;

        } else {

            /**
             * This check should really just be:
             * mRefreshableView.getFirstVisiblePosition() == 0, but PtRListView
             * internally use a HeaderView which messes the positions up. For
             * now we'll just add one to account for it and rely on the inner
             * condition which checks getTop().
             */
            if (((LinearLayoutManager)mRefreshableView.getLayoutManager()).findFirstVisibleItemPosition() <= 1) {
                final View firstVisibleChild = mRefreshableView.getChildAt(0);
                if (firstVisibleChild != null) {
                    return firstVisibleChild.getTop() >= mRefreshableView.getTop();
                }
            }
        }

        return false;
    }
}
