package vitaliqp.commonutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 类名：vitaliqp.shootballscreen.common
 * 时间：2019/4/28 下午1:26
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CommonPopupWindowController {
    View mPopupView;
    private int mLayoutResId;
    private Context mContext;
    private PopupWindow mPopupWindow;
    private Window mWindow;
    private View mView;

    public CommonPopupWindowController(Context context, PopupWindow popupWindow) {
        mContext = context;
        mPopupWindow = popupWindow;
    }

    public void setView(int layoutResId) {
        mView = null;
        mLayoutResId = layoutResId;
        installContent();
    }

    private void installContent() {
        if (mLayoutResId != 0) {
            mPopupView = LayoutInflater.from(mContext).inflate(mLayoutResId, null);
        } else if (mView != null) {
            mPopupView = mView;
        }
        mPopupWindow.setContentView(mPopupView);
    }

    public void setView(View view) {
        mView = view;
        mLayoutResId = 0;
        installContent();
    }

    private void setWidthAndHeight(int width, int height) {
        if (width == 0 || height == 0) {
            //如果没设置宽高，默认是wrap_content
            mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            mPopupWindow.setWidth(width);
            mPopupWindow.setHeight(height);
        }
    }

    private void setBackGroundLevel(float level) {
        mWindow = ((Activity) mContext).getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = level;
        mWindow.setAttributes(params);
    }

    private void setAnimationStyle(int animationStyle) {
        mPopupWindow.setAnimationStyle(animationStyle);
    }

    private void setOutsideTouchable(boolean touchable) {
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(touchable);
        mPopupWindow.setFocusable(touchable);
    }

    static class PopupParams {

        /**
         * 布局id
         */
        public int layoutResId;
        public Context mContext;
        /**
         * 弹窗的宽和高
         */
        public int mWidth, mHeight;
        public boolean isShowBg, isShowAnim;
        /**
         * 屏幕背景灰色程度
         */
        public float bg_level;
        /**
         * 动画Id
         */
        public int animationStyle;
        public View mView;
        public boolean isTouchable = true;

        public PopupParams(Context mContext) {
            this.mContext = mContext;
        }

        public void apply(CommonPopupWindowController controller) {
            if (mView != null) {
                controller.setView(mView);
            } else if (layoutResId != 0) {
                controller.setView(layoutResId);
            } else {
                throw new IllegalArgumentException("PopupView's contentView is null");
            }
            controller.setWidthAndHeight(mWidth, mHeight);
            //设置outside可点击
            controller.setOutsideTouchable(isTouchable);
            if (isShowBg) {
                //设置背景
                controller.setBackGroundLevel(bg_level);
            }
            if (isShowAnim) {
                controller.setAnimationStyle(animationStyle);
            }
        }
    }
}
