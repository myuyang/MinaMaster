package vitaliqp.shootballscreen.net;

import com.blankj.utilcode.util.LogUtils;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/10 下午5:04
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
class MinaConnectListener implements IoServiceListener {

    private NioSocketConnector mConnector;

    public MinaConnectListener(NioSocketConnector connector) {
        mConnector = connector;
    }

    @Override
    public void serviceActivated(IoService service) throws Exception {
        LogUtils.d("serviceActivated");
    }

    @Override
    public void serviceIdle(IoService service, IdleStatus status) throws Exception {
        LogUtils.d("serviceIdle");
    }

    @Override
    public void serviceDeactivated(IoService service) throws Exception {
        LogUtils.d("serviceDeactivated");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        LogUtils.d("sessionCreated");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        LogUtils.d("sessionClosed");
    }

    @Override
    public void sessionDestroyed(IoSession session) throws Exception {
        LogUtils.d("sessionDestroyed");
        //TODO:处理断线重连
    }
}
