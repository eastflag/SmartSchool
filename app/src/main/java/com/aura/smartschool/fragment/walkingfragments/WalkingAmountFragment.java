package com.aura.smartschool.fragment.walkingfragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aura.smartschool.R;
import com.aura.smartschool.fragment.BaseFragment;
import com.aura.smartschool.service.StepCounterService;
import com.aura.smartschool.vo.MemberVO;

/**
 * Created by Administrator on 2015-06-28.
 */
public class WalkingAmountFragment extends BaseFragment {

    private static String KEY_MEMBER = "member";
    private MemberVO mMember;

    private static float WALKING_COEFFICIENT = 0.9F;

    private TextView mTvWalkingCount;
    private TextView mTvCalories;
    private TextView mTvDistance;
    private TextView mTvWalkingTime;


    private IBinder mBinder;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            mBinder = binder;
            ((StepCounterService.StepCounterBinder) binder).setSensorDelay(StepCounterService.SENSOR_DELAY_TYPE.WALKING_FRAGMENT_SHOW, mStepCounterHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBinder = null;
        }
    };

    //Message hander
    private final Handler mStepCounterHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Bundle data = message.getData();
            int steps = data.getInt("steps");
            int totalWalkingTime = data.getInt("time");

            mTvWalkingCount.setText(String.valueOf(steps));

            int hours = totalWalkingTime / 3600;
            int minutes = (totalWalkingTime % 3600) / 60;
            int seconds = totalWalkingTime % 60;

            mTvCalories.setText(getCalories(totalWalkingTime) + " kcal");
            mTvDistance.setText(String.format("%.2f Km", getDistance(steps)));
            mTvWalkingTime.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        }
    };

    private int getCalories(int second) {
        int weight = 72;
        int height = 177;
        int age = 31;
        return (int)(WALKING_COEFFICIENT*weight/15/60*second);
    }

    private double getDistance(int steps) {
        /*
        Men - you can multiply your height in cm by 0.415
        Ladies - multiply your height in cm by 0.413
         */
        int height = 177;

        return (height*0.415)*steps/100/1000;
    }

    public static WalkingAmountFragment newInstance(MemberVO member) {
        WalkingAmountFragment instance = new WalkingAmountFragment();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_walking_count, null);
        mTvWalkingCount = (TextView) view.findViewById(R.id.tv_walking_count);
        mTvCalories = (TextView) view.findViewById(R.id.tv_calories);
        mTvDistance = (TextView) view.findViewById(R.id.tv_distance);
        mTvWalkingTime = (TextView) view.findViewById(R.id.tv_walking_time);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionbar(R.drawable.btn_pre, mMember.name);

        Intent serviceIntent = new Intent(getActivity(), StepCounterService.class);
        getActivity().startService(serviceIntent);
        getActivity().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mBinder != null) {
            ((StepCounterService.StepCounterBinder) mBinder).setSensorDelay(StepCounterService.SENSOR_DELAY_TYPE.WALKING_FRAGMENT_DISAPPEAR, null);
        }
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder = null;
    }
}


