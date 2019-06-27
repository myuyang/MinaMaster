package vitaliqp.shootballscreen.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vitaliqp.commonutils.CommonGsonUtils;
import vitaliqp.shootballscreen.R;
import vitaliqp.shootballscreen.datas.Constant;
import vitaliqp.shootballscreen.datas.SendJsonToServer;
import vitaliqp.shootballscreen.events.MessageControlStartEvent;
import vitaliqp.shootballscreen.fragments.FixedPointFragment;
import vitaliqp.shootballscreen.fragments.HorizontalFragment;
import vitaliqp.shootballscreen.fragments.VerticalFragment;
import vitaliqp.shootballscreen.net.MinaSessionManager;

/**
 * @author qp
 */
public class ShootBallActivity extends BaseActivity implements FixedPointFragment.OnFixedPointFragmentListener, VerticalFragment.OnVerticalFragmentListener, HorizontalFragment.OnHorizontalFragmentListener {

    @BindView(R.id.btn_activity_back)
    Button mConfigBtnBack;
    @BindView(R.id.tv_activity_shoot_fixed)
    TextView mTvActivityShootFixed;
    @BindView(R.id.tv_activity_shoot_horizontal)
    TextView mTvActivityShootHorizontal;
    @BindView(R.id.tv_activity_shoot_vertical)
    TextView mTvActivityShootVertical;
    @BindView(R.id.config_ll_navigation)
    LinearLayout mConfigRlNavigation;
    @BindView(R.id.config_fl_shoot_ball)
    FrameLayout mConfigFlShootBall;
    @BindView(R.id.tv_activity_shoot_title)
    TextView mTvActivityTitle;
    @BindView(R.id.btn_activity_shoot_start)
    Button mBtnActivityShootStart;

