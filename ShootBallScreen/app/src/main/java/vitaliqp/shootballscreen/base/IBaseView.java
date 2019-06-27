package vitaliqp.shootballscreen.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * 类名：vitaliqp.shootballscreen.base
 * 时间：2018/12/19 下午4:13
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public interface IBaseView extends View.OnClickListener{

    /**
     * init data
     * @param bundle
     */
    void initData(@Nullable Bundle bundle);

    /**
     * bind layout
     * @return
     */
    int bindLayout();

    /**
     * Set root layout
     * @param layoutId
     */
    void setRootLayout(int layoutId);

    /**
     * Init view
     * @param savedInstanceState
     * @param contentView
     */
    void initView(@Nullable Bundle savedInstanceState,View contentView);

    /**
     * do your business
     */
    void doBusiness();

    /**
     * Widget click
     * @param view
     */
    void onWidgetClick(View view);
}
