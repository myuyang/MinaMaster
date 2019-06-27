package vitaliqp.shootballscreen.adapters;

import vitaliqp.shootballscreen.datas.BleControllerMode;

/**
 * 类名：vitaliqp.shootballscreen.adapters
 * 时间：2018/12/6 上午9:16
 * 描述：此类用于适配蓝牙BLE连接新加接口且不同类型的接口
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
@Deprecated
public class BleControllerAdapter {

    private BleControllerMode mBleControllerMode;

    public BleControllerAdapter(){
    }

    public BleControllerAdapter(BleControllerMode bleControllerMode) {
        mBleControllerMode = bleControllerMode;
    }

    public BleControllerMode getBleControllerMode() {
        return mBleControllerMode;
    }

    public void setBleControllerMode(BleControllerMode bleControllerMode) {
        mBleControllerMode = bleControllerMode;
    }
}
