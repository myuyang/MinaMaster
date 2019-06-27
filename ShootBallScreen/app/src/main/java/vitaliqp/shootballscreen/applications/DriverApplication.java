package vitaliqp.shootballscreen.applications;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.usb.UsbManager;

import com.blankj.utilcode.util.LogUtils;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import vitaliqp.shootballscreen.datas.Constant;

/**
 * 类名：vitaliqp.shootballscreen
 * 时间：2018/11/29 下午2:39
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class DriverApplication extends BaseApplication {

    public static final String TAG = "cn.wch.wchusbdriver";
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";

    /**
     * 需要将CH34x的驱动类写在APP类下面
     * 使得帮助类的生命周期与整个应用程序的生命周期是相同的
     */
    public static CH34xUARTDriver driver;

    @Override
    public void onCreate() {
        super.onCreate();
        initDriver();
        initLog();
    }

    private void initLog() {
        LogUtils.Config config = LogUtils.getConfig();
        config.setLogSwitch(Constant.LOG_SWITCH);
        config.setConsoleSwitch(Constant.CONSOLE_SWITCH);
    }

    /**
     * create driver
     */
    private void initDriver() {
        driver = new CH34xUARTDriver((UsbManager)
                getSystemService(Context.USB_SERVICE), this.getApplicationContext(), ACTION_USB_PERMISSION);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
