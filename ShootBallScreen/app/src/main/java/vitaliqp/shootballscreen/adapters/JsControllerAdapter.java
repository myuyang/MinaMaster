package vitaliqp.shootballscreen.adapters;

import vitaliqp.shootballscreen.datas.JsController;

/**
 * 类名：vitaliqp.shootballscreen
 * 时间：2018/11/28 下午2:08
 * 描述：此类用于适配手柄+手机模式下的新接口添加
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
@Deprecated
public class JsControllerAdapter {

    private JsController mJsController;

    public JsControllerAdapter() {
    }

    public JsControllerAdapter(JsController jsController) {
        mJsController = jsController;
    }

    public JsController getJsController() {
        return mJsController;
    }

    public void setJsController(JsController jsController) {
        mJsController = jsController;
    }


}

