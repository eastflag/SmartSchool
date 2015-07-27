package com.aura.smartschool.fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aura.smartschool.R;
import com.aura.smartschool.vo.MemberVO;

public class BmiFragment extends BaseFragment {

    private View mView;
    private TextView tv_bmi, tv_fat;
    private TextView tv_help_left, tv_help_right;
    private ImageView iv_bmi;
    private PopupWindow mPopup;

    private MemberVO mMember;

    public BmiFragment() {
        // Required empty public constructor
    }

    public static BmiFragment newInstance(MemberVO member) {

        BmiFragment instance = new BmiFragment();

        Bundle args = new Bundle();
        args.putSerializable("member", member);
        instance.setArguments(args);

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mMember = (MemberVO) args.getSerializable("member");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_bmi, null);

        tv_bmi = (TextView) mView.findViewById(R.id.tv_bmi);
        tv_fat = (TextView) mView.findViewById(R.id.tv_fat);
        tv_help_left = (TextView) mView.findViewById(R.id.tv_help_left);
        tv_help_right = (TextView) mView.findViewById(R.id.tv_help_right);
        iv_bmi = (ImageView) mView.findViewById(R.id.iv_bmi);

        tv_bmi.setText(String.format("%s", mMember.mMeasureSummaryVO.bmi));
        tv_fat.setText(String.valueOf(mMember.mMeasureSummaryVO.fat) + "%");

        if(mMember.mMeasureSummaryVO.bmiStatus.startsWith("저체중")) {
            iv_bmi.setImageResource(R.drawable.type_under);
        } else if(mMember.mMeasureSummaryVO.bmiStatus.startsWith("정상상")) {
            iv_bmi.setImageResource(R.drawable.type_normal);
        } else if(mMember.mMeasureSummaryVO.bmiStatus.startsWith("과체중")) {
            iv_bmi.setImageResource(R.drawable.type_over_1);
        } else if(mMember.mMeasureSummaryVO.bmiStatus.startsWith("비만")) {
            iv_bmi.setImageResource(R.drawable.type_over_2);
        } else {
            iv_bmi.setImageResource(R.drawable.type_over_3);
        }

        tv_help_left.setOnClickListener(mClick);
        tv_help_right.setOnClickListener(mClick);

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(mPopup != null && mPopup.isShowing()) {
            mPopup.dismiss();
        }
    }

    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mPopup != null && mPopup.isShowing()) {
                mPopup.dismiss();
                return;
            }

            DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, dm);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, dm);
            View view = View.inflate(getActivity(), R.layout.popup_help, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);

            switch(v.getId()) {
                case R.id.tv_help_left:
                    tv_title.setText("BMI란?");
                    tv_content.setText("체중(kg)을 키의 제곱으로 나눈 값을 통해 지방의 양을 추정하는 비만 측정법이다. 수치가 클수록 체격이 커진다.");

                    mPopup = new PopupWindow(view, width, height);
                    mPopup.setAnimationStyle(-1);
                    mPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
                    //mPopup.showAsDropDown(tv_lastmonth);
                    break;

                case R.id.tv_help_right:
                    tv_title.setText("체지방률이란?");
                    tv_content.setText("체중에서 체지방이 차지하는 비율. 수치가 작을수록 근육형체형이다.");

                    mPopup = new PopupWindow(view, width, height);
                    mPopup.setAnimationStyle(-1);
                    mPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
                    break;
            }
        }
    };
}