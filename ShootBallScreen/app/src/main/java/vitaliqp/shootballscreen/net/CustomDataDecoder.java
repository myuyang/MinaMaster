package vitaliqp.shootballscreen.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

import vitaliqp.shootballscreen.datas.Constant;

/**
 * 类名：vitaliqp.shootballscreen.net
 * 时间：2019/4/9 上午9:34
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CustomDataDecoder extends CumulativeProtocolDecoder {

    /**
     * 返回值含义:
     * 1、当内容刚好时，返回false，告知父类接收下一批内容
     * 2、内容不够时需要下一批发过来的内容，此时返回false，这样父类
     * CumulativeProtocolDecoder 会将内容放进IoSession中，等下次来数据后就自动拼装再交给本类的doDecode
     * 3、当内容多时，返回true，因为需要再将本批数据进行读取，父类会将剩余的数据再次推送本类的doDecode方法
     */
    private final static Charset charset = Charset.forName("UTF-8");

    private IoBuffer mBuffer = IoBuffer.allocate(100).setAutoExpand(true);

    @Override
    protected boolean doDecode(IoSession session, IoBuffer buffer, ProtocolDecoderOutput output) throws Exception {

        /**
         * 消息格式：消息头（int类型：头标识、int类型：消息体长度）+ 消息体（Json）
         */

        int start = buffer.position();

        if (buffer.remaining() < 8) {
            return false;
        }

        int tag = buffer.getInt();
        int len = buffer.getInt();

        if (tag != Constant.SENT_HEADER) {
            buffer.position(start);
            return false;
        }

        if (buffer.remaining() < len + 4) {
            buffer.position(start);
            return false;
        }

        byte[] dataTypeByte = new byte[len];

        buffer.get(dataTypeByte, 0, len);

        String body = new String(dataTypeByte);

        output.write(body);


//        if (buffer.remaining() < 8) {
//            return false;
//        }
//
//        int tag = buffer.getInt();
//        int len = buffer.getInt();
//
//        if (buffer.remaining() > 1) {
//            buffer.mark();
//            if (tag == Constant.SENT_HEADER) {
//                if (len > buffer.remaining() - 4) {
//                    //出现断包
//                    buffer.reset();
//                    return false;
//                } else {
//
////                    buffer.reset();
//
//                    int sumLen = 8 + len;
//
//                    byte[] pack = new byte[sumLen];
//
//                    buffer.get(pack, 0, sumLen);
//
//                    IoBuffer buf = IoBuffer.allocate(sumLen);
//
//                    buf.put(pack);
//
//                    buf.flip();
//
//                    output.write(buf);
//
//                    if (buffer.remaining() > 0) {
//                        return true;
//                    }
//                }
//            } else {
//                return false;
//            }
//        }
        return false;
    }
}
