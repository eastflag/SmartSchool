package com.aura.smartschool.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.aura.smartschool.Constant;
import com.aura.smartschool.R;
import com.aura.smartschool.adapter.VideoListAdapter;
import com.aura.smartschool.dialog.LoadingDialog;
import com.aura.smartschool.utils.SchoolLog;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MemberVO;
import com.aura.smartschool.vo.VideoVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VideoFragment extends Fragment {

    private View mView;
    private AQuery mAq;

    private MemberVO mMember;
    private ArrayList<VideoVO> mVideoList = new ArrayList<VideoVO>();
    private ListView mListview;
    private VideoListAdapter mAdapter;

    private int mType; //1: pt 화면, 2:키, 3: 체중, 4:bmi

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(MemberVO member, int type) {

        VideoFragment instance = new VideoFragment();

        Bundle args = new Bundle();
        args.putSerializable("member", member);
        args.putInt("type", type);
        instance.setArguments(args);

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        mAq = new AQuery(getActivity());

        mMember = (MemberVO) args.getSerializable("member");
        mType = args.getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = View.inflate(getActivity(), R.layout.fragment_growth, null);

        mListview = (ListView) mView.findViewById(R.id.listview);
        mAdapter = new VideoListAdapter(getActivity(), mMember, mVideoList, mType);
        mListview.setAdapter(mAdapter);

        getVideoList();

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void getVideoList() {
        LoadingDialog.showLoading(getActivity());
        try {
            String url = "";
            JSONObject json = new JSONObject();

            switch(mType) {
                case 1:
                    url = Constant.HOST + Constant.API_GET_VIDEOLIST_BY_SECTION;
                    //동영상 데이터베이스에 초등학교, 중학교만 있으므로 고등학생 일경우에 중학교로 세팅
                    String gubun2 = mMember.mSchoolVO.gubun2;
                    if(!"초등학교".equals(gubun2) && !"중학교".equals(gubun2)) {
                        gubun2 = "중학교";
                    }
                    json.put("infoType", gubun2);
                    break;
                case 2:
                    url = Constant.HOST + Constant.API_GET_VIDEOLIST_BY_MASTERID;
                    json.put("masterGradeId", mMember.mMeasureSummaryVO.heightGradeId);
                    break;
                case 3:
                    url = Constant.HOST + Constant.API_GET_VIDEOLIST_BY_MASTERID;
                    json.put("masterGradeId", mMember.mMeasureSummaryVO.weightGradeId);
                    break;
                case 4:
                    url = Constant.HOST + Constant.API_GET_VIDEOLIST_BY_MASTERID;
                    json.put("masterGradeId", mMember.mMeasureSummaryVO.bmiGradeId);
                    break;
            }

            SchoolLog.d("LDK", "url:" + url);
            SchoolLog.d("LDK", "input parameter:" + json.toString(1));

            mAq.post(url, json, JSONObject.class, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    LoadingDialog.hideLoading();
                    try {
                        if (status.getCode() != 200) {
                            return;
                        }
                        SchoolLog.d("LDK", "result:" + object.toString(1));

                        if ("0".equals(object.getString("result"))) {
                            JSONArray array = object.getJSONArray("data");
                            for(int i=0; i < array.length(); ++i) {
                                VideoVO video = new VideoVO();
                                video.image_url = array.getJSONObject(i).getString("thumbnailUrl");
                                video.video_url = array.getJSONObject(i).getString("videoUrl");
                                video.title = array.getJSONObject(i).getString("title");
                                video.duration = Util.convertSecondToMinute(array.getJSONObject(i).getString("duration"));
                                mVideoList.add(video);
                            }
                            mAdapter.setData(mVideoList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Util.showToast(getActivity(), object.getString("msg"));
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
}