package com.lede.second_23.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lede.second_23.R;
import com.lede.second_23.adapter.MyFragmentPagerAdapter;
import com.lede.second_23.utils.MyViewPager;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * Created by ld on 17/2/22.
 */

public class ChildFragment extends Fragment {

    public MyViewPager vp_childFragment_ViewPager;
    public static ChildFragment instance;
    private ArrayList<Fragment> fragment_List;
    private MainFragment mainFragment;
    private PersonFragment personFragment;
    private IUnReadMessageObserver iUnReadMessageObserver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.child_fragment_layout,container,false);
        initView(view);
        instance=this;
        return view;
    }

    private void initView(View view) {
        vp_childFragment_ViewPager = (MyViewPager) view.findViewById(R.id.vp_childFragment_ViewPager);
        initViewPager();
    }

    private void initViewPager() {
        fragment_List=new ArrayList<>();
        mainFragment=new MainFragment();
        fragment_List.add(mainFragment);
        personFragment=new PersonFragment();
        fragment_List.add(personFragment);
        vp_childFragment_ViewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),fragment_List));
        vp_childFragment_ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mainFragment.setLineCallBack(positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        iUnReadMessageObserver = new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                if (i >= 1) {
                    Log.i("TAS", "onCountChanged: " + i);
                    PersonFragment.instance.iv_personfragment_msg_test.setImageResource(R.mipmap.grzx_message);
                    MainFragment.instance.iv_mainFragment_person.setImageResource(R.mipmap.new_personal);
                }else {
                    PersonFragment.instance.iv_personfragment_msg_test.setImageResource(R.mipmap.comment_off);
                    MainFragment.instance.iv_mainFragment_person.setImageResource(R.mipmap.personal);
                }
            }
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(iUnReadMessageObserver, Conversation.ConversationType.PRIVATE);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(iUnReadMessageObserver);
    }
}