    private FragmentManager mManager;
    private Fragment mFragment;
    private Fragment[] mFragments = new Fragment[3];
    private String[] tags = new String[]{Constant.FIXEDPOINT, Constant.HORIZONTAL, Constant.VERTICAL};
    private int curIndex;
    private boolean mIsFixed = true;
    private boolean mIsHorizontal;
    private boolean mIsVertical;
    private boolean mStartShootBall;
    private SendJsonToServer mJsonToServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sbs_activity_shoot_ball);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
        checkState(savedInstanceState);
    }

    private void initView() {
        mTvActivityTitle.setText(R.string.activity_shoot_title);
        mManager = getSupportFragmentManager();
        mFragments[0] = FixedPointFragment.newInstance("0", tags[0]);
        mFragments[1] = HorizontalFragment.newInstance("1", tags[1]);
        mFragments[2] = VerticalFragment.newInstance("2", tags[2]);
    }

    private void initData() {

        mJsonToServer = new SendJsonToServer();


    }

    private void checkState(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            FragmentTransaction transaction = mManager.beginTransaction();
            mFragment = mFragments[0];
            transaction.add(R.id.config_fl_shoot_ball, mFragment);
            transaction.commit();
        } else {
            FixedPointFragment fixedPointFragment =
                    (FixedPointFragment) getSupportFragmentManager().findFragmentByTag(tags[0]);
            HorizontalFragment horizontalFragment =
                    (HorizontalFragment) getSupportFragmentManager().findFragmentByTag(tags[1]);
            VerticalFragment verticalFragment =
                    (VerticalFragment) getSupportFragmentManager().findFragmentByTag(tags[2]);
            getSupportFragmentManager().beginTransaction()
                    .show(fixedPointFragment)
                    .hide(verticalFragment)
                    .hide(horizontalFragment)
                    .commit();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("curIndex", curIndex);
    }

    @OnClick({R.id.tv_activity_shoot_fixed, R.id.tv_activity_shoot_horizontal,
            R.id.tv_activity_shoot_vertical, R.id.btn_activity_back, R.id.btn_activity_shoot_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_activity_shoot_fixed:
                if (mIsFixed) {
                    return;
                }
                mIsFixed = true;
                mIsVertical = false;
                mIsHorizontal = false;
                setSelectItem();
                switchFragment(mFragment, mFragments[0], 0);
                break;
            case R.id.tv_activity_shoot_horizontal:
                if (mIsHorizontal) {
                    return;
                }
                mIsHorizontal = true;
                mIsVertical = false;
                mIsFixed = false;
                setSelectItem();
                switchFragment(mFragment, mFragments[1], 1);
                break;
            case R.id.tv_activity_shoot_vertical:
                if (mIsVertical) {
                    return;
                }
                mIsVertical = true;
                mIsFixed = false;
                mIsHorizontal = false;
                setSelectItem();
                switchFragment(mFragment, mFragments[2], 2);
                break;
            case R.id.btn_activity_back:
                back();
                break;
            case R.id.btn_activity_shoot_start:
                startShootBall();
                break;
        }
    }

    private void setSelectItem() {

        if (mIsFixed) {
            mTvActivityShootFixed.setTextColor(Color.BLACK);
        } else {
            mTvActivityShootFixed.setTextColor(Color.GRAY);
        }

        if (mIsHorizontal) {
            mTvActivityShootHorizontal.setTextColor(Color.BLACK);
        } else {
            mTvActivityShootHorizontal.setTextColor(Color.GRAY);
        }

        if (mIsVertical) {
            mTvActivityShootVertical.setTextColor(Color.BLACK);
        } else {
            mTvActivityShootVertical.setTextColor(Color.GRAY);
        }
    }

    private void switchFragment(Fragment from, Fragment to, int index) {

        if (mFragment != to) {
            mFragment = to;
            FragmentTransaction transaction = mManager.beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(from)
                        .add(R.id.config_fl_shoot_ball, to, tags[index])
                        .commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

    private void back() {
        ActivityUtils.finishActivity(ShootBallActivity.class);
    }

    private void startShootBall() {
        if (!isFastClick()) {
            mStartShootBall = !mStartShootBall;
            mBtnActivityShootStart.setBackgroundResource(
                    mStartShootBall ? R.mipmap.sbs_activity_remote_start_pressed : R.mipmap.sbs_activity_remote_start_normal);
            mJsonToServer.setState(mStartShootBall ? 1 : 2);

            if (mIsHorizontal) {
                ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<Boolean>() {
                    @Nullable
                    @Override
                    public Boolean doInBackground() throws Throwable {
                        return sendCommandToServer();
                    }

                    @Override
                    public void onSuccess(@Nullable Boolean result) {
                        LogUtils.i("Horizontal send a command ");
                    }
                });
            }
        }

        if (mIsFixed) {
            ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<Boolean>() {
                @Nullable
                @Override
                public Boolean doInBackground() throws Throwable {
                    return sendCommandToServer();
                }

                @Override
                public void onSuccess(@Nullable Boolean result) {
                    LogUtils.i("Fixed send a command ");
                }
            });
        }

        if (mIsVertical) {
            ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<Boolean>() {
                @Nullable
                @Override
                public Boolean doInBackground() throws Throwable {
                    return sendCommandToServer();
                }

                @Override
                public void onSuccess(@Nullable Boolean result) {
                    LogUtils.i("Vertical send a command ");
                }
            });
        }
    }

    private boolean sendCommandToServer() {
        if (mJsonToServer == null) {
            return false;
        }
        String convert = CommonGsonUtils.getGson().toJson(mJsonToServer);
        MinaSessionManager.getInstance().writeToServer(convert);
        Log.i("1111111111111111111", convert);
        LogUtils.i("send:" + convert);
        return true;
    }

    @Override
    public void onVerticalFragment(SendJsonToServer vertical) {
        mJsonToServer = vertical;
        LogUtils.i("vertical");
    }

    @Override
    public void onHorizontalFragment(SendJsonToServer horizontal) {

        mJsonToServer = horizontal;
        LogUtils.i("horizontal");
    }

    @Override
    public void onFixedPointFragment(SendJsonToServer fixed) {
        mJsonToServer = fixed;
        LogUtils.i("Fixed");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageIstart(MessageControlStartEvent messageControlStartEvent) {
        if (!messageControlStartEvent.istart()) {
            back();
        }
    }
}
