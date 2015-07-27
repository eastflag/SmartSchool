package com.aura.smartschool.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.aura.smartschool.Constant;
import com.aura.smartschool.MainActivity;
import com.aura.smartschool.R;
import com.aura.smartschool.dialog.LoadingDialog;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MeasureSummaryVO;
import com.aura.smartschool.vo.MemberVO;

import org.json.JSONException;
import org.json.JSONObject;

public class HealthMainFragment extends BaseFragment {
	private View mView;
	private MemberVO mMember;
    private AQuery mAq;
    private MeasureSummaryVO mSummaryVO;

    private RelativeLayout rl_height, rl_growth, rl_dining, rl_pt;
    private RelativeLayout rl_weight, rl_fat, rl_smoke, rl_mental;
    private RelativeLayout rl_bmi, rl_activity;
    //private RelativeLayout rl_ranking;
    private RelativeLayout rl_map, rl_consult, rl_noti, rl_challenge;

    private TextView tv_height;
    //private TextView tv_height_status;
    private ImageView iv_height, iv_growth;
    private TextView tv_growth_grade;
    private TextView tv_weight;
    //private TextView tv_weight_status;
    private TextView tv_bmi;
    //private TextView tv_bmi_status;
    private TextView tv_fat;
    private TextView tv_smoke_status;
    //private TextView tv_smoke_cohb, tv_smoke_ppm;
    private ImageView iv_map, iv_consult, iv_noti, iv_challenge;

    private static String KEY_MEMBER = "member";

    private boolean mAnimationEnd = false;
    private float mHeight, mGrowth;

    public static HealthMainFragment newInstance(MemberVO member) {

        HealthMainFragment instance = new HealthMainFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_MEMBER, member);
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
		mView = View.inflate(getActivity(), R.layout.fragment_main, null);
        mAq = new AQuery(mView);

        rl_height = (RelativeLayout) mView.findViewById(R.id.rl_height);
        rl_growth = (RelativeLayout) mView.findViewById(R.id.rl_growth);
        rl_dining = (RelativeLayout) mView.findViewById(R.id.rl_dining);
        rl_pt = (RelativeLayout) mView.findViewById(R.id.rl_pt);

        rl_weight = (RelativeLayout) mView.findViewById(R.id.rl_weight);
        rl_fat = (RelativeLayout) mView.findViewById(R.id.rl_fat);
        rl_smoke = (RelativeLayout) mView.findViewById(R.id.rl_smoke);
        rl_mental = (RelativeLayout) mView.findViewById(R.id.rl_mental);

        rl_bmi = (RelativeLayout) mView.findViewById(R.id.rl_bmi);
        //rl_ranking = (RelativeLayout) mView.findViewById(R.id.rl_ranking);
        rl_activity = (RelativeLayout) mView.findViewById(R.id.rl_activity);

        rl_map = (RelativeLayout) mView.findViewById(R.id.rl_map);
        rl_consult = (RelativeLayout) mView.findViewById(R.id.rl_consult);
        rl_noti = (RelativeLayout) mView.findViewById(R.id.rl_noti);
        rl_challenge = (RelativeLayout) mView.findViewById(R.id.rl_challenge);

        tv_height = (TextView) mView.findViewById(R.id.tv_height);
        //tv_height_status = (TextView) mView.findViewById(R.id.tv_height_status);
        iv_height = (ImageView) mView.findViewById(R.id.iv_height);
        iv_height.setVisibility(View.INVISIBLE);

        iv_growth = (ImageView) mView.findViewById(R.id.iv_growth);
        iv_growth.setVisibility(View.INVISIBLE);
        tv_growth_grade = (TextView) mView.findViewById(R.id.tv_growth_grade);
        tv_weight = (TextView) mView.findViewById(R.id.tv_weight);
        //tv_weight_status = (TextView) mView.findViewById(R.id.tv_weight_status);
        tv_bmi = (TextView) mView.findViewById(R.id.tv_bmi);
        //tv_bmi_status = (TextView) mView.findViewById(R.id.tv_bmi_status);
        tv_fat = (TextView) mView.findViewById(R.id.tv_fat);
        tv_smoke_status = (TextView) mView.findViewById(R.id.tv_smoke_status);
        //tv_smoke_cohb = (TextView) mView.findViewById(R.id.tv_smoke_cohb);
        //tv_smoke_ppm = (TextView) mView.findViewById(R.id.tv_smoke_ppm);
        iv_map = (ImageView) mView.findViewById(R.id.iv_map);
        iv_consult = (ImageView) mView.findViewById(R.id.iv_consult);
        iv_noti = (ImageView) mView.findViewById(R.id.iv_noti);
        iv_challenge = (ImageView) mView.findViewById(R.id.iv_challenge);

