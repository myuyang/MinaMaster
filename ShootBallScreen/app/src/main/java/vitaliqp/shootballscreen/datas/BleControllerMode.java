package vitaliqp.shootballscreen.datas;

import vitaliqp.shootballscreen.enums.ControlMode;
import vitaliqp.shootballscreen.enums.ShootBallPattern;
import vitaliqp.shootballscreen.interfaces.IBleFeature;
import vitaliqp.shootballscreen.interfaces.IMessage;

/**
 * 类名：vitaliqp.shootballscreen.interfaces
 * 时间：2018/11/28 下午1:56
 * 描述：此类用于蓝牙BLE连接状态下的数据类
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
@Deprecated
public class BleControllerMode implements IMessage ,IBleFeature{

    public BleControllerMode() {
    }

    @Override
    public void setControlMode(ControlMode mode) {

    }

    @Override
    public ControlMode getControlMode() {
        return null;
    }

    @Override
    public void Start() {

    }

    @Override
    public void Stop() {

    }

    @Override
    public Boolean isRunning() {
        return null;
    }

    @Override
    public void startRoller() {

    }

    @Override
    public void stopRoller() {

    }

    @Override
    public Boolean isRollerRunning() {
        return null;
    }

    @Override
    public void setSpeeds(Speeds speeds) {

    }

    @Override
    public Speeds getSpeeds() {
        return null;
    }

    @Override
    public void setShootBallPattern(ShootBallPattern pattern) {

    }

    @Override
    public ShootBallPattern getShootBallPattern() {
        return null;
    }

    @Override
    public void setShootBallConfiguration(ShootBallConfiguration configuration) {

    }

    @Override
    public ShootBallConfiguration getShootBallConfiguration() {
        return null;
    }


    @Override
    public void scan() {

    }

    @Override
    public void connect() {

    }

    @Override
    public void disConnect() {

    }

    @Override
    public void close() {

    }
}
