package vitaliqp.shootballscreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import vitaliqp.shootballscreen.R;
import vitaliqp.shootballscreen.base.BaseBasicActivity;
import vitaliqp.shootballscreen.datas.Constant;

/**
 * @author qp
 */
public class SplashActivity extends BaseBasicActivity {


    @Override
    public void initData(Bundle bundle) {
    }

    @Override
    public int bindLayout() {
        return R.layout.sbs_activity_splash;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @Override
    public void doBusiness() {
        finishSplashToMain();
    }

    @Override
    public void onWidgetClick(View view) {

    }

    private void finishSplashToMain() {
        final Intent intent = new Intent(this, MainActivity.class);

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        executorService.schedule(() -> {
            startActivity(intent);
            finish();
            executorService.shutdown();
        }, Constant.SPLASH_TIME_MILLISECONDS, TimeUnit.MILLISECONDS);
    }
}
