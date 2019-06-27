package vitaliqp.shootballscreen.datas;

import android.util.Log;

/**
 * 类名：vitaliqp.shootballscreen.datas
 * 时间：2018/12/10 下午1:47
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class JsControllerReceiveBuilder {

    private static final String TAG = "ReceiveBuilder";
    private byte[] _buffer;
    private int _offSet;
    private final Object _bufferLock = new Object();

    public JsControllerReceiveBuilder() {
        _buffer = new byte[JsControllerReceive.FRAME_LENGTH];
    }

    public JsControllerReceive append(byte[] data) {

        if (data == null) {

            return null;
        }

        JsControllerReceive receiveData = null;

        synchronized (_bufferLock) {

            for (byte rx : data) {
                JsControllerReceive newOne = append(rx);
                if (newOne != null) {
                    receiveData = newOne;
                }
            }
        }
        return receiveData;
    }

    private JsControllerReceive append(byte rx) {

        _buffer[_offSet++] = rx;
        if (_offSet < 2) {
            return null;
        }

        JsControllerReceive receiveData = null;

        //判断帧头,去除冗余数据
        if (_offSet > 2 &&
                _buffer[_offSet - 2] == JsControllerReceive.FRAME_HEADER[0] &&
                _buffer[_offSet - 1] == JsControllerReceive.FRAME_HEADER[1]) {

            //确定帧头，开始赋值
            _buffer[0] = JsControllerReceive.FRAME_HEADER[0];
            _buffer[1] = JsControllerReceive.FRAME_HEADER[1];
            _offSet = 2;
        } else if (_offSet >= JsControllerReceive.FRAME_LENGTH) {

            if (JsControllerReceive.isValidFrame(_buffer)) {
                try {
                    receiveData = new JsControllerReceive(_buffer);
                } catch (Exception ex) {

                    Log.e(TAG, "append: ReceiveData append Exception" + ex.getMessage());
                }
                _offSet = 0;
            } else if (rx == JsControllerReceive.FRAME_HEADER[0]) {
                _buffer[0] = rx;
                _offSet = 1;
            }

            if (_offSet >= JsControllerReceive.FRAME_LENGTH) {
                _offSet = 0;
            }
        }

        return receiveData;
    }
}
