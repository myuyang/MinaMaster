package vitaliqp.shootballscreen.net;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import vitaliqp.commonutils.CommonGsonUtils;
import vitaliqp.shootballscreen.datas.ReceiveJsonFormServer;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/10 下午5:14
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CustomServiceHandler extends IoHandlerAdapter {
    private Context mContext;

    public CustomServiceHandler(Context context) {
        mContext = context;
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        LogUtils.d("sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        //连接打开ID
        LogUtils.d("sessionOpened" + session);
        MinaSessionManager.getInstance().setSession(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        LogUtils.d("sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
        LogUtils.e("exceptionCaught" + cause.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        super.messageReceived(session, message);

        LogUtils.d("msg = " + message.toString());

        ReceiveJsonFormServer receiveJsonFormServer = CommonGsonUtils.getGson().
                fromJson(message.toString(), ReceiveJsonFormServer.class);

        LogUtils.d("type = " + receiveJsonFormServer.getType());

    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        //发送数据
        LogUtils.d("messageSent" + message.toString());
    }
}
