package vitaliqp.shootballscreen.datas;

import org.greenrobot.eventbus.EventBus;

import vitaliqp.shootballscreen.enums.ControlMode;
import vitaliqp.shootballscreen.enums.ShootBallPattern;
import vitaliqp.shootballscreen.events.MessageControlModeEvent;
import vitaliqp.shootballscreen.events.MessageControlStartEvent;
import vitaliqp.shootballscreen.interfaces.IMessage;

/**
 * 类名： JsController
 * 时间：2018/12/6 下午1:55
 * 描述：此类用于与手柄通讯
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class JsController implements IMessage {

    /**
     * 单例模式
     **/
    private static JsController mJsController = null;
    private boolean mStart;
    private boolean mStartRoller;
    private ControlMode mMode = ControlMode.STAND_BY;
    private boolean mRobotRunning;
    private boolean mIsRobotRollerRunning;
    private Speeds mInitSpeeds;
    private ShootBallPattern mPattern = ShootBallPattern.LEFTANDRIGHT;
    private ShootBallConfiguration mConfiguration;

    /**
     * 构造函数私有化
     **/
    private JsController() {
        mInitSpeeds = new Speeds();
        mConfiguration = new ShootBallConfiguration();
    }

    /**
     * 公有的静态函数，对外暴露获取单例对象的接口
     **/
    public static JsController getInstance() {
        if (mJsController == null) {
            synchronized (JsController.class) {
                if (mJsController == null) {
                    mJsController = new JsController();
                }
            }
        }
        return mJsController;
    }

    private boolean isRobotRollerRunning() {
        return mIsRobotRollerRunning;
    }

    private void setRobotRollerRunning(boolean robotRollerRunning) {
        if (robotRollerRunning == mIsRobotRollerRunning) {
            return;
        }
        mIsRobotRollerRunning = robotRollerRunning;
        EventBus.getDefault().postSticky(new MessageControlStartEvent(mIsRobotRollerRunning));
    }

    private ControlMode getMode() {
        return mMode;
    }


    private void setMode(ControlMode mode) {
        if (mode == mMode) {
            return;
        }
        mMode = mode;
        EventBus.getDefault().post(new MessageControlModeEvent(mMode));
    }

    private boolean isRobotRunning() {
        return mRobotRunning;
    }

    private void setRobotRunning(boolean robotRunning) {
        if (robotRunning == mRobotRunning) {
            return;
        }

        mRobotRunning = robotRunning;
        EventBus.getDefault().postSticky(new MessageControlStartEvent(mRobotRunning));
    }

    private Speeds getInitSpeeds() {
        return mInitSpeeds;
    }

    private void setInitSpeeds(Speeds initSpeeds) {
        mInitSpeeds = initSpeeds;
    }

    private ShootBallPattern getPattern() {
        return mPattern;
    }

    private void setPattern(ShootBallPattern pattern) {
        mPattern = pattern;
    }

    private ShootBallConfiguration getConfiguration() {
        return mConfiguration;
    }

    private void setConfiguration(ShootBallConfiguration configuration) {
        mConfiguration = configuration;
    }


    @Override
    public void setControlMode(ControlMode mode) {
        setMode(mode);
    }

    @Override
    public ControlMode getControlMode() {
        return getMode();
    }

    @Override
    public void Start() {
        setRobotRunning(true);
    }

    @Override
    public void Stop() {
        setRobotRunning(false);
    }

    @Override
    public Boolean isRunning() {
        return isRobotRunning();
    }

    @Override
    public void startRoller() {
        setRobotRollerRunning(true);
    }

    @Override
    public void stopRoller() {
        setRobotRollerRunning(false);
    }

    @Override
    public Boolean isRollerRunning() {
        return isRobotRollerRunning();
    }

    @Override
    public void setSpeeds(Speeds speeds) {
        setInitSpeeds(speeds);
    }

    @Override
    public Speeds getSpeeds() {
        return getInitSpeeds();
    }

    @Override
    public void setShootBallPattern(ShootBallPattern pattern) {
        setPattern(pattern);
    }

    @Override
    public ShootBallPattern getShootBallPattern() {
        return getPattern();
    }

    @Override
    public void setShootBallConfiguration(ShootBallConfiguration configuration) {
        setConfiguration(configuration);
    }

    @Override
    public ShootBallConfiguration getShootBallConfiguration() {
        return getConfiguration();
    }
}
