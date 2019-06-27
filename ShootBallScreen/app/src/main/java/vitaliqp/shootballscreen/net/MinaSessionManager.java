package vitaliqp.shootballscreen.net;

import com.blankj.utilcode.util.LogUtils;

import org.apache.mina.core.session.IoSession;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/9 下午2:06
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class MinaSessionManager {
    private static MinaSessionManager mInstance = null;

    private IoSession mSession;

    public MinaSessionManager(IoSession session) {
        mSession = session;
    }

    public MinaSessionManager() {
    }

    public static MinaSessionManager getInstance() {
        if (mInstance == null) {
            synchronized (MinaSessionManager.class) {
                if (mInstance == null) {
                    mInstance = new MinaSessionManager();
                }
            }
        }
        return mInstance;
    }

    public void setSession(IoSession session) {
        LogUtils.d("session = [" + session + "]");
        mSession = session;
    }

    public void writeToServer(Object msg) {
        if (mSession != null) {
            LogUtils.d("开始发送数据到服务端");
            mSession.write(msg);
        } else {
            LogUtils.d("session为空");
        }
    }

    public void closeSession() {
        if (mSession != null) {
            mSession.closeOnFlush();
        }
    }

    public void removeSession() {
        this.mSession = null;
    }
}
