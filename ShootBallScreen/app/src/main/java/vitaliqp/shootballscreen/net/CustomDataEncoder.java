package vitaliqp.shootballscreen.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

import vitaliqp.shootballscreen.datas.Constant;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/9 上午9:35
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CustomDataEncoder extends ProtocolEncoderAdapter {

    @Override
    public void encode(IoSession session, Object o, ProtocolEncoderOutput output) throws Exception {
        if (o instanceof String) {
            String msg = (String) o;
            byte[] bytes = msg.getBytes(Charset.forName("UTF-8"));
            IoBuffer ioBuffer = IoBuffer.allocate(bytes.length + 8);
            ioBuffer.putInt(Constant.SENT_HEADER);
            ioBuffer.putInt(bytes.length);
            ioBuffer.put(bytes);
            ioBuffer.flip();
            output.write(ioBuffer);
        } else {
            //不处理其他类型数据
        }
    }
}
