package vitaliqp.shootballscreen.events;

import vitaliqp.shootballscreen.enums.ControlMode;

/**
 * 类名：vitaliqp.shootballscreen.events
 * 时间：2018/12/12 下午4:31
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class MessageControlModeEvent {

    private ControlMode mControlMode;

    public MessageControlModeEvent(ControlMode controlMode) {
        mControlMode = controlMode;
    }

    public ControlMode getControlMode() {
        return mControlMode;
    }

    public void setControlMode(ControlMode controlMode) {
        mControlMode = controlMode;
    }
}
