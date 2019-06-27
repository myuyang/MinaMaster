package vitaliqp.shootballscreen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vitaliqp.commonutils.CommonGsonUtils;
import vitaliqp.shootballscreen.R;
import vitaliqp.shootballscreen.datas.Constant;
import vitaliqp.shootballscreen.datas.SendJsonToServer;
import vitaliqp.shootballscreen.enums.JoystickDirection;
import vitaliqp.shootballscreen.enums.JoystickDirectionMode;
import vitaliqp.shootballscreen.events.MessageControlStartEvent;
import vitaliqp.shootballscreen.fragments.AreaBallFragment;
import vitaliqp.shootballscreen.fragments.SmartBallFragment;
import vitaliqp.shootballscreen.net.MinaSessionManager;

/**
 * 类名： AutoControllerActivity
 * 时间：2019/3/14 17:15
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author QP
 */
public class AutoControllerActivity extends BaseActivity
        implements SmartBallFragment.OnSmartFragmentListener, AreaBallFragment.OnAreaFragmentListener {

    //*

    private SendJsonToServer mRemoteJson;
    private boolean mIsStart;

    //*


    @BindView(R.id.btn_activity_auto_back)
    Button mBtnActivityAutoBack;
    @BindView(R.id.tv_activity_auto_title)
    TextView mTvActivityAutoTitle;
    @BindView(R.id.btn_activity_auto_start)
    Button mBtnActivityAutoStart;
    @BindView(R.id.fl_auto_content)
    FrameLayout mFlAutoContent;
    @BindView(R.id.bnv_activity_auto_bottom)
    BottomNavigationView mBnvActivityAutoBottom;
    private FragmentManager mManager;
    private Fragment mFragment;
    private Fragment[] mFragments = new Fragment[2];
    private String[] tags = new String[]{Constant.SMARRT, Constant.AREA};
    private SendJsonToServer mAutoJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sbs_activity_auto_controller);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
        checkState(savedInstanceState);
    }

    private void initView() {

        mManager = getSupportFragmentManager();
        mFragments[0] = SmartBallFragment.newInstance("0", tags[0]);
        mFragments[1] = AreaBallFragment.newInstance("1", tags[1]);

        mBnvActivityAutoBottom.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_action_smart:
                    return true;
                case R.id.item_action_area:
                    return true;
                default:
                    //do nothing
                    break;
            }
            return false;
        });
    }

    private void initData() {
        //*

        mRemoteJson = new SendJsonToServer();
        mRemoteJson.setType(Constant.MODE_AUTO);
        //*
    }

    private void checkState(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            FragmentTransaction transaction = mManager.beginTransaction();
            mFragment = mFragments[0];
            transaction.add(R.id.fl_auto_content, mFragment);
            transaction.commit();
        } else {
            SmartBallFragment smartBallFragment =
                    (SmartBallFragment) getSupportFragmentManager().findFragmentByTag(tags[0]);
            AreaBallFragment areaBallFragment =
                    (AreaBallFragment) getSupportFragmentManager().findFragmentByTag(tags[1]);
            getSupportFragmentManager().beginTransaction()
                    .show(smartBallFragment)
                    .hide(areaBallFragment)
                    .commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageIstart(MessageControlStartEvent messageControlStartEvent) {
        if (!messageControlStartEvent.istart()) {
            back();
        }
    }

    private void back() {
        ActivityUtils.finishActivity(AutoControllerActivity.class);
    }

    private void sendCommandToServer() {
        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<Boolean>() {
            @Nullable
            @Override
            public Boolean doInBackground() throws Throwable {
                if (mAutoJson == null) {
                    return false;
                }
                String convert = CommonGsonUtils.getGson().toJson(mAutoJson);
                LogUtils.i("send command ---" + convert);
                MinaSessionManager.getInstance().writeToServer(convert);
                return true;
            }

            @Override
            public void onSuccess(@Nullable Boolean result) {

            }
        });


    }

    @OnClick({R.id.btn_activity_auto_back, R.id.btn_activity_auto_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_auto_back:
                back();
                break;
            case R.id.btn_activity_auto_start:
                //*
                autoCollect();
                //*
                break;
            default:
                // do nothing
                break;
        }
    }

    @Override
    public void onSmartFragment(SendJsonToServer smart) {
        if (smart != null) {
            mAutoJson = smart;
        }
    }

    @Override
    public void onAreaFragment(SendJsonToServer area) {

    }


    //*
    private ThreadUtils.Task<Boolean> mSendTask = new ThreadUtils.SimpleTask<Boolean>() {
        @Nullable
        @Override
        public Boolean doInBackground() throws Throwable {
            if (mRemoteJson == null) {
                return false;
            }
            String convert = CommonGsonUtils.getGson().toJson(mRemoteJson);
            LogUtils.i("send command ---" + convert);
            MinaSessionManager.getInstance().writeToServer(convert);
            return true;
        }

        @Override
        public void onSuccess(@Nullable Boolean result) {

        }
    };

    private void autoCollect() {
        if (!isFastClick()) {
            mIsStart = !mIsStart;
            mRemoteJson.setState(mIsStart ? 1 : 2);
            mBtnActivityAutoStart.setBackgroundResource(mIsStart ? R.mipmap.sbs_activity_remote_start_pressed : R.mipmap.sbs_activity_remote_start_normal);
            ThreadUtils.executeByFixedWithDelay(1024, mSendTask, 0, TimeUnit.MILLISECONDS);
        }
    }
    //*

}
