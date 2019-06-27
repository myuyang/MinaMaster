package vitaliqp.shootballscreen.interfaces;

import vitaliqp.shootballscreen.enums.ControlMode;
import vitaliqp.shootballscreen.datas.Speeds;
import vitaliqp.shootballscreen.datas.ShootBallConfiguration;
import vitaliqp.shootballscreen.enums.ShootBallPattern;

/**
 * 类名： IMessage
 * 时间：2018/12/5 下午5:10
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
*/
public interface IMessage {

    /**
     * 切换模式
     * enum ControlMode: Standby, RemoteControl, BallCollect, ShootBall;
     */
    void setControlMode(ControlMode mode);

    ControlMode getControlMode();

    /**
     * 开始/暂停 for all three control modes: RemoteControl, BallCollect and ShootBall
     * It will be ignored if it is in Standby mode
     */
    void Start();

    void Stop();

    Boolean isRunning();

    /**
     * 是否开启滚筒. startBallRoller() and stopBallRoller() will be ignored
     * if the ControlMode is not in RemoteControl and BallCollect modes.
     */
    void startRoller();

    void stopRoller();

    Boolean isRollerRunning();


    /**
     * 控制 linear and angular speeds. Speeds include two properties: linear_speed, angular_speed.
     * The unit of linear speed is cm/second;
     * The unit of angular speed unit is degree/second
     */
    void setSpeeds(Speeds speeds);

    Speeds getSpeeds();

    /**
     * Get/Set shoot ball pattern. enum ShootBallPattern: Manual, LeftAndRight, ShortAndDeep, Random
     * @param pattern
     */
    void setShootBallPattern(ShootBallPattern pattern);

    ShootBallPattern getShootBallPattern();

    /**
     * 设置发球参数 Get/Set shoot ball configuration of the manual pattern
     * @param configuration
     */
    void setShootBallConfiguration(ShootBallConfiguration configuration);

    ShootBallConfiguration getShootBallConfiguration();
}

