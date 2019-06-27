package vitaliqp.shootballscreen.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 类名：vitaliqp.shootballscreen.base
 * 时间：2018/12/19 下午4:46
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public abstract class BaseBasicActivity extends AppCompatActivity implements IBaseView {

    protected View mContentView;
    protected Activity mActivity;
    private long lastClick = 0;
    private boolean isFastClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        initData(getIntent().getExtras());
        setRootLayout(bindLayout());
        initView(savedInstanceState, mContentView);
        doBusiness();
    }

    @Override
    public void setRootLayout(int layoutId) {
        if (layoutId <= 0) {
            return;
        }
        mContentView = LayoutInflater.from(this).inflate(layoutId, null);
        setContentView(mContentView);
    }

    @Override
    public void onClick(View v) {
        if (!isFastClick()) {
            onWidgetClick(v);
        }
    }

    private boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= 200) {
            lastClick = now;
            return false;
        }
        return true;
    }
}
