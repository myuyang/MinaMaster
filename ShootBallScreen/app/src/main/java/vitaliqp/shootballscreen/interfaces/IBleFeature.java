package vitaliqp.shootballscreen.interfaces;

/**
 * 类名：vitaliqp.shootballscreen.interfaces
 * 时间：2018/11/28 下午4:27
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public interface IBleFeature {

    /**
     * 扫描蓝牙
     */
    void scan();

    /**
     * 连接蓝牙
     */
    void connect();

    /**
     * 断开蓝牙
     */
    void disConnect();

    /**
     * 关闭蓝牙
     */
    void close();
}
