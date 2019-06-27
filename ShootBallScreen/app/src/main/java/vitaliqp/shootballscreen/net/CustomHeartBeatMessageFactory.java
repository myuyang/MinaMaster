package vitaliqp.shootballscreen.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/10 下午4:54
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CustomHeartBeatMessageFactory implements KeepAliveMessageFactory {
    @Override
    public boolean isRequest(IoSession session, Object o) {
        //如果是客户端主动向服务器发起的心跳包, return true
        //该框架会发送 getRequest() 方法返回的心跳包内容.
        return false;
    }

    @Override
    public boolean isResponse(IoSession session, Object o) {
        //如果是服务器发送过来的心跳包, return true后会在 getResponse() 方法中处理心跳包.
        return false;
    }

    @Override
    public Object getRequest(IoSession session) {
        //自定义向服务器发送的心跳包内容.
        return null;
    }

    @Override
    public Object getResponse(IoSession session, Object o) {
        //自定义解析服务器发送过来的心跳包.
        return null;
    }
}
