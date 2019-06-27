package vitaliqp.shootballscreen.datas;

/**
 * 类名：vitaliqp.shootballscreen.datas
 * 时间：2018/12/3 下午1:39
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class Constant {
    /**
     * 日志开关
     */
    public static final boolean LOG_SWITCH = false;
    public static final boolean CONSOLE_SWITCH = false;

    /**
     * 欢迎界面时间
     */
    public static final int SPLASH_TIME_MILLISECONDS = 1000;

    public static final int LONG_CLICK_TIME_MILLISECONDS = 1000;

    public static final int ACTIVITY_CHANGE_TIME_MILLISECONDS = 500;

    /**
     * 主界面球旋转一圈的时间
     */
    public static final int MAIN_BALL_ANIMATOR_DURATION = 6000;

    /**
     * fragment
     */
    public static final String FIXEDPOINT = "fixedpoint";
    public static final String HORIZONTAL = "horizontal";
    public static final String VERTICAL = "vertical";
    public static final String AREA = "area";
    public static final String SMARRT = "smart";

    /**
     * OTG配置
     */
    public static final int BAUDTATE = 115200;
    public static final byte STOPBIT = 1;
    public static final byte DATABIT = 8;
    public static final byte PARITY = 0;
    public static final byte FLOWCONTROL = 0;

    /**
     * Socket配置
     */
    public static final int PORT = 33000;
    public static final String IP = "192.168.0.101";
    public static final int CONNECT_TIMEOUT = 3000;
    public static final int CONNECT_TIME = 3000;
    public static final int READ_BUFFER_SIZE = 2048;
    /**
     * 帧头：FFEC
     */
    public static final int SENT_HEADER = 65512;
    /**
     * 模式选择
     */
    public static final int MODE_STAND_BY = 0;
    public static final int MODE_REMOTE = 1;
    public static final int MODE_AUTO = 2;
    public static final int MODE_SHOOT = 3;

    /**
     * 网络开关
     */
    public static final boolean SOCKET_SWITCH = true;

    /**
     * 命令发送频率
     */
    public static final int COMMAND_FREQUENCY = 20;
}
