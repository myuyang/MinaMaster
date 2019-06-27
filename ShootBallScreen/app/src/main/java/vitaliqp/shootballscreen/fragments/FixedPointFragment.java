package vitaliqp.shootballscreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import vitaliqp.shootballscreen.R;
import vitaliqp.shootballscreen.datas.Constant;
import vitaliqp.shootballscreen.datas.SendJsonToServer;

/**
 * 类名： FixedPointFragment
 * 时间：2019/4/15 下午2:00
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class FixedPointFragment extends BaseFragment {

    private static final String INDEX = "0";
    private static final String NAME = "fixedPointFragment";
    private static SparseIntArray mManual;

    static {
        mManual = new SparseIntArray();
        mManual.append(0, 3500);
        mManual.append(1, 0);
        mManual.append(2, -1);
        mManual.append(3, -1);
    }

    @BindView(R.id.ll_fragment_preset_bg)
    LinearLayout mLlFragmentPresetBg;
    Unbinder unbinder;
    @BindView(R.id.tv_fragment_fixed_content)
    TextView mTvFragmentFixedContent;
    @BindView(R.id.seek_bar_fixed)
    SeekBar mSeekBarFixed;
    private SendJsonToServer mJsonFixedPoint;
    private List<SendJsonToServer.ShootBean> mFixedShoot;
    private SendJsonToServer.ShootBean mFixedBean;
    private String mIndex;
    private String mName;
    private OnFixedPointFragmentListener mListener;
    private SeekBar.OnSeekBarChangeListener mSeekBarFixedChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mFixedBean.setDistance(progress * 10);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            onButtonPressed(mJsonFixedPoint);
        }
    };

    public FixedPointFragment() {
        // Required empty public constructor
    }

    /**
     * instance
     *
     * @param index
     * @param name
     * @return
     */
    public static FixedPointFragment newInstance(String index, String name) {
        FixedPointFragment fragment = new FixedPointFragment();
        Bundle args = new Bundle();
        args.putString(INDEX, index);
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFixedPointFragmentListener) {
            mListener = (OnFixedPointFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFixedPointFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getString(INDEX);
            mName = getArguments().getString(NAME);
        }

        mJsonFixedPoint = new SendJsonToServer();
        mFixedShoot = new ArrayList<>();
        mFixedBean = new SendJsonToServer.ShootBean();
        mJsonFixedPoint.setType(Constant.MODE_SHOOT);
        mFixedBean.setFrequency(mManual.get(0));
        mFixedBean.setDistance(mManual.get(1));
        mFixedBean.setElevationAngular(mManual.get(2));
        mFixedBean.setHorizontalAngular(mManual.get(3));
        mFixedShoot.add(mFixedBean);
        mJsonFixedPoint.setShoot(mFixedShoot);
        onButtonPressed(mJsonFixedPoint);
    }

    /**
     * 监听调用
     *
     * @param preset
     */
    public void onButtonPressed(SendJsonToServer preset) {
        if (mListener != null) {
            mListener.onFixedPointFragment(preset);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sbs_fragment_fixed_point, container, false);
        unbinder = ButterKnife.bind(this, view);
        setSelectButton();
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setSelectButton() {
    }

    private void initView() {
        mSeekBarFixed.setOnSeekBarChangeListener(mSeekBarFixedChangeListener);
    }

    private void setShootBallBg(int model) {
        switch (model) {
            case 1:
                mLlFragmentPresetBg.setBackgroundResource(R.mipmap.sbs_fragment_shoot_ball_lr);
                break;
            case 2:
                mLlFragmentPresetBg.setBackgroundResource(R.mipmap.sbs_fragment_shoot_ball_df);
                break;
            case 3:
                mLlFragmentPresetBg.setBackgroundResource(R.mipmap.sbs_fragment_fixed);
                break;
            default:
                mLlFragmentPresetBg.setBackgroundResource(R.mipmap.sbs_fragment_shoot_ball_lr);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFixedPointFragmentListener {
        /**
         * Fragment回调
         *
         * @param fixed
         */
        void onFixedPointFragment(SendJsonToServer fixed);
    }


}
