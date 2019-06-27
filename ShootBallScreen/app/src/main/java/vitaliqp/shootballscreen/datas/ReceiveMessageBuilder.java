package vitaliqp.shootballscreen.datas;

import android.util.Log;

/**
 * 类名：vitaliqp.shootballscreen.datas
 * 时间：2018/12/4 上午10:00
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
@Deprecated
public class ReceiveMessageBuilder {

    private static final String TAG = "ReceiveMessageBuilder";
    private byte[] _buffer;
    private int _offSet;
    private final Object _bufferLock = new Object();

    public ReceiveMessageBuilder() {
        _buffer = new byte[ReceiveMessage.FRAME_LENGTH];
    }

    public ReceiveMessage append(byte[] data) {

        if (data == null) {

            return null;
        }

        ReceiveMessage receiveData = null;

        synchronized (_bufferLock) {

            for (byte rx : data) {
                ReceiveMessage newOne = append(rx);
                if (newOne != null) {
                    receiveData = newOne;
                }
            }
        }
        return receiveData;
    }

    private ReceiveMessage append(byte rx) {

        _buffer[_offSet++] = rx;
        if (_offSet < 2) {
            return null;
        }

        ReceiveMessage receiveData = null;

        //判断帧头,去除冗余数据
        if (_offSet > 2 &&
                _buffer[_offSet - 2] == ReceiveMessage.FRAME_HEADER[0] &&
                _buffer[_offSet - 1] == ReceiveMessage.FRAME_HEADER[1]) {

            //确定帧头，开始赋值
            _buffer[0] = ReceiveMessage.FRAME_HEADER[0];
            _buffer[1] = ReceiveMessage.FRAME_HEADER[1];
            _offSet = 2;
        } else if (_offSet >= ReceiveMessage.FRAME_LENGTH) {

            if (ReceiveMessage.isValidFrame(_buffer)) {
                try {
                    receiveData = new ReceiveMessage(_buffer);
                } catch (Exception ex) {

                    Log.e(TAG, "append: ReceiveData append Exception" + ex.getMessage());
                }
                _offSet = 0;
            } else if (rx == ReceiveMessage.FRAME_HEADER[0]) {
                _buffer[0] = rx;
                _offSet = 1;
            }

            if (_offSet >= ReceiveMessage.FRAME_LENGTH) {
                _offSet = 0;
            }
        }

        return receiveData;
    }
}
