package vitaliqp.shootballscreen.events;

import vitaliqp.shootballscreen.datas.JsControllerReceive;

/**
 * 类名：vitaliqp.shootballscreen.events
 * 时间：2018/12/14 下午3:28
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class MessageControlReceiveEvent {

    private JsControllerReceive mJsControllerReceive;

    public MessageControlReceiveEvent(JsControllerReceive jsControllerReceive) {
        mJsControllerReceive = jsControllerReceive;
    }

    public JsControllerReceive getJsControllerReceive() {
        return mJsControllerReceive;
    }

    public void setJsControllerReceive(JsControllerReceive jsControllerReceive) {
        mJsControllerReceive = jsControllerReceive;
    }
}
