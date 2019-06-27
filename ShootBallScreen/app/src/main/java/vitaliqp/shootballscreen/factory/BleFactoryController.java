package vitaliqp.shootballscreen.factory;

import vitaliqp.shootballscreen.interfaces.IBleFeature;

/**
 * 类名：vitaliqp.shootballscreen.factory
 * 时间：2018/11/28 下午4:36
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class BleFactoryController extends BleFactory {

    private IBleFeature mIBleFeature;

    public BleFactoryController(){
    }

    public BleFactoryController(IBleFeature IBleFeature) {
        mIBleFeature = IBleFeature;
    }

    public IBleFeature getIBleFeature() {
        return mIBleFeature;
    }

    public void setIBleFeature(IBleFeature IBleFeature) {
        mIBleFeature = IBleFeature;
    }

    @Override
    public IBleFeature getBleController() {
        return mIBleFeature;
    }
}
