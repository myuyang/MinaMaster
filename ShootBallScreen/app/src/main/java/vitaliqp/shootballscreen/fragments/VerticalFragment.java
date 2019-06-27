package vitaliqp.shootballscreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
 * 类名： VerticalFragment
 * 时间：2019/3/19 下午5:04
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class VerticalFragment extends BaseFragment {

    public static final int FIXED_MAX_ANGLE = 60;
    public static final int FIXED_MIN_ANGLE = 20;
    public static final int FIXED_MAX_DISTANCE = 30;
    public static final int FIXED_MIN_DISTANCE = 16;
    public static final int FIXED_MAX_FREQUENCYE = 60;
    public static final int FIXED_MIN_FREQUENCYE = 3;
    private static final String INDEX = "2";
    private static final String NAME = "verticalFragment";

    private static SparseIntArray mVertical;

    static {
        mVertical = new SparseIntArray();
        mVertical.append(0, 3500);
        mVertical.append(1, -1);
        mVertical.append(2, 0);
        mVertical.append(3, -1);
    }

    EditText mEtFragmentFixedFrequency;
    Unbinder unbinder;
    @BindView(R.id.tv_fragment_vertical_content)
    TextView mTvFragmentVerticalContent;
    @BindView(R.id.seek_bar_vertical)
    SeekBar mSeekBarVertical;
    private String mIndex;
    private String mName;
    private OnVerticalFragmentListener mListener;
    private SendJsonToServer mJsonVertical;
    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                mVerticalBean.setElevationAngular(progress * 10);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //回调发送给activity
            onButtonPressed(mJsonVertical);
        }
    };
    private List<SendJsonToServer.ShootBean> mVerticalShoot;
    private SendJsonToServer.ShootBean mVerticalBean;

    public VerticalFragment() {
        // Required empty public constructor
    }

    /**
     * newInstance
     *
     * @param index
     * @param name
     * @return
     */
    public static VerticalFragment newInstance(String index, String name) {
        VerticalFragment fragment = new VerticalFragment();
        Bundle args = new Bundle();
        args.putString(INDEX, index);
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVerticalFragmentListener) {
            mListener = (OnVerticalFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVerticalFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getString(INDEX);
            mName = getArguments().getString(NAME);
        }
        mJsonVertical = new SendJsonToServer();
        mVerticalShoot = new ArrayList<>();
        mVerticalBean = new SendJsonToServer.ShootBean();
        mJsonVertical.setType(Constant.MODE_SHOOT);
        mVerticalBean.setFrequency(mVertical.get(0));
        mVerticalBean.setDistance(mVertical.get(1));
        mVerticalBean.setElevationAngular(mVertical.get(2));
        mVerticalBean.setHorizontalAngular(mVertical.get(3));
        mVerticalShoot.add(mVerticalBean);
        mJsonVertical.setShoot(mVerticalShoot);
        onButtonPressed(mJsonVertical);
    }

    public void onButtonPressed(SendJsonToServer vertical) {
        if (mListener != null) {
            mListener.onVerticalFragment(vertical);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sbs_fragment_vertical, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private void initView() {
        mSeekBarVertical.setOnSeekBarChangeListener(mSeekBarChangeListener);
    }

    /**
     * 回调
     */
    public interface OnVerticalFragmentListener {
        /**
         * Fragment回调
         *
         * @param vertical
         */
        void onVerticalFragment(SendJsonToServer vertical);
    }
}
