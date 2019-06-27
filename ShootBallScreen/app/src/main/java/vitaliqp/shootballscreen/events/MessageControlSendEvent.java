package vitaliqp.shootballscreen.events;

import vitaliqp.shootballscreen.datas.JsControllerSend;

/**
 * 类名：vitaliqp.shootballscreen.events
 * 时间：2018/12/14 下午3:30
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class MessageControlSendEvent {
    private JsControllerSend mJsControllerSend;

    public MessageControlSendEvent(JsControllerSend jsControllerSend) {
        mJsControllerSend = jsControllerSend;
    }

    public JsControllerSend getJsControllerSend() {
        return mJsControllerSend;
    }

    public void setJsControllerSend(JsControllerSend jsControllerSend) {
        mJsControllerSend = jsControllerSend;
    }
}
