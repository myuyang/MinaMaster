package vitaliqp.shootballscreen.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import vitaliqp.shootballscreen.R;

/**
 * 类名：vitaliqp.shootballscreen.views
 * 时间：2019/4/19 下午1:15
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CustomDragView extends View {

    /**
     * 可拖拽方向
     */
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    private static final int DEFAULT_SIZE_WIDTH = 100;
    private static final int DEFAULT_SIZE_HEIGHT = 40;
    /**
     * 可移动区域背景
     */
    private static final int AREA_BACKGROUND_MODE_PIC = 0;
    private static final int AREA_BACKGROUND_MODE_COLOR = 1;
    private static final int AREA_BACKGROUND_MODE_XML = 2;
    private static final int AREA_BACKGROUND_MODE_DEFAULT = 3;

    /**
     * 文字区域背景
     */
    private static final int TEXT_BACKGROUND_MODE_PIC = 4;
    private static final int TEXT_BACKGROUND_MODE_COLOR = 5;
    private static final int TEXT_BACKGROUND_MODE_XML = 6;
    private static final int TEXT_BACKGROUND_MODE_DEFAULT = 7;
    private final Paint mAreaBackgroundPaint;
    private Point mCenterPoint;
    private Point mShowPoint;
    private int mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
    private int mDirection = UP;
    private int mAreaLength;
    private int mTextBackgroundMode = TEXT_BACKGROUND_MODE_DEFAULT;
    private Bitmap mAreaBitmap;
    private Bitmap mTextBitmap;
    private int mAreaColor;
    private int mTextColor;

    public CustomDragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initAttribute(context, attrs);

        mAreaBackgroundPaint = new Paint();
        mAreaBackgroundPaint.setAntiAlias(true);

        mCenterPoint = new Point();
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragView);

        Drawable areaBackground = typedArray.getDrawable(R.styleable.DragView_areaDragBackground);
        if (null != areaBackground) {
            if (areaBackground instanceof BitmapDrawable) {
                mAreaBitmap = ((BitmapDrawable) areaBackground).getBitmap();
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_PIC;
            } else if (areaBackground instanceof GradientDrawable) {
                mAreaBitmap = drawable2Bitmap(areaBackground);
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_XML;
            } else if (areaBackground instanceof ColorDrawable) {
                mAreaColor = ((ColorDrawable) areaBackground).getColor();
            } else {
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
            }
        } else {
            mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
        }

        int direction = typedArray.getInteger(R.styleable.DragView_direction, 0);
        switch (direction) {
            case UP:
                mDirection = UP;
                break;
            case DOWN:
                mDirection = DOWN;
                break;
            case LEFT:
                mDirection = LEFT;
                break;
            case RIGHT:
                mDirection = RIGHT;
                break;
            default:
                mDirection = UP;
                break;
        }

        Drawable textBackground = typedArray.getDrawable(R.styleable.DragView_textBackground);
        if (null != textBackground) {
            if (textBackground instanceof BitmapDrawable) {
                mTextBitmap = ((BitmapDrawable) textBackground).getBitmap();
                mTextBackgroundMode = AREA_BACKGROUND_MODE_PIC;
            } else if (textBackground instanceof GradientDrawable) {
                mTextBitmap = drawable2Bitmap(textBackground);
                mTextBackgroundMode = AREA_BACKGROUND_MODE_XML;
            } else if (textBackground instanceof ColorDrawable) {
                mTextColor = ((ColorDrawable) textBackground).getColor();
            } else {
                mTextBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
            }
        } else {
            mTextBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;

        }

        typedArray.recycle();
    }

    /**
     * Drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                mCenterPoint = getAreaPositionPoint(mCenterPoint, new Point((int) moveX, (int) moveY));
                moveArea(mCenterPoint.x, mCenterPoint.y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    private Point getAreaPositionPoint(Point centerPoint, Point touchPoint) {
        // 两点在X轴的距离
        float lenX = (float) (touchPoint.x - centerPoint.x);
        // 两点在Y轴距离
        float lenY = (float) (touchPoint.y - centerPoint.y);

        LogUtils.i("touchPointX ==" +  centerPoint.x + "touchPointY == " + centerPoint.y );

        int showPointX = centerPoint.x;
        int showPointY = centerPoint.y;

        if (lenY >= 0 && lenY <= mAreaLength) {
            showPointY = touchPoint.y;
            if (showPointY < mAreaLength) {
                showPointY = mAreaLength;
            }
        } else {

        }
        LogUtils.i("showPointX ==" + showPointX + "showPointY == " + showPointY + "mAreaLength==" + mAreaLength );
        return new Point(showPointX, showPointY);
    }

    private void moveArea(int x, int y) {
        mCenterPoint.set(x, y);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int measuredWidth = getMeasuredWidth();
        int width = getWidth();
        int height = getHeight();
        int measuredHeight = getMeasuredHeight();

        int cx = measuredWidth / 2;
        int cy = measuredHeight / 2;

        if (0 == mCenterPoint.x || 0 == mCenterPoint.y) {

            mCenterPoint.set(cx, cy);
        }

        mAreaLength = (measuredWidth <= measuredHeight) ? measuredWidth : measuredHeight;

        if (AREA_BACKGROUND_MODE_PIC == mAreaBackgroundMode || AREA_BACKGROUND_MODE_XML == mAreaBackgroundMode) {
            Rect src = new Rect(0, 0, mAreaBitmap.getWidth(), mAreaBitmap.getHeight());
            Rect dst = new Rect(mCenterPoint.x - cx, mCenterPoint.y - cy,
                    mCenterPoint.x + cx, mCenterPoint.y + cy);
            canvas.drawBitmap(mAreaBitmap, src, dst, mAreaBackgroundPaint);
        } else if (AREA_BACKGROUND_MODE_COLOR == mAreaBackgroundMode) {
            mAreaBackgroundPaint.setColor(mAreaColor);
            canvas.drawRect(mCenterPoint.x - cx, mCenterPoint.y - cy,
                    mCenterPoint.x + cx, mCenterPoint.y + cy, mAreaBackgroundPaint);
        } else {
            mAreaBackgroundPaint.setColor(Color.GRAY);
            canvas.drawRect(mCenterPoint.x - cx, mCenterPoint.y - cy,
                    mCenterPoint.x + cx, mCenterPoint.y + cy, mAreaBackgroundPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth, measureHeight;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            // 具体的值和match_parent
            measureWidth = widthSize;
        } else {
            measureWidth = DEFAULT_SIZE_WIDTH;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight = heightSize;
        } else {
            measureHeight = DEFAULT_SIZE_HEIGHT;
        }

        setMeasuredDimension(measureWidth, measureHeight);
    }
}
