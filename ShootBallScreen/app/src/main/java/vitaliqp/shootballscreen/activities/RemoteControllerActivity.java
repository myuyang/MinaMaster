package vitaliqp.shootballscreen.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import vitaliqp.shootballscreen.enums.JoystickViewCallBackMode;
import vitaliqp.shootballscreen.events.MessageControlStartEvent;
import vitaliqp.shootballscreen.net.MinaSessionManager;
import vitaliqp.shootballscreen.views.CustomJoystickView;

/**
 * @author qp
 */
public class RemoteControllerActivity extends BaseActivity {

    @BindView(R.id.btn_activity_remote_up)
    Button mBtnActivityRemoteUp;
    @BindView(R.id.btn_activity_remote_down)
    Button mBtnActivityRemoteDown;
    @BindView(R.id.btn_activity_remote_left)
    Button mBtnActivityRemoteLeft;
    @BindView(R.id.btn_activity_remote_right)
    Button mBtnActivityRemoteRight;
    @BindView(R.id.btn_activity_remote_y)
    Button mBtnActivityRemoteY;
    @BindView(R.id.btn_activity_remote_x)
    Button mBtnActivityRemoteX;
    @BindView(R.id.btn_activity_remote_a)
    Button mBtnActivityRemoteA;
    @BindView(R.id.btn_activity_remote_b)
    Button mBtnActivityRemoteB;
    @BindView(R.id.cjv_activity_remote_left)
    CustomJoystickView mCjvActivityRemoteLeft;
    @BindView(R.id.cjv_activity_remote_right)
    CustomJoystickView mCjvActivityRemoteRight;
    @BindView(R.id.btn_activity_back)
    Button mBtnActivityBack;
    @BindView(R.id.tv_activity_title)
    TextView mTvActivityTitle;
    @BindView(R.id.btn_activity_start)
    Button mBtnActivityStart;
    private CustomJoystickView mJoystickLeft;
    private CustomJoystickView mJoystickRight;
    private JoystickDirection mLeftDirection = JoystickDirection.DIRECTION_CENTER;
    private JoystickDirection mRightDirection = JoystickDirection.DIRECTION_CENTER;
    private int mLeftActualDistance;
    private int mRightActualDistance;
    private int mLinSpeed;
    private int mAngSpeed;
    private boolean mIsStart;
    private boolean mIsLeftJoyStart;
    private boolean mIsRightJoyStart;
    private boolean mIsUpStart;
    private boolean mIsDownStart;
    private boolean mIsLeftStart;
    private boolean mIsRightStart;
    private SendJsonToServer mRemoteJson;
    private ArrayList<SendJsonToServer.RemoteBean> mRemoteArray;
    private SendJsonToServer.RemoteBean mRemoteBean;
    private ThreadUtils.Task<Boolean> mUpTask;
    private ThreadUtils.Task<Boolean> mDownTask;
    private ThreadUtils.Task<Boolean> mRightTask;
    private ThreadUtils.Task<Boolean> mLeftTask;
    private boolean mIsRollerStart;
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
    private ThreadUtils.Task<Boolean> mJoyLeftTask = new ThreadUtils.SimpleTask<Boolean>() {
        @Nullable
        @Override
        public Boolean doInBackground() throws Throwable {
            return true;
        }

        @Override
        public void onSuccess(@Nullable Boolean result) {

        }
    };
    private ThreadUtils.Task<Boolean> mJoyRightTask = new ThreadUtils.SimpleTask<Boolean>() {
        @Nullable
        @Override
        public Boolean doInBackground() throws Throwable {
            return true;
        }

        @Override
        public void onSuccess(@Nullable Boolean result) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sbs_activity_remote_controller);
        ButterKnife.bind(this);
        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        //获取屏幕密度比
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float density = metrics.density;
        mJoystickLeft = findViewById(R.id.cjv_activity_remote_left);
        mJoystickRight = findViewById(R.id.cjv_activity_remote_right);
        mTvActivityTitle.setText(R.string.activity_remote_title);
        initLeftJoystick(density);
        initRightJoystick(density);
        initDirectionUpLongClick();
        initDirectionDownLongClick();
        initDirectionLeftLongClick();
        initDirectionRightLongClick();
    }

    private void initData() {
        mRemoteJson = new SendJsonToServer();
        mRemoteArray = new ArrayList<>();
        mRemoteBean = new SendJsonToServer.RemoteBean();
        mRemoteJson.setType(Constant.MODE_REMOTE);
        mRemoteBean.setLin(0);
        mRemoteBean.setAng(0);
        mRemoteBean.setRoller(0);
        mRemoteArray.add(mRemoteBean);
        mRemoteJson.setRemote(mRemoteArray);
    }

    private void initLeftJoystick(float density) {
        mJoystickLeft.setJoystickViewCallBackMode(JoystickViewCallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        mJoystickLeft.setOnShakeListener(JoystickDirectionMode.DIRECTION_2_VERTICAL, new CustomJoystickView.OnShakeListener() {
            @Override
            public void onStart() {
                mLeftDirection = JoystickDirection.DIRECTION_CENTER;
            }

            @Override
            public void direction(JoystickDirection joystickDirection) {
                mLeftDirection = joystickDirection;
            }

            @Override
            public void onFinish() {
                mLeftDirection = JoystickDirection.DIRECTION_CENTER;
            }
        });

        mJoystickLeft.setOnDistanceChangeListener(new CustomJoystickView.OnDistanceChangeListener() {
            long last;

            @Override
            public void onStart() {
                if (mIsStart) {
                    mIsLeftJoyStart = true;
                }
            }

            @Override
            public void distance(int distance, int distanceX, int distanceY) {

                if (!mIsStart) {
                    return;
                }

                int actualY = (int) (distanceY / density);

                if (mLeftDirection == JoystickDirection.DIRECTION_UP) {
                    mLeftActualDistance = actualY <= 0 ? -1 * actualY : actualY;
                } else {
                    mLeftActualDistance = actualY <= 0 ? actualY : actualY * -1;
                }
                long now = System.currentTimeMillis();
                if (now - last > Constant.COMMAND_FREQUENCY) {
                    last = now;
                    mRemoteBean.setLin(mLeftActualDistance * 10);
                    sendCommandToServer(0);
                }
            }

            @Override
            public void onFinish() {
                if (mIsStart) {
                    mIsLeftJoyStart = false;
                    mRemoteBean.setLin(0);
                    sendCommandToServer(0);
                }
            }
        });
    }

    private void initRightJoystick(float density) {

        mJoystickRight.setJoystickViewCallBackMode(JoystickViewCallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        mJoystickRight.setOnShakeListener(JoystickDirectionMode.DIRECTION_2_HORIZONTAL, new CustomJoystickView.OnShakeListener() {
            @Override
            public void onStart() {
                mRightDirection = JoystickDirection.DIRECTION_CENTER;
            }

            @Override
            public void direction(JoystickDirection joystickDirection) {
                mRightDirection = joystickDirection;
            }

            @Override
            public void onFinish() {
                mRightDirection = JoystickDirection.DIRECTION_CENTER;
            }
        });

        mJoystickRight.setOnDistanceChangeListener(new CustomJoystickView.OnDistanceChangeListener() {
            long last;

            @Override
            public void onStart() {
                if (mIsStart) {
                    mIsRightJoyStart = true;
                }
            }

            @Override
            public void distance(int distance, int distanceX, int distanceY) {

                if (!mIsStart) {
                    return;
                }

                int actualX = (int) (distanceX / density);

                if (mRightDirection == JoystickDirection.DIRECTION_RIGHT) {
                    mRightActualDistance = actualX >= 0 ? actualX * -1 : actualX;
                } else {
                    mRightActualDistance = actualX >= 0 ? actualX : actualX * -1;
                }
                long now = System.currentTimeMillis();
                if (now - last > Constant.COMMAND_FREQUENCY) {
                    mRemoteBean.setAng(mRightActualDistance);
                    last = now;
                    sendCommandToServer(0);
                }
            }

            @Override
            public void onFinish() {
                if (mIsStart) {
                    mIsRightJoyStart = false;
                    mRemoteBean.setAng(0);
                    sendCommandToServer(0);
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")    //setOnTouchListener导致selector不起作用，代码中增加点击效果
    private void initDirectionUpLongClick() {
        mBtnActivityRemoteUp.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBtnActivityRemoteUp.setBackgroundResource(R.drawable.sbs_activity_remote_up_pressed);
                    if (mIsStart) {
                        if (mIsLeftJoyStart) {
                            mIsUpStart = true;
                            mUpTask = new ThreadUtils.SimpleTask<Boolean>() {
                                @Nullable
                                @Override
                                public Boolean doInBackground() throws Throwable {
                                    mLinSpeed = mLinSpeed >= 120 ? 120 : mLinSpeed + 10;
//                            mRemoteBean.setLin(mLinSpeed);
//                            MinaSessionManager.getInstance().writeToServer(mRemoteJson);
                                    LogUtils.i(mLinSpeed);
                                    return true;
                                }

                                @Override
                                public void onSuccess(@Nullable Boolean result) {

                                }
                            };
                            ThreadUtils.executeByFixedAtFixRate(10, mUpTask, 1000, TimeUnit.MILLISECONDS);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mBtnActivityRemoteUp.setBackgroundResource(R.drawable.sbs_activity_remote_up_normal);
                    if (mIsStart) {
                        if (mIsLeftJoyStart) {
                            mIsUpStart = false;
                            ThreadUtils.cancel(mUpTask);
                            mLinSpeed = 0;
                            //TODO:SEND COMMAND TO SERVER TO STOP
                        }
                    }
                    break;
                default:
                    //do nothing
                    break;
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDirectionDownLongClick() {
        mBtnActivityRemoteDown.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBtnActivityRemoteDown.setBackgroundResource(R.drawable.sbs_activity_remote_down_pressed);
                    if (mIsStart) {
                        if (!mIsLeftStart) {
                            mIsDownStart = true;
                            mDownTask = new ThreadUtils.SimpleTask<Boolean>() {
                                @Nullable
                                @Override
                                public Boolean doInBackground() throws Throwable {
                                    mLinSpeed = mLinSpeed <= -120 ? -120 : mLinSpeed - 10;
//                            mRemoteBean.setLin(mLinSpeed);
//                            MinaSessionManager.getInstance().writeToServer(mRemoteJson);
                                    LogUtils.i(mLinSpeed);
                                    return true;
                                }

                                @Override
                                public void onSuccess(@Nullable Boolean result) {

                                }
                            };
                            ThreadUtils.executeByFixedAtFixRate(10, mDownTask, 1000, TimeUnit.MILLISECONDS);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mBtnActivityRemoteDown.setBackgroundResource(R.drawable.sbs_activity_remote_down_normal);
                    if (mIsStart) {
                        if (mIsLeftJoyStart) {
                            mIsDownStart = false;
                            ThreadUtils.cancel(mDownTask);
                            mLinSpeed = 0;
                            //TODO:SEND COMMAND TO SERVER TO STOP
                        }
                    }
                    break;
                default:
                    //do nothing
                    break;
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDirectionLeftLongClick() {
        mBtnActivityRemoteLeft.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBtnActivityRemoteLeft.setBackgroundResource(R.drawable.sbs_activity_remote_left_pressed);
                    if (mIsStart) {
                        if (!mIsRightJoyStart) {
                            mIsLeftStart = true;
                            mLeftTask = new ThreadUtils.SimpleTask<Boolean>() {
                                @Nullable
                                @Override
                                public Boolean doInBackground() throws Throwable {
                                    mAngSpeed = mAngSpeed <= -60 ? -60 : mAngSpeed - 5;
//                            mRemoteBean.setLin(mAngSpeed);
//                            mRemoteArray.add(mRemoteBean);
//                            MinaSessionManager.getInstance().writeToServer(mRemoteJson);
                                    LogUtils.i(mAngSpeed);
                                    return true;
                                }

                                @Override
                                public void onSuccess(@Nullable Boolean result) {

                                }
                            };
                            ThreadUtils.executeByFixedAtFixRate(10, mLeftTask, 1000, TimeUnit.MILLISECONDS);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mBtnActivityRemoteLeft.setBackgroundResource(R.drawable.sbs_activity_remote_left_normal);
                    mIsLeftStart = false;
                    ThreadUtils.cancel(mLeftTask);
                    mAngSpeed = 0;
                    //TODO:SEND COMMAND TO SERVER TO STOP
                    break;
                default:
                    //do nothing
                    break;
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initDirectionRightLongClick() {
        mBtnActivityRemoteRight.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBtnActivityRemoteRight.setBackgroundResource(R.drawable.sbs_activity_remote_right_pressed);
                    if (mIsStart) {
                        if (!mIsRightJoyStart) {
                            mIsRightStart = true;
                            mRightTask = new ThreadUtils.SimpleTask<Boolean>() {
                                @Nullable
                                @Override
                                public Boolean doInBackground() throws Throwable {
                                    mAngSpeed = mAngSpeed >= 60 ? 60 : mAngSpeed + 5;
//                            mRemoteBean.setLin(mAngSpeed);
//                            mRemoteArray.add(mRemoteBean);
//                            MinaSessionManager.getInstance().writeToServer(mRemoteJson);
                                    LogUtils.i(mAngSpeed);
                                    return true;
                                }

                                @Override
                                public void onSuccess(@Nullable Boolean result) {

                                }
                            };
                            ThreadUtils.executeByFixedAtFixRate(10, mRightTask, 1000, TimeUnit.MILLISECONDS);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mBtnActivityRemoteRight.setBackgroundResource(R.drawable.sbs_activity_remote_right_normal);
                    if (mIsStart) {
                        if (!mIsRightJoyStart) {
                            mIsRightStart = false;
                            ThreadUtils.cancel(mRightTask);
                            mAngSpeed = 0;
                            //TODO:SEND COMMAND TO SERVER TO STOP
                        }
                    }
                    break;
                default:
                    // do nothing
                    break;
            }
            return true;
        });
    }

    private void sendCommandToServer(long delay) {
        ThreadUtils.executeByFixedWithDelay(1024, mSendTask, delay, TimeUnit.MILLISECONDS);
    }

    @OnClick({R.id.btn_activity_back, R.id.btn_activity_remote_up, R.id.btn_activity_remote_down,
            R.id.btn_activity_remote_left, R.id.btn_activity_remote_right, R.id.btn_activity_remote_y,
            R.id.btn_activity_remote_x, R.id.btn_activity_remote_a, R.id.btn_activity_remote_b, R.id.btn_activity_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_back:
                back();
                break;
            case R.id.btn_activity_start:
                startRemote();
                break;
            case R.id.btn_activity_remote_up:
                break;
            case R.id.btn_activity_remote_down:
                break;
            case R.id.btn_activity_remote_left:
                break;
            case R.id.btn_activity_remote_right:
                break;
            case R.id.btn_activity_remote_y:
                break;
            case R.id.btn_activity_remote_x:
                break;
            case R.id.btn_activity_remote_a:
                startRoller();
                break;
            case R.id.btn_activity_remote_b:
                break;
            default:
                //do nothing
                break;
        }
    }

    private void back() {

        ActivityUtils.finishActivity(RemoteControllerActivity.class);
    }

    private void startRemote() {
        if (!isFastClick()) {
            mIsStart = !mIsStart;
            mRemoteJson.setState(mIsStart ? 1 : 2);
            mBtnActivityStart.setBackgroundResource(mIsStart ? R.mipmap.sbs_activity_remote_start_pressed : R.mipmap.sbs_activity_remote_start_normal);
            sendCommandToServer(0);
        }
    }

    private void startRoller() {
        if (mIsStart){
            mIsRollerStart = !mIsRollerStart;
            mRemoteBean.setRoller(mIsRollerStart ? 1 : 0);
            sendCommandToServer(0);
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
}
