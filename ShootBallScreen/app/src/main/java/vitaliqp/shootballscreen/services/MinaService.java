package vitaliqp.shootballscreen.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;

import java.util.concurrent.TimeUnit;

import vitaliqp.shootballscreen.datas.Constant;
import vitaliqp.shootballscreen.net.MinaConnectionConfig;
import vitaliqp.shootballscreen.net.MinaConnectionManager;

/**
 * 类名：vitaliqp.shootballscreen.services
 * 时间：2019/4/9 上午9:11
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class MinaService extends Service {

    private MinaConnectionManager mManager;

    private ThreadUtils.Task<Boolean> mConnectTask = new ThreadUtils.Task<Boolean>() {
        @Nullable
        @Override
        public Boolean doInBackground() throws Throwable {
            boolean connect = mManager.connect();
            LogUtils.d("连接：" + connect);
            return connect;
        }

        @Override
        public void onSuccess(@Nullable Boolean result) {
            if (result) {
                LogUtils.d("连接成功，关闭连接线程");
                ThreadUtils.cancel(mConnectTask);

            } else {
                //打印日志
                LogUtils.d("连接失败，启动下一次连接");
            }
        }

        @Override
        public void onCancel() {
            //连接服务已取消
            LogUtils.d("连接任务取消");
        }

        @Override
        public void onFail(Throwable t) {
            //连接失败
            LogUtils.d("连接任务失败");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        initConfig();
    }

    private void initConfig() {
        MinaConnectionConfig config = new MinaConnectionConfig.Builder(getApplicationContext())
                //test
//                .setIp("192.168.31.155")
                //test
//                .setPort(8989)
//                .setIp("192.168.0.102")
//                .setPort(33000)
//                .setReadBufferSize(2048)
//                .setConnectTimeout(3000)
                .builder();
        LogUtils.d("初始化config:" + config.getIp() + "-" + config.getPort());
        mManager = new MinaConnectionManager(config);
        ThreadUtils.executeByFixedAtFixRate(1, mConnectTask, Constant.CONNECT_TIME, TimeUnit.MILLISECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mManager != null) {
            mManager.disconnect();
        }

        if (mConnectTask != null) {
            ThreadUtils.cancel(mConnectTask);
            mConnectTask = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
