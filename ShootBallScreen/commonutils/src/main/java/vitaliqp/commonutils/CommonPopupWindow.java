package vitaliqp.commonutils;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

/**
 * 类名：vitaliqp.shootballscreen.common
 * 时间：2019/4/28 下午12:57
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CommonPopupWindow extends PopupWindow {
    CommonPopupWindowController mController;

    public CommonPopupWindow(Context context) {
        mController = new CommonPopupWindowController(context, this);
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface ViewInterface {
        /**
         * getchildView
         *
         * @param view
         * @param layoutResId
         */
        void getChildView(View view, int layoutResId);
    }

    public static class Builder {
        private CommonPopupWindowController.PopupParams mParams;
        private ViewInterface listener;

        public Builder(Context context) {
            mParams = new CommonPopupWindowController.PopupParams(context);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return
         */
        public Builder setView(int layoutResId) {
            mParams.mView = null;
            mParams.layoutResId = layoutResId;
            return this;
        }

        /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(View view) {
            mParams.mView = view;
            mParams.layoutResId = 0;
            return this;
        }

        /**
         * 设置子View
         *
         * @param listener ViewInterface
         * @return Builder
         */
        public Builder setViewOnclickListener(ViewInterface listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            mParams.mWidth = width;
            mParams.mHeight = height;
            return this;
        }

        /**
         * 设置背景灰色程度
         *
         * @param level 0.0f-1.0f
         * @return Builder
         */
        public Builder setBackGroundLevel(float level) {
            mParams.isShowBg = true;
            mParams.bg_level = level;
            return this;
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            mParams.isTouchable = touchable;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            mParams.isShowAnim = true;
            mParams.animationStyle = animationStyle;
            return this;
        }

        public CommonPopupWindow create() {
            final CommonPopupWindow popupWindow = new CommonPopupWindow(mParams.mContext);
            mParams.apply(popupWindow.mController);
            if (listener != null && mParams.layoutResId != 0) {
                listener.getChildView(popupWindow.mController.mPopupView, mParams.layoutResId);
            }
            DataUtils.measureWidthAndHeight(popupWindow.mController.mPopupView);
            return popupWindow;
        }
    }
}
