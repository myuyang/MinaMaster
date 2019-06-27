package vitaliqp.shootballscreen.datas;

import vitaliqp.shootballscreen.enums.ControlMode;

/**
 * 类名：vitaliqp.shootballscreen.datas
 * 时间：2018/12/6 下午2:36
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class JsControllerSend {

    public static final int FRAME_LENGTH = 20;

    public static final byte[] FRAME_HEADER = new byte[]{(byte) 0xEE, (byte) 0xAA};

    private final JsController mJsController;

    public JsControllerSend() {
        mJsController = JsController.getInstance();
    }

    public void setContorlMode(ControlMode mode){

        mJsController.setControlMode(mode);
    }



    public byte[] sendCommand() {

        byte[] bytes = new byte[FRAME_LENGTH];

        System.arraycopy(FRAME_HEADER, 0, bytes, 0, FRAME_HEADER.length);

        int startIndex = FRAME_HEADER.length;
        startIndex += fillFlag(bytes, startIndex);
        startIndex += fillWorkMode(bytes, startIndex);
        startIndex += fillIsStart(bytes, startIndex);
        startIndex += fillIsStartRoller(bytes, startIndex);
        startIndex += fillLinSpeeds(bytes, startIndex);
        startIndex += fillAngSpeeds(bytes, startIndex);
        startIndex += fillShootBallPattern(bytes, startIndex);
        startIndex += fillShootBallConfiguration(bytes, startIndex);
        startIndex += fillCheckSum(bytes, startIndex);

        return bytes;
    }

    private int fillFlag(byte[] bytes, int index) {
        bytes[index] = (byte) 0x09;
        return 1;
    }

    private int fillWorkMode(byte[] bytes, int index) {
        switch (mJsController.getControlMode()) {
            case REMOTE_MODE:
                bytes[index] = (byte) 0x01;
                break;
            case AUTO_MODE:
                bytes[index] = (byte) 0x02;
                break;
            case SHOOT_BALL_MODE:
                bytes[index] = (byte) 0x03;
                break;
            default:
                bytes[index] = (byte) 0x00;
                break;
        }

        return 1;
    }

    private int fillIsStart(byte[] bytes, int index) {
        bytes[index] = mJsController.isRunning() ? (byte) 0x01 : (byte) 0x00;
        return 1;
    }

    private int fillIsStartRoller(byte[] bytes, int index) {
        bytes[index] = mJsController.isRollerRunning() ? (byte) 0x01 : (byte) 0x00;
        return 1;
    }

    private int fillLinSpeeds(byte[] bytes, int index) {
        bytes[index] = (byte) (mJsController.getSpeeds().getLinSpeed() & 0xFF);
        return 1;
    }

    private int fillAngSpeeds(byte[] bytes, int index) {
        bytes[index] = (byte) (mJsController.getSpeeds().getAngSpeed() & 0xFF);
        return 1;
    }

    private int fillShootBallPattern(byte[] bytes, int index) {
        switch (mJsController.getShootBallPattern()) {
            case LEFTANDRIGHT:
                bytes[index] = (byte) 0x01;
                break;
            case SHORTANDDEEP:
                bytes[index] = (byte) 0x02;
                break;
            case RANDOM:
                bytes[index] = (byte) 0x03;
                break;
            default:
                bytes[index] = (byte) 0x00;
                break;
        }
        return 1;
    }

    private int fillShootBallConfiguration(byte[] bytes, int index) {
        ShootBallConfiguration configuration = mJsController.getShootBallConfiguration();
        bytes[index] = (byte) (configuration.getElevationAngular() & 0xFF);
        bytes[index + 1] = (byte) (configuration.getDistance() & 0xFF);
        bytes[index + 2] = (byte) (configuration.getFrequency() & 0xFF);

        return 10;
    }

    /**
     * 处理检验字
     *
     * @param bytes
     * @param index
     * @return
     */
    private int fillCheckSum(byte[] bytes, int index) {

        int temp = 0;
        for (int i = 0, len = 19; i < len; i++) {
            temp += bytes[i] & 0xFF;
        }

        bytes[index] = (byte) (temp & 0x00FF);
        return 1;
    }

}
