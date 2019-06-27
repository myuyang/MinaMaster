package vitaliqp.shootballscreen.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vitaliqp.commonutils.CommonGsonUtils;
import vitaliqp.shootballscreen.R;
import vitaliqp.shootballscreen.datas.Constant;
import vitaliqp.shootballscreen.datas.MinaControlMode;
import vitaliqp.shootballscreen.datas.SendJsonToServer;
import vitaliqp.shootballscreen.net.MinaSessionManager;
import vitaliqp.shootballscreen.services.MinaService;

import static vitaliqp.shootballscreen.datas.Constant.SOCKET_SWITCH;

/**
 * @author qp
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.ibn_activity_main_collect)
    ImageButton mIbnActivityMainCollect;
    @BindView(R.id.ibn_activity_main_auto)
    ImageButton mIbnActivityMainAuto;
    @BindView(R.id.ibn_activity_main_shoot)
    ImageButton mIbnActivityMainShoot;
    @BindView(R.id.tv_activity_main_remote)
    TextView mTvActivityMainRemote;
    @BindView(R.id.tv_activity_main_auto)
    TextView mTvActivityMainAuto;
    @BindView(R.id.tv_activity_main_shoot)
    TextView mTvActivityMainShoot;
    private AnimatorSet mAnimatorSet;
    private SendJsonToServer mJson;
    private ArrayList<SendJsonToServer.RemoteBean> mRemoteBeans;
    private ArrayList<SendJsonToServer.AutoBean> mAutoBeans;
    private ArrayList<SendJsonToServer.ShootBean> mShootBeans;

    private ThreadUtils.Task<Boolean> mSendModeTask = new ThreadUtils.SimpleTask<Boolean>() {
        @Nullable
        @Override
        public Boolean doInBackground() throws Throwable {
            MinaSessionManager.getInstance().writeToServer(CommonGsonUtils.getGson().toJson(mJson));
            return true;
        }

        @Override
        public void onSuccess(@Nullable Boolean result) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sbs_activity_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mTvActivityMainRemote.bringToFront();
        mTvActivityMainAuto.bringToFront();
        mTvActivityMainShoot.bringToFront();

        //test
        mTvActivityMainRemote.setOnLongClickListener(v -> {
            JsonObject json = new JsonObject();
            json.addProperty("int", 200);
            json.addProperty("string", "baby");
            json.addProperty("double", 10.0121321312);
            MinaSessionManager.getInstance().writeToServer(new Gson().toJson(json));
            return true;
        });
    }

    private void initData() {
        mJson = new SendJsonToServer();
        mRemoteBeans = new ArrayList<>();
        SendJsonToServer.RemoteBean remoteBean = new SendJsonToServer.RemoteBean();
        remoteBean.setAng(0);
        remoteBean.setLin(0);
        remoteBean.setRoller(0);
        mRemoteBeans.add(remoteBean);
        mJson.setRemote(mRemoteBeans);

        mAutoBeans = new ArrayList<>();
        mShootBeans = new ArrayList<>();
        startAnimator();
    }

    private void startAnimator() {

        ObjectAnimator remote = ObjectAnimator.ofFloat(mIbnActivityMainCollect, "rotation", 0, 359);
        remote.setRepeatCount(ValueAnimator.INFINITE);
        remote.setRepeatMode(ValueAnimator.RESTART);
        ObjectAnimator auto = ObjectAnimator.ofFloat(mIbnActivityMainAuto, "rotation", 0, 359);
        auto.setRepeatCount(ValueAnimator.INFINITE);
        auto.setRepeatMode(ValueAnimator.RESTART);
        ObjectAnimator shoot = ObjectAnimator.ofFloat(mIbnActivityMainShoot, "rotation", 0, 359);
        shoot.setRepeatCount(ValueAnimator.INFINITE);
        shoot.setRepeatMode(ValueAnimator.RESTART);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(remote, auto, shoot);
        mAnimatorSet.setDuration(Constant.MAIN_BALL_ANIMATOR_DURATION);
        mAnimatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SOCKET_SWITCH) {
            Intent minaService = new Intent(this, MinaService.class);
            startService(minaService);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            mAnimatorSet.cancel();
//        EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.tv_activity_main_remote, R.id.tv_activity_main_auto, R.id.tv_activity_main_shoot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_activity_main_remote:
                startActivityMode(Constant.MODE_REMOTE);
                break;
            case R.id.tv_activity_main_auto:
                startActivityMode(Constant.MODE_AUTO);
                break;
            case R.id.tv_activity_main_shoot:
                startActivityMode(Constant.MODE_SHOOT);
                break;
            default:
                break;
        }
    }

    private void startActivityMode(@MinaControlMode int mode) {
        mJson.setType(mode);
        ThreadUtils.executeBySingle(mSendModeTask);

        switch (mode) {
            case Constant.MODE_REMOTE:
                ActivityUtils.startActivity(MainActivity.this, RemoteControllerActivity.class,
                        R.anim.sbs_fade_in_1000, R.anim.sbs_fade_out_1000);
                break;
            case Constant.MODE_AUTO:
                ActivityUtils.startActivity(MainActivity.this, AutoControllerActivity.class,
                        R.anim.sbs_fade_in_1000, R.anim.sbs_fade_out_1000);
                break;
            case Constant.MODE_SHOOT:
                ActivityUtils.startActivity(MainActivity.this, ShootBallActivity.class,
                        R.anim.sbs_fade_in_1000, R.anim.sbs_fade_out_1000);
                break;
            default:
                break;
        }
    }

    /**
     * Handler
     */
    private static class MainHandler extends Handler {

        private WeakReference<MainActivity> activityWeakReference;

        public MainHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    default:
                        break;
                }
            }
        }

    }
}
