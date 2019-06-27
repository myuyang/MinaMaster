package vitaliqp.shootballscreen.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * 类名： HorizontalFragment
 * 时间：2019/4/15 下午1:58
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class HorizontalFragment extends BaseFragment {

    private static final String INDEX = "1";
    private static final String NAME = "horizontalFragment";
    private static SparseIntArray mMHorizontal;

    static {
        mMHorizontal = new SparseIntArray();
        mMHorizontal.append(0, 3500);
        mMHorizontal.append(1, -1);
        mMHorizontal.append(2, -1);
        mMHorizontal.append(3, 0);
    }

    Unbinder unbinder;
    @BindView(R.id.tv_fragment_horizontal_content)
    TextView mTvFragmentHorizontalContent;
    @BindView(R.id.seek_bar_horizontal)
    SeekBar mSeekBarHorizontal;
    private SendJsonToServer mJsonHorizontal;
    private List<SendJsonToServer.ShootBean> mHorizontalShoot;
    private SendJsonToServer.ShootBean mHorizontalBean;
    private String mIndex;
    private String mName;
    private OnHorizontalFragmentListener mListener;
    private SeekBar.OnSeekBarChangeListener mSeekBarHorizontalChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mHorizontalBean.setHorizontalAngular(progress * 10);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //回调数据给activity
            onButtonPressed(mJsonHorizontal);
        }
    };

    public HorizontalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param index Parameter 1.
     * @param name  Parameter 2.
     * @return A new instance of fragment HorizontalFragment.
     */
    public static HorizontalFragment newInstance(String index, String name) {
        HorizontalFragment fragment = new HorizontalFragment();
        Bundle args = new Bundle();
        args.putString(INDEX, index);
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHorizontalFragmentListener) {
            mListener = (OnHorizontalFragmentListener) context;
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

        initData();
    }

    private void initData() {
        mJsonHorizontal = new SendJsonToServer();
        mHorizontalShoot = new ArrayList<>();
        mHorizontalBean = new SendJsonToServer.ShootBean();
        mJsonHorizontal.setType(Constant.MODE_SHOOT);
        mHorizontalBean.setFrequency(mMHorizontal.get(0));
        mHorizontalBean.setDistance(mMHorizontal.get(1));
        mHorizontalBean.setElevationAngular(mMHorizontal.get(2));
        mHorizontalBean.setHorizontalAngular(mMHorizontal.get(3));
        mHorizontalShoot.add(mHorizontalBean);
        mJsonHorizontal.setShoot(mHorizontalShoot);
        onButtonPressed(mJsonHorizontal);
    }

    public void onButtonPressed(SendJsonToServer horizontal) {
        if (mListener != null) {
            mListener.onHorizontalFragment(horizontal);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sbs_fragment_horizontal, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mSeekBarHorizontal.setOnSeekBarChangeListener(mSeekBarHorizontalChangeListener);
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
    public interface OnHorizontalFragmentListener {
        /**
         * Fragment回调
         *
         * @param horizontal
         */
        void onHorizontalFragment(SendJsonToServer horizontal);
    }
}
