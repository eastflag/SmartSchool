package com.aura.smartschool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.aura.smartschool.LoginManager;
import com.aura.smartschool.R;
import com.aura.smartschool.fragment.walkingfragments.WalkingCountFragment;
import com.aura.smartschool.fragment.walkingfragments.WalkingHistoryFragment;
import com.aura.smartschool.vo.MemberVO;

/**
 * Created by Administrator on 2015-06-14.
 */
public class WalkingPagerFragment extends Fragment implements View.OnClickListener{

    private static String KEY_MEMBER = "member";
    private MemberVO mMember;

    private View mView;
    private FragmentStatePagerAdapter adapterViewPager;
    private ViewPager mVpPager;

    private View llTabContainer;
    private View mTabWalkingCount;
    private View mTabWalkingHistory;
    private View mTabSetting;

    public static WalkingPagerFragment newInstance(MemberVO member) {

        WalkingPagerFragment instance = new WalkingPagerFragment();

        Bundle args = new Bundle();
        args.putSerializable("member", member);
        instance.setArguments(args);

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mMember = (MemberVO) args.getSerializable(KEY_MEMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_walking_view_pager, null);

        llTabContainer = mView.findViewById(R.id.ll_tab_container);
        mTabWalkingCount = mView.findViewById(R.id.fl_tab_walking_count);
        mTabWalkingHistory = mView.findViewById(R.id.fl_tab_walking_history);
        mTabSetting = mView.findViewById(R.id.fl_tab_setting);

        mTabWalkingCount.setOnClickListener(this);
        mTabWalkingHistory.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);

        mTabWalkingCount.setSelected(true);

        if (LoginManager.getInstance().getLoginUser().is_parent == 1) {
            llTabContainer.setVisibility(View.GONE);
        }

        mVpPager = (ViewPager) mView.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(this.getActivity().getSupportFragmentManager(), mMember);
        mVpPager.setAdapter(adapterViewPager);
        mVpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                mTabWalkingCount.setSelected(false);
                mTabWalkingHistory.setSelected(false);
                mTabSetting.setSelected(false);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (position == 0) {
                    mTabWalkingCount.setSelected(true);
                } else if (position == 1) {
                    mTabWalkingHistory.setSelected(true);
                } else {
                    mTabSetting.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVpPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_tab_walking_count:
                mVpPager.setCurrentItem(0);
                break;
            case R.id.fl_tab_walking_history:
                mVpPager.setCurrentItem(1);
                break;
            case R.id.fl_tab_setting:
                mVpPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        private MemberVO mMember;
        public MyPagerAdapter(FragmentManager fragmentManager, MemberVO memberVO) {
            super(fragmentManager);
            this.mMember = memberVO;
        }

        @Override
        public int getCount() {
            return LoginManager.getInstance().getLoginUser().is_parent == 0 ? 2 : 1;
        }

        @Override
        public Fragment getItem(int position) {
            if (LoginManager.getInstance().getLoginUser().is_parent == 1) {
                return WalkingHistoryFragment.newInstance(mMember);
            }
            switch (position) {
                case 0:
                    return WalkingCountFragment.newInstance(mMember);
                case 1:
                    return WalkingHistoryFragment.newInstance(mMember);
                default:
                    return null;
            }
        }
   }
}
