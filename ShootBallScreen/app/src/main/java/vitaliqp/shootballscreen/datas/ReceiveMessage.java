package vitaliqp.shootballscreen.datas;

/**
 * 类名： ReceiveMessage
 * 时间：2018/12/3 下午3:21
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
*/
@Deprecated
public class ReceiveMessage {
    /*
     *   通讯格式
     *   数据头+标识+载包+检验字
     *
     *   字节         值             含义                 释义
     *   0           ee             帧头
     *   1           aa             帧头
     *   2-1         01             应用标识
     *   2-2         ff             包类型标识           上位机-app
     *   3           00/01          1开始0停止           默认为0
     *   4-5         [0,65535]      对打剩余时间          默认为0
     *   6           [0,255]        出球最高速度          m/s
     *   7           [0,255]        出球平均速度          m/s
     *   8           [0,255]        一回合最短时间        0.25*s
     *   9           [0,255]        一回合平均时间        0.25*s
     *   10-18       预留
     *   19          checkSum       检验字
     * */

    public static final int FRAME_LENGTH = 20;

    public static final byte[] FRAME_HEADER = new byte[]{(byte) 0xEE, (byte) 0xAA};

    private boolean isStart;

    private int fightTime;

    private int highestSpeed;

    private int averageSpeed;

    private int shortestTime;

    private int averageTime;

    public boolean getStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public int getFightTime() {
        return fightTime;
    }

    public void setFightTime(int fightTime) {
        this.fightTime = fightTime;
    }

    public int getHighestSpeed() {
        return highestSpeed;
    }

    public void setHighestSpeed(int highestSpeed) {
        this.highestSpeed = highestSpeed;
    }

    public int getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getShortestTime() {
        return shortestTime;
    }

    public void setShortestTime(int shortestTime) {
        this.shortestTime = shortestTime;
    }

    public int getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(int averageTime) {
        this.averageTime = averageTime;
    }

    public ReceiveMessage(byte[] stateFrame) {
        parse(stateFrame);
    }

    private void parse(byte[] stateFrame) {

        int startIndex = FRAME_HEADER.length + 1;
        startIndex += parseIsStart(stateFrame, startIndex);
        startIndex += parseFightTime(stateFrame, startIndex);
        startIndex += parseHighestSpeed(stateFrame, startIndex);
        startIndex += parseAverageSpeed(stateFrame, startIndex);
        startIndex += parseShortestTime(stateFrame, startIndex);
        startIndex += parseAverageTime(stateFrame, startIndex);
    }

    /**
     * 处理机器人速度
     *
     * @param stateFrame
     * @param startIndex
     * @return
     */
    private int parseIsStart(byte[] stateFrame, int startIndex) {
        isStart = (stateFrame[startIndex] & 0xFF) == 0;
        return 1;
    }

    /**
     * 处理剩余对打时间
     *
     * @param stateFrame
     * @param startIndex
     * @return
     */
    private int parseFightTime(byte[] stateFrame, int startIndex) {
        fightTime = stateFrame[startIndex] & 0xFF;
        fightTime = fightTime << 8;
        fightTime += stateFrame[startIndex + 1] & 0xFF;
        return 2;
    }

    /**
     * 处理最高球速
     *
     * @param stateFrame
     * @param startIndex
     * @return
     */
    private int parseHighestSpeed(byte[] stateFrame, int startIndex) {
        highestSpeed = stateFrame[startIndex] & 0xFF;
        return 1;
    }

    /**
     * 处理平均球速
     *
     * @param stateFrame
     * @param startIndex
     * @return
     */
    private int parseAverageSpeed(byte[] stateFrame, int startIndex) {
        averageSpeed = stateFrame[startIndex] & 0xFF;
        return 1;
    }

    /**
     * 处理回合最短时间
     *
     * @param stateFrame
     * @param startIndex
     * @return
     */
    private int parseShortestTime(byte[] stateFrame, int startIndex) {
        shortestTime = stateFrame[startIndex] & 0xFF;
        return 1;
    }


    /**
     * 处理错误信息
     *
     * @param stateFrame
     * @param startIndex
     * @return
     */
    private int parseAverageTime(byte[] stateFrame, int startIndex) {
        averageTime = stateFrame[startIndex] & 0xFF;
        return 10;
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

    private boolean batteryLow;

    private boolean leftMotorMalfunctional;

    private boolean rightMotorMalfunctional;

    private boolean ballJamed;

    private boolean hasError;

    private RobotErrorProperties errorProperties;

    public RobotErrorProperties getErrorProperties() {
        return errorProperties;
    }

    public void setErrorProperties(RobotErrorProperties errorProperties) {
        this.errorProperties = errorProperties;
    }

    public boolean isBatteryLow() {
        return batteryLow;
    }

    public void setBatteryLow(boolean batteryLow) {
        this.batteryLow = batteryLow;
    }

    public boolean isLeftMotorMalfunctional() {
        return leftMotorMalfunctional;
    }

    public void setLeftMotorMalfunctional(boolean leftMotorMalfunctional) {
        this.leftMotorMalfunctional = leftMotorMalfunctional;
    }

    public boolean isRightMotorMalfunctional() {
        return rightMotorMalfunctional;
    }

    public void setRightMotorMalfunctional(boolean rightMotorMalfunctional) {
        this.rightMotorMalfunctional = rightMotorMalfunctional;
    }

    public boolean isBallJamed() {
        return ballJamed;
    }

    public void setBallJamed(boolean ballJamed) {
        this.ballJamed = ballJamed;
    }

    public boolean isHasError() {

        return batteryLow || leftMotorMalfunctional || rightMotorMalfunctional || ballJamed;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    private class RobotErrorProperties {

        private RobotErrorProperties(byte properties) {
            batteryLow = (properties & (byte) 0x01) > 0;
            leftMotorMalfunctional = (properties & (byte) 0x02) > 0;
            rightMotorMalfunctional = (properties & (byte) 0x04) > 0;
            ballJamed = (properties & (byte) 0x08) > 0;
        }
    }
}
