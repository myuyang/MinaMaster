package vitaliqp.shootballscreen.datas;

import vitaliqp.shootballscreen.enums.ControlMode;

/**
 * 类名：vitaliqp.shootballscreen.datas
 * 时间：2018/12/6 上午9:38
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class JsControllerReceive {

    public static final int FRAME_LENGTH = 20;
    public static final byte[] FRAME_HEADER = new byte[]{(byte) 0xEE, (byte) 0xAA};
    private static final String TAG = "JsControllerReceive";
    private final JsController mController;

    public JsControllerReceive(byte[] stateFrame) {
        mController = JsController.getInstance();
        parse(stateFrame);
    }

    private void parse(byte[] stateFrame) {

        int startIndex = FRAME_HEADER.length + 1;
        startIndex += parseWorkMode(stateFrame, startIndex);
        startIndex += parseIsRunning(stateFrame, startIndex);
//        startIndex += parseIsRollerRunning(stateFrame, startIndex);
    }

    private int parseWorkMode(byte[] frame, int index) {
        switch (frame[index] & 0xFF) {
            case 1:
                mController.setControlMode(ControlMode.REMOTE_MODE);
                break;
            case 2:
                mController.setControlMode(ControlMode.AUTO_MODE);
                break;
            case 3:
                mController.setControlMode(ControlMode.SHOOT_BALL_MODE);
                break;
            default:
                mController.setControlMode(ControlMode.STAND_BY);
                break;
        }

        return 1;
    }

    private int parseIsRunning(byte[] frame, int index) {
        if ((frame[index] & 0xFF) == 0) {
            mController.Stop();
        } else if ((frame[index] & 0xFF) == 1) {
            mController.Start();
        }
        return 15;
    }

    /**
     * 是否为可靠字节
     *
     * @param stateFrame
     * @return
     */
    public static boolean isValidFrame(byte[] stateFrame) {

        return stateFrame != null &&
                stateFrame.length == FRAME_LENGTH &&
                stateFrame[0] == FRAME_HEADER[0] &&
                stateFrame[1] == FRAME_HEADER[1] &&
                (stateFrame[2] & 0xEE) == 0 && checkSumState(stateFrame);
    }

    /**
     * 进行和校验取后八位作为校验字
     *
     * @param stateFrame
     * @return
     */
    private static boolean checkSumState(byte[] stateFrame) {

        int tmp = 0;
        for (int i = 0, len = 19; i < len; i++) {
            tmp += stateFrame[i] & 0xFF;
        }
        return (tmp & 0x00FF) == (stateFrame[19] & 0xFF);
    }

    public ControlMode getContolMode() {
        return mController.getControlMode();
    }

//    private int parseIsRollerRunning(byte[] frame, int index) {
//        setRollerRunning((frame[index] & 0xFF) == 0);
//        return 14;
//    }
}
