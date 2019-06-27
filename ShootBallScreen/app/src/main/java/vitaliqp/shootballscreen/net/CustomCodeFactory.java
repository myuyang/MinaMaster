package vitaliqp.shootballscreen.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/9 上午9:33
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CustomCodeFactory implements ProtocolCodecFactory {

    private CustomDataEncoder mDataEncoder;
    private CustomDataDecoder mDataDecoder;

    public CustomCodeFactory(CustomDataEncoder dataEncoder, CustomDataDecoder dataDecoder) {
        mDataEncoder = dataEncoder == null ? new CustomDataEncoder() : dataEncoder;
        mDataDecoder = dataDecoder == null ? new CustomDataDecoder() : dataDecoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return mDataEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return mDataDecoder;
    }
}
