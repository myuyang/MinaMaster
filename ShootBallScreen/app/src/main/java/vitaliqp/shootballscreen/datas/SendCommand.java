package vitaliqp.shootballscreen.datas;

/**
 * 类名： SendCommand
 * 时间：2018/12/3 下午3:19
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
*/
@Deprecated
public class SendCommand {

    /*
     *   通讯格式
     *   数据头+标识+载包+检验字
     *
     *   字节         值             含义                 释义
     *   0           ee             帧头
     *   1           aa             帧头
     *   2-1         01             应用标识
     *   2-2         01             包类型标识           app-上位机
     *   3           00/01          1开始0停止           默认为0
     *   4           00/01          1发球方0否           默认为0
     *   5           [0,255]        对打时间             min
     *   6           [0,255]        对打启动延迟          s
     *   7-18        预留
     *   19          checkSum       检验字
     * */

    public static final int FRAME_LENGTH = 20;

    public static final byte[] FRAME_HEADER = new byte[]{(byte) 0xEE, (byte) 0xAA};

    private boolean isStart;

    private boolean isServe;

    private int fightTime;

    private int fightDelay;

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isServe() {
        return isServe;
    }

    public void setServe(boolean serve) {
        isServe = serve;
    }

    public int getFightTime() {
        return fightTime;
    }

    public void setFightTime(int fightTime) {
        this.fightTime = fightTime;
    }

    public int getFightDelay() {
        return fightDelay;
    }

    public void setFightDelay(int fightDelay) {
        this.fightDelay = fightDelay;
    }

    public byte[] sendCommand() {

        byte[] bytes = new byte[FRAME_LENGTH];

        System.arraycopy(FRAME_HEADER, 0, bytes, 0, FRAME_HEADER.length);

        int startIndex = FRAME_HEADER.length;
        startIndex += fillFlag(bytes, startIndex);
        startIndex += fillIsStart(bytes, startIndex);
        startIndex += fillIsServe(bytes, startIndex);
        startIndex += fillFightTime(bytes, startIndex);
        startIndex += fillFightDelay(bytes, startIndex);
        startIndex += fillCheckSum(bytes, startIndex);

        return bytes;
    }

    /**
     * 处理标识
     *
     * @param bytes
     * @param index
     * @return
     */
    private int fillFlag(byte[] bytes, int index) {
        bytes[index] = (byte) 0x09;
        return 1;
    }

    /**
     * 处理发球
     *
     * @param bytes
     * @param index
     * @return
     */
    private int fillIsStart(byte[] bytes, int index) {
        bytes[index] = (isStart ? (byte) 0x01 : (byte) 0x00);
        return 1;
    }

    /**
     * 设置发球方
     *
     * @param bytes
     * @param index
     * @return
     */
    private int fillIsServe(byte[] bytes, int index) {
        bytes[index] = (isServe ? (byte) 0x01 : (byte) 0x00);
        return 1;
    }

    /**
     * 设置对打时间
     *
     * @param bytes
     * @param index
     * @return
     */
    private int fillFightTime(byte[] bytes, int index) {
        bytes[index] = (byte) (fightTime & 0xFF);
        return 1;
    }

    /**
     * 设置启动时延
     *
     * @param bytes
     * @param index
     * @return
     */
    private int fillFightDelay(byte[] bytes, int index) {
        bytes[index] = (byte) (fightDelay & 0xFF);
        return 13;
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
            temp += bytes[i]  & 0xFF;
        }

        bytes[index] = (byte) (temp & 0x00FF);
        return 1;
    }
}
