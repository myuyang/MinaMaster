package vitaliqp.shootballscreen.net;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/8 下午1:14
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class MinaConnectionManager {
    public static final String TAG = "MinaConnectionManager";
    private final WeakReference<Context> mContext;
    private InetSocketAddress mAddress;
    private NioSocketConnector mConnector;
    private ConnectFuture mFuture;
    private MinaConnectionConfig mConfig;
    private IoSession mSession;

    public MinaConnectionManager(MinaConnectionConfig config) {
        mConfig = config;
        mContext = new WeakReference<>(config.getContext());
        init();
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(), mConfig.getPort());
        mConnector = new NioSocketConnector();
        mConnector.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
        mConnector.setConnectTimeoutMillis(mConfig.getConnectionTimeout());
        //设置日志过滤器
        mConnector.getFilterChain().addLast("logger", new LoggingFilter());
        //设置文本换行符编解码器
//        mConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        //TODO:设置心跳
        //设置协议封装解析处理

        mConnector.getFilterChain().addLast("protocol", new ProtocolCodecFilter
                (new CustomCodeFactory(new CustomDataEncoder(), new CustomDataDecoder())));
        //mConnector.getFilterChain().addLast("exceutor", new ExecutorFilter());

//        KeepAliveFilter keepAliveFilter = new KeepAliveFilter(new CustomHeartBeatMessageFactory());
        //设置每五分钟发送一个心跳包
//        keepAliveFilter.setRequestInterval(5 * 60);
        //设置心跳包超时时间为10s
//        keepAliveFilter.setRequestTimeout(10);

//        DefaultIoFilterChainBuilder chain = mConnector.getFilterChain();
        //添加编码过滤器 处理乱码编码问题
        //chain.addLast("encoder",new ProtocolCodecFilter();
        //chain.addLast("decoder",new ProtocolCodecFilter());
        mConnector.setHandler(new CustomServiceHandler(mContext.get()));
        mConnector.setDefaultRemoteAddress(mAddress);
        //监听客户端是否断线
        mConnector.addListener(new MinaConnectListener(mConnector));

    }

    public boolean connect() {
        try {
            if (NetworkUtils.isWifiConnected()) {
                LogUtils.d("wifi已连接，开启连接");
                mFuture = mConnector.connect();
                //等待连接创建成功
                mFuture.awaitUninterruptibly();
                LogUtils.d("等待连接完毕");
                mSession = mFuture.getSession();
            } else {
                Thread.sleep(1000);
                LogUtils.d("wifi未连接");
                return false;
            }
        } catch (IllegalStateException e) {
            //future运行占用资源，死锁
            LogUtils.e("future运行占用资源，死锁" + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            //连接异常，无法获取到session
            LogUtils.e("连接异常，无法获取到session" + e.getMessage());
            return false;
        } catch (Exception e) {
            //连接失败
            LogUtils.e("连接失败" + e.getMessage());
            return false;
        }
        return mSession == null ? false : true;
    }

    public void disconnect() {
        if (mConnector != null) {
            mConnector.dispose();
            mConnector = null;
            mSession = null;
            mAddress = null;
        }
    }


}