        rl_height.setOnClickListener(mMeasureClick);
        rl_weight.setOnClickListener(mMeasureClick);
        rl_smoke.setOnClickListener(mMeasureClick);
        rl_bmi.setOnClickListener(mMeasureClick);
        rl_fat.setOnClickListener(mMeasureClick);
        rl_growth.setOnClickListener(mMeasureClick);
        rl_pt.setOnClickListener(mClick);
        rl_activity.setOnClickListener(mClick);
        rl_map.setOnClickListener(mClick);
        rl_consult.setOnClickListener(mClick);
        rl_noti.setOnClickListener(mClick);

        return mView;
	}

    @Override
    public void onResume() {
        super.onResume();
//        if (LoginManager.getInstance().getLoginUser().is_parent == 1) {
            setActionbar(R.drawable.actionbar_back, mMember.name);
//        } else {
//            setActionbar(R.drawable.home, mMember.name);
//        }


        if (!mAnimationEnd) {
            animateHealthMenu();
            getMeasureSummary();
        } else {
            displayData();
        }
    }

    private void animateHealthMenu() {
        Animation aniLeftToRight = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right);
        Animation aniTopToBottom = AnimationUtils.loadAnimation(getActivity(), R.anim.top_to_bottom);
        Animation aniRightToLeft = AnimationUtils.loadAnimation(getActivity(), R.anim.right_to_left);
        Animation aniBottomToTop = AnimationUtils.loadAnimation(getActivity(), R.anim.bottom_to_top);
        Animation aniTranslateAndBounce = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_bounce);

        rl_height.startAnimation(aniLeftToRight);
        rl_growth.startAnimation(aniTopToBottom);
        rl_dining.startAnimation(aniRightToLeft);
        rl_pt.startAnimation(aniBottomToTop);
        rl_weight.startAnimation(aniRightToLeft);
        rl_fat.startAnimation(aniTopToBottom);
        rl_smoke.startAnimation(aniRightToLeft);
        rl_mental.startAnimation(aniBottomToTop);
        rl_bmi.startAnimation(aniBottomToTop);
        //rl_ranking.startAnimation(aniRightToLeft);
        rl_activity.startAnimation(aniLeftToRight);
        iv_map.startAnimation(aniTranslateAndBounce);
        iv_consult.startAnimation(aniTranslateAndBounce);
        iv_noti.startAnimation(aniTranslateAndBounce);
        iv_challenge.startAnimation(aniTranslateAndBounce);

        aniLeftToRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("LDK", "onAnimationEnd");
                mAnimationEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void getMeasureSummary() {
        LoadingDialog.showLoading(getActivity());
        try {
            String url = Constant.HOST + Constant.API_GET_MEASURESUMMARY;

            JSONObject json = new JSONObject();
            json.put("member_id", mMember.member_id);

            Log.d("LDK", "url:" + url);
            Log.d("LDK", "input parameter:" + json.toString(1));

            mAq.post(url, json, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    LoadingDialog.hideLoading();

                    try {
                        if (status.getCode() != 200) {
                            iv_height.setVisibility(View.VISIBLE);
                            iv_growth.setVisibility(View.VISIBLE);
                            return;
                        }
                        Log.d("LDK", "result:" + object.toString(1));

                        if (object.getInt("result") == 0) {
                            JSONObject json = object.getJSONObject("data");

                            mSummaryVO = new MeasureSummaryVO();
                            mSummaryVO.measure_date = json.getString("measure_date");
                            mSummaryVO.height = Float.parseFloat(json.getString("height"));
                            mSummaryVO.heightStatus = json.getString("heightStatus");
                            mSummaryVO.weight = json.getString("weight");
                            mSummaryVO.weightStatus = json.getString("weightStatus");
                            mSummaryVO.fat = json.getString("fat");

                            float muscle = Float.parseFloat(json.getString("muscle"));
                            float weight = Float.parseFloat(json.getString("weight"));
                            mSummaryVO.muscle_percent = (int) (muscle /weight * 100);

                            mSummaryVO.waist = json.getString("waist");
                            mSummaryVO.skeletal = json.getString("skeletal");//골격근량
                            mSummaryVO.bmi = json.getString("bmi");
                            mSummaryVO.bmiStatus = json.getString("bmiStatus");
                            mSummaryVO.bmiGradeId = json.getString("bmiGradeId");
                            mSummaryVO.ppm = json.getString("ppm");
                            mSummaryVO.cohd = json.getString("cohd");
                            mSummaryVO.smokeStatus = json.getString("smokeStatus");
                            mSummaryVO.growthGrade = Float.parseFloat(json.getString("growthGrade"));

                            mMember.mMeasureSummaryVO = mSummaryVO;

                            mHandler.sendEmptyMessage(MainActivity.MSG_CHECK_ANIMATION);
                        } else {
                            iv_height.setVisibility(View.VISIBLE);
                            iv_growth.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {

                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayData() {
        if (mSummaryVO == null) return;

        iv_height.setVisibility(View.VISIBLE);
        iv_height.setPivotX(0.5f);
        iv_height.setPivotY(100f);
        iv_height.setScaleY(mHeight);
        iv_growth.setVisibility(View.VISIBLE);
        iv_growth.setPivotX(0.0f);
        iv_growth.setPivotY(0.0f);
        iv_growth.setScaleX(mGrowth);

        //height animation
        mHandler.sendEmptyMessage(MainActivity.MSG_INCREASE_NUMBER);
        //height image animation
        //Animation aniScale = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_bottom_to_top);
        //iv_height.startAnimation(aniScale);

        //tv_height_status.setText(mSummaryVO.heightStatus);
        //tv_growth_grade.setText(String.valueOf(mSummaryVO.growthGrade));
        tv_weight.setText(String.format("%s", mSummaryVO.weight));
        //tv_weight_status.setText(mSummaryVO.weightStatus);
        tv_bmi.setText(mSummaryVO.bmi);
        //tv_bmi_status.setText(mSummaryVO.bmiStatus);
        tv_smoke_status.setText(mSummaryVO.smokeStatus);
        //tv_smoke_cohb.setText(String.format("%s COHb", mSummaryVO.cohd));
        //tv_smoke_ppm.setText(String.format("%s ppm", mSummaryVO.ppm));
        tv_fat.setText(String.valueOf(mSummaryVO.muscle_percent) + "%");
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MainActivity.MSG_CHECK_ANIMATION:
                    if(mAnimationEnd) {
                        displayData();
                    } else {
                        mHandler.sendEmptyMessageDelayed(0, 100);
                    }
                    break;
                case MainActivity.MSG_INCREASE_NUMBER:
                    //1초 애니메이션, 20ms 간격 총 50번
                    if(mHeight < mSummaryVO.height) {
                        mHeight += mSummaryVO.height * 0.02;
                        mGrowth += mSummaryVO.growthGrade * 0.02;
                        mHandler.sendEmptyMessageDelayed(MainActivity.MSG_INCREASE_NUMBER, 20);
                    } else {
                        mHeight = mSummaryVO.height;
                        mGrowth = mSummaryVO.growthGrade;
                    }
                    tv_height.setText(String.format("%.1f", mHeight));
                    iv_height.setScaleY(mHeight / mSummaryVO.height);
                    tv_growth_grade.setText(String.format("%.0f", mGrowth));
                    iv_growth.setScaleX(mGrowth / mSummaryVO.growthGrade);
                    break;
            }
        }
    };

    View.OnClickListener mMeasureClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSummaryVO == null) {
                Util.showAlertDialog(getActivity(), getString(R.string.popup_alert_nodata));
                return;
            }
            switch (v.getId()) {
                case R.id.rl_height:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, HeightFragment.newInstance(mMember, 1)).addToBackStack(null).commit();
                    break;

                case R.id.rl_weight:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, HeightFragment.newInstance(mMember, 2)).addToBackStack(null).commit();
                    break;

                case R.id.rl_smoke:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, SmokeFragment.newInstance(mMember)).addToBackStack(null).commit();
                    break;

                case R.id.rl_bmi:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, BmiFragment.newInstance(mMember)).addToBackStack(null).commit();
                    break;

                case R.id.rl_fat:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, MuscleFragment.newInstance(mMember)).addToBackStack(null).commit();
                    break;

                case R.id.rl_growth:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, GrowthFragment.newInstance(mMember)).addToBackStack(null).commit();
                    break;
            }
        }
    };

    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_pt:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, VideoFragment.newInstance(mMember)).addToBackStack(null).commit();
                    break;

                case R.id.rl_activity:
                    Log.d("test", "onClick >> rl_activity");
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, WalkingPagerFragment.newInstance(mMember)).addToBackStack(null).commit();
                    break;

                case R.id.rl_map:
                    if(mMember.lat != 0) {
                        getFragmentManager().beginTransaction().replace(R.id.content_frame, LocationFragment.newInstance(mMember)).addToBackStack(null).commit();
                    } else {
                        Util.showToast(getActivity(), "위치정보가 없습니다");
                    }
                    break;
                case R.id.rl_consult:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, ConsultMainFragment.newInstance(mMember)).addToBackStack(null).commit();
                    break;
                case R.id.rl_noti:
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, SchoolNoticePagerFragment.newInstance(mMember)).addToBackStack(null).commit();
                    break;
            }
        }
    };
}