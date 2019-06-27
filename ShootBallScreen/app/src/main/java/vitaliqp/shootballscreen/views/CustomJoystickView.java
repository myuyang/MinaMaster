package vitaliqp.shootballscreen.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

import vitaliqp.shootballscreen.R;
import vitaliqp.shootballscreen.enums.JoystickDirection;
import vitaliqp.shootballscreen.enums.JoystickDirectionMode;
import vitaliqp.shootballscreen.enums.JoystickViewCallBackMode;

import static com.blankj.utilcode.util.ImageUtils.drawable2Bitmap;

/**
 * 类名：vitaliqp.shootballscreen.views
 * 时间：2018/12/20 下午1:02
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author qp
 */
public class CustomJoystickView extends View {

    private static final int DEFAULT_SIZE = 200;
    private static final int DEFAULT_JOYSTICK_RADIUS = DEFAULT_SIZE / 4;
    /**
     * 角度
     */
    private static final double ANGLE_0 = 0;
    private static final double ANGLE_360 = 360;
    /**
     * 360°水平方向平分2份的边缘角度
     */
    private static final double ANGLE_HORIZONTAL_2D_OF_0P = 90;
    private static final double ANGLE_HORIZONTAL_2D_OF_1P = 270;

    /**
     * 360°垂直方向平分2份的边缘角度
     */
    private static final double ANGLE_VERTICAL_2D_OF_0P = 0;
    private static final double ANGLE_VERTICAL_2D_OF_1P = 180;

    /**
     * 360°平分4份的边缘角度
     */
    private static final double ANGLE_4D_OF_0P = 0;
    private static final double ANGLE_4D_OF_1P = 90;
    private static final double ANGLE_4D_OF_2P = 180;
    private static final double ANGLE_4D_OF_3P = 270;

    /**
     * 360°平分4份的边缘角度(旋转45度)
     */
    private static final double ANGLE_ROTATE45_4D_OF_0P = 45;
    private static final double ANGLE_ROTATE45_4D_OF_1P = 135;
    private static final double ANGLE_ROTATE45_4D_OF_2P = 225;
    private static final double ANGLE_ROTATE45_4D_OF_3P = 315;

    /**
     * 360°平分8份的边缘角度
     */
    private static final double ANGLE_8D_OF_0P = 22.5;
    private static final double ANGLE_8D_OF_1P = 67.5;
    private static final double ANGLE_8D_OF_2P = 112.5;
    private static final double ANGLE_8D_OF_3P = 157.5;
    private static final double ANGLE_8D_OF_4P = 202.5;
    private static final double ANGLE_8D_OF_5P = 247.5;
    private static final double ANGLE_8D_OF_6P = 292.5;
    private static final double ANGLE_8D_OF_7P = 337.5;

    /**
     * 摇杆可移动区域背景
     */
    private static final int AREA_BACKGROUND_MODE_PIC = 0;
    private static final int AREA_BACKGROUND_MODE_COLOR = 1;
    private static final int AREA_BACKGROUND_MODE_XML = 2;
    private static final int AREA_BACKGROUND_MODE_DEFAULT = 3;

    /**
     * 摇杆背景
     */
    private static final int JOYSTICK_BACKGROUND_MODE_PIC = 4;
    private static final int JOYSTICK_BACKGROUND_MODE_COLOR = 5;
    private static final int JOYSTICK_BACKGROUND_MODE_XML = 6;
    private static final int JOYSTICK_BACKGROUND_MODE_DEFAULT = 7;
    private final Paint mAreaBackgroundPaint;
    private final Paint mJoyStickPaint;
    private final Point mCenterPoint;
    private int mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
    private int mJoystickBackgroundMode = JOYSTICK_BACKGROUND_MODE_DEFAULT;
    private JoystickViewCallBackMode mJoystickViewCallBackMode = JoystickViewCallBackMode.CALL_BACK_MODE_MOVE;
    private JoystickDirection mTempJoystickDirection = JoystickDirection.DIRECTION_CENTER;
    private OnDistanceChangeListener mOnDistanceChangeListener;
    private OnAngleChangeListener mOnAngleChangeListener;
    private OnShakeListener mOnShakeListener;
    private JoystickDirectionMode mJoystickDirectionMode;
    private Point mJoyStickPoint;
    private Bitmap mAreaBitmap;
    private Bitmap mJoystickBitmap;
    private int mAreaColor;
    private int mJoystickColor;
    private int mJoystickRadius;
    private int mAreaRadius;

    public CustomJoystickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //获取自定义属性
        initAttribute(context, attrs);

        //目的是为了防止窗口泄露
        if (isInEditMode()) {
            //log
        }

        //移动区域画笔
        mAreaBackgroundPaint = new Paint();
        mAreaBackgroundPaint.setAntiAlias(true);

        //摇杆画笔
        mJoyStickPaint = new Paint();
        mJoyStickPaint.setAntiAlias(true);

        //中心点
        mCenterPoint = new Point();
        //摇杆位置
        mJoyStickPoint = new Point();
    }

    /**
     * 获取属性
     *
     * @param context
     * @param attrs
     */
    private void initAttribute(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JoystickView);

        //可移动区域背景
        Drawable areaBackground = typedArray.getDrawable(R.styleable.JoystickView_areaBackground);
        if (null != areaBackground) {
            //有设置过背景
            if (areaBackground instanceof BitmapDrawable) {
                //设置为图片
                mAreaBitmap = ((BitmapDrawable) areaBackground).getBitmap();
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_PIC;
            } else if (areaBackground instanceof GradientDrawable) {
                //设置为xml
                mAreaBitmap = drawable2Bitmap(areaBackground);
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_XML;
            } else if (areaBackground instanceof ColorDrawable) {
                //色值
                mAreaColor = ((ColorDrawable) areaBackground).getColor();
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_COLOR;

            } else {
                //其他形式
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
            }
        } else {
            //没有设置背景
            mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
        }

        //摇杆背景
        //可移动区域背景
        Drawable joystickBackground = typedArray.getDrawable(R.styleable.JoystickView_joystickBackground);
        if (null != joystickBackground) {
            //有设置过背景
            if (joystickBackground instanceof BitmapDrawable) {
                //设置为图片
                mJoystickBitmap = ((BitmapDrawable) joystickBackground).getBitmap();
                mJoystickBackgroundMode = JOYSTICK_BACKGROUND_MODE_PIC;
            } else if (joystickBackground instanceof GradientDrawable) {
                //设置为xml
                mJoystickBitmap = drawable2Bitmap(joystickBackground);
                mJoystickBackgroundMode = JOYSTICK_BACKGROUND_MODE_XML;
            } else if (joystickBackground instanceof ColorDrawable) {
                //色值
                mJoystickColor = ((ColorDrawable) joystickBackground).getColor();
                mJoystickBackgroundMode = JOYSTICK_BACKGROUND_MODE_COLOR;

            } else {
                //其他形式
                mJoystickBackgroundMode = JOYSTICK_BACKGROUND_MODE_DEFAULT;
            }
        } else {
            //没有设置背景
            mJoystickBackgroundMode = JOYSTICK_BACKGROUND_MODE_DEFAULT;
        }

        //摇杆半径
        mJoystickRadius = typedArray.getDimensionPixelOffset(R.styleable.JoystickView_joystickRadius, DEFAULT_JOYSTICK_RADIUS);

        typedArray.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                callBackStart();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                mJoyStickPoint = getJoystickPositionPoint(mCenterPoint, new Point((int) moveX, (int) moveY), mAreaRadius, mJoystickRadius);
                moveJoystick(mJoyStickPoint.x, mJoyStickPoint.y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                callBackFinish();
                //获取抬起的位置
                float upX = event.getX();
                float upY = event.getY();
                moveJoystick(mCenterPoint.x, mCenterPoint.y);
                break;
            default:
                break;
        }
        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int cx = measuredWidth / 2;
        int cy = measuredHeight / 2;

        // 中心点
        mCenterPoint.set(cx, cy);
        // 可移动区域的半径
        mAreaRadius = (measuredWidth <= measuredHeight) ? cx : cy;

        if (0 == mJoyStickPoint.x || 0 == mJoyStickPoint.y) {
            mJoyStickPoint.set(mCenterPoint.x, mCenterPoint.y);
        }

        // 画可移动区域
        if (AREA_BACKGROUND_MODE_PIC == mAreaBackgroundMode || AREA_BACKGROUND_MODE_XML == mAreaBackgroundMode) {
            // 图片
            Rect src = new Rect(0, 0, mAreaBitmap.getWidth(), mAreaBitmap.getHeight());
            Rect dst = new Rect(mCenterPoint.x - mAreaRadius, mCenterPoint.y - mAreaRadius, mCenterPoint.x + mAreaRadius, mCenterPoint.y + mAreaRadius);
            canvas.drawBitmap(mAreaBitmap, src, dst, mAreaBackgroundPaint);
        } else if (AREA_BACKGROUND_MODE_COLOR == mAreaBackgroundMode) {
            // 色值
            mAreaBackgroundPaint.setColor(mAreaColor);
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mAreaRadius, mAreaBackgroundPaint);
        } else {
            // 其他或者未设置
            mAreaBackgroundPaint.setColor(Color.GRAY);
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mAreaRadius, mAreaBackgroundPaint);
        }

        //画摇杆

        if (JOYSTICK_BACKGROUND_MODE_PIC == mJoystickBackgroundMode || JOYSTICK_BACKGROUND_MODE_XML == mJoystickBackgroundMode) {
            //图片类型
            Rect src = new Rect(0, 0, mJoystickBitmap.getWidth(), mJoystickBitmap.getHeight());
            Rect dst = new Rect(mJoyStickPoint.x - mJoystickRadius, mJoyStickPoint.y - mJoystickRadius, mJoyStickPoint.x + mJoystickRadius, mJoyStickPoint.y + mJoystickRadius);

            canvas.drawBitmap(mJoystickBitmap, src, dst, mJoyStickPaint);
        } else if (JOYSTICK_BACKGROUND_MODE_COLOR == mJoystickBackgroundMode) {
            //色值
            mJoyStickPaint.setColor(mJoystickColor);
            canvas.drawCircle(mJoyStickPoint.x, mJoyStickPoint.y, mJoystickRadius, mJoyStickPaint);
        } else {
            //默认
            mJoyStickPaint.setColor(Color.WHITE);
            canvas.drawCircle(mJoyStickPoint.x, mJoyStickPoint.y, mJoystickRadius, mJoyStickPaint);
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
            measureWidth = DEFAULT_SIZE;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight = heightSize;
        } else {
            measureHeight = DEFAULT_SIZE;
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private void callBackStart() {

        mTempJoystickDirection = JoystickDirection.DIRECTION_CENTER;
        if (null != mOnDistanceChangeListener) {
            mOnDistanceChangeListener.onStart();
        }
        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener.onStart();
        }
        if (null != mOnShakeListener) {
            mOnShakeListener.onStart();
        }
    }

    /**
     * 获取摇杆实际要显示的位置（点）
     *
     * @param centerPoint    中心点
     * @param touchPoint     触摸点
     * @param regionRadius   摇杆可活动区域半径
     * @param jotstickRadius 摇杆半径
     * @return 摇杆实际显示的位置（点）
     */
    private Point getJoystickPositionPoint(Point centerPoint, Point touchPoint, float regionRadius, float jotstickRadius) {
        // 两点在X轴的距离
        float lenX = (float) (touchPoint.x - centerPoint.x);
        // 两点在Y轴距离
        float lenY = (float) (touchPoint.y - centerPoint.y);
        // 两点距离
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 计算弧度
        double radian = Math.acos(lenX / lenXY) * (touchPoint.y < centerPoint.y ? -1 : 1);
        // 计算角度
        double angle = radian2Angle(radian);
        // 触摸位置在可活动范围内
        if (lenXY + jotstickRadius <= regionRadius) {
            callBack(angle, (int) lenXY, (int) lenX, (int) lenY);
            return touchPoint;
        } else {
            // 触摸位置在可活动范围以外
            // 计算要显示的位置
            int showPointX = (int) (centerPoint.x + (regionRadius - jotstickRadius) * Math.cos(radian));
            int showPointY = (int) (centerPoint.y + (regionRadius - jotstickRadius) * Math.sin(radian));
            // 项目所需显示的点
            int x = (int) (regionRadius * Math.cos(radian));
            int y = (int) (regionRadius * Math.sin(radian));
            int distance = (int) Math.sqrt((showPointX - centerPoint.x) * (showPointX - centerPoint.x) + (showPointY - centerPoint.y) * (showPointY - centerPoint.y));
            callBack(angle, distance, x, y);
            return new Point(showPointX, showPointY);
        }
    }

    /**
     * 移动摇杆
     *
     * @param x
     * @param y
     */
    private void moveJoystick(float x, float y) {

        mJoyStickPoint.set((int) x, (int) y);

        invalidate();
    }

    private void callBackFinish() {
        mTempJoystickDirection = JoystickDirection.DIRECTION_CENTER;
        if (null != mOnDistanceChangeListener) {
            mOnDistanceChangeListener.onFinish();
        }
        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener.onFinish();
        }
        if (null != mOnShakeListener) {
            mOnShakeListener.onFinish();
        }
    }

    private double radian2Angle(double radian) {
        double tmp = Math.round(radian / Math.PI * 180);
        return tmp >= 0 ? tmp : 360 + tmp;
    }

    private void callBack(double angle, int distance, int distanceX, int distanceY) {

        //可选择不用
        if (null != mOnShakeListener) {
            handleShakeListener(angle);
        }

        if (null != mOnDistanceChangeListener) {
            mOnDistanceChangeListener.distance(distance, distanceX, distanceY);
        }

        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener.angle(angle);
        }
    }

    private void handleShakeListener(double angle) {

        if (JoystickViewCallBackMode.CALL_BACK_MODE_MOVE == mJoystickViewCallBackMode) {
            switch (mJoystickDirectionMode) {
                // 左右方向
                case DIRECTION_2_HORIZONTAL:
                    boolean right_2_h = ANGLE_0 <= angle && ANGLE_HORIZONTAL_2D_OF_0P > angle || ANGLE_HORIZONTAL_2D_OF_1P <= angle && ANGLE_360 > angle;
                    if (right_2_h) {
                        // 右
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_RIGHT);
                    } else if (ANGLE_HORIZONTAL_2D_OF_0P <= angle && ANGLE_HORIZONTAL_2D_OF_1P > angle) {
                        // 左
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_LEFT);
                    }
                    break;
                // 上下方向
                case DIRECTION_2_VERTICAL:
                    if (ANGLE_VERTICAL_2D_OF_0P <= angle && ANGLE_VERTICAL_2D_OF_1P > angle) {
                        // 下
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN);
                    } else if (ANGLE_VERTICAL_2D_OF_1P <= angle && ANGLE_360 > angle) {
                        // 上
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP);
                    }
                    break;
                // 四个方向
                case DIRECTION_4_ROTATE_0:
                    if (ANGLE_4D_OF_0P <= angle && ANGLE_4D_OF_1P > angle) {
                        // 右下
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN_RIGHT);
                    } else if (ANGLE_4D_OF_1P <= angle && ANGLE_4D_OF_2P > angle) {
                        // 左下
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN_LEFT);
                    } else if (ANGLE_4D_OF_2P <= angle && ANGLE_4D_OF_3P > angle) {
                        // 左上
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP_LEFT);
                    } else if (ANGLE_4D_OF_3P <= angle && ANGLE_360 > angle) {
                        // 右上
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP_RIGHT);
                    }
                    break;
                // 四个方向 旋转45度
                case DIRECTION_4_ROTATE_45:
                    boolean right_4_shake = ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle;
                    if (right_4_shake) {
                        // 右
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_RIGHT);
                    } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle) {
                        // 下
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN);
                    } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle) {
                        // 左
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_LEFT);
                    } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle) {
                        // 上
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP);
                    }
                    break;
                // 八个方向
                case DIRECTION_8:
                    boolean right_8_shake = ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle;
                    if (right_8_shake) {
                        // 右
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_RIGHT);
                    } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle) {
                        // 右下
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN_RIGHT);
                    } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle) {
                        // 下
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN);
                    } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle) {
                        // 左下
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN_LEFT);
                    } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle) {
                        // 左
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_LEFT);
                    } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle) {
                        // 左上
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP_LEFT);
                    } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle) {
                        // 上
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP);
                    } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle) {
                        // 右上
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP_RIGHT);
                    }
                    break;
                default:
                    break;
            }
        } else if (JoystickViewCallBackMode.CALL_BACK_MODE_STATE_CHANGE == mJoystickViewCallBackMode) {
            switch (mJoystickDirectionMode) {
                // 左右方向
                case DIRECTION_2_HORIZONTAL:
                    boolean right_2_h = (ANGLE_0 <= angle && ANGLE_HORIZONTAL_2D_OF_0P > angle || ANGLE_HORIZONTAL_2D_OF_1P <= angle && ANGLE_360 > angle) && mTempJoystickDirection != JoystickDirection.DIRECTION_RIGHT;
                    if (right_2_h) {
                        // 右
                        mTempJoystickDirection = JoystickDirection.DIRECTION_RIGHT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_RIGHT);
                    } else if (ANGLE_HORIZONTAL_2D_OF_0P <= angle && ANGLE_HORIZONTAL_2D_OF_1P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_LEFT) {
                        // 左
                        mTempJoystickDirection = JoystickDirection.DIRECTION_LEFT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_LEFT);
                    }
                    break;
                // 上下方向
                case DIRECTION_2_VERTICAL:
                    if (ANGLE_VERTICAL_2D_OF_0P <= angle && ANGLE_VERTICAL_2D_OF_1P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_DOWN) {
                        // 下
                        mTempJoystickDirection = JoystickDirection.DIRECTION_DOWN;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN);
                    } else if (ANGLE_VERTICAL_2D_OF_1P <= angle && ANGLE_360 > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_UP) {
                        // 上
                        mTempJoystickDirection = JoystickDirection.DIRECTION_UP;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP);
                    }
                    break;
                // 四个方向
                case DIRECTION_4_ROTATE_0:
                    if (ANGLE_4D_OF_0P <= angle && ANGLE_4D_OF_1P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_DOWN_RIGHT) {
                        // 右下
                        mTempJoystickDirection = JoystickDirection.DIRECTION_DOWN_RIGHT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN_RIGHT);
                    } else if (ANGLE_4D_OF_1P <= angle && ANGLE_4D_OF_2P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_DOWN_LEFT) {
                        // 左下
                        mTempJoystickDirection = JoystickDirection.DIRECTION_DOWN_LEFT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN_LEFT);
                    } else if (ANGLE_4D_OF_2P <= angle && ANGLE_4D_OF_3P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_UP_LEFT) {
                        // 左上
                        mTempJoystickDirection = JoystickDirection.DIRECTION_UP_LEFT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP_LEFT);
                    } else if (ANGLE_4D_OF_3P <= angle && ANGLE_360 > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_UP_RIGHT) {
                        // 右上
                        mTempJoystickDirection = JoystickDirection.DIRECTION_UP_RIGHT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP_RIGHT);
                    }
                    break;
                // 四个方向 旋转45度
                case DIRECTION_4_ROTATE_45:
                    boolean right_4 = (ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) && mTempJoystickDirection != JoystickDirection.DIRECTION_RIGHT;
                    if (right_4) {
                        // 右
                        mTempJoystickDirection = JoystickDirection.DIRECTION_RIGHT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_RIGHT);
                    } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_DOWN) {
                        // 下
                        mTempJoystickDirection = JoystickDirection.DIRECTION_DOWN;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN);
                    } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_LEFT) {
                        // 左
                        mTempJoystickDirection = JoystickDirection.DIRECTION_LEFT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_LEFT);
                    } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_UP) {
                        // 上
                        mTempJoystickDirection = JoystickDirection.DIRECTION_UP;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP);
                    }
                    break;
                // 八个方向
                case DIRECTION_8:
                    boolean right_8 = (ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle) && mTempJoystickDirection != JoystickDirection.DIRECTION_RIGHT;
                    if (right_8) {
                        // 右
                        mTempJoystickDirection = JoystickDirection.DIRECTION_RIGHT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_RIGHT);
                    } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_DOWN_RIGHT) {
                        // 右下
                        mTempJoystickDirection = JoystickDirection.DIRECTION_DOWN_RIGHT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN_RIGHT);
                    } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_DOWN) {
                        // 下
                        mTempJoystickDirection = JoystickDirection.DIRECTION_DOWN;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN);
                    } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_DOWN_LEFT) {
                        // 左下
                        mTempJoystickDirection = JoystickDirection.DIRECTION_DOWN_LEFT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_DOWN_LEFT);
                    } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_LEFT) {
                        // 左
                        mTempJoystickDirection = JoystickDirection.DIRECTION_LEFT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_LEFT);
                    } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_UP_LEFT) {
                        // 左上
                        mTempJoystickDirection = JoystickDirection.DIRECTION_UP_LEFT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP_LEFT);
                    } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_UP) {
                        // 上
                        mTempJoystickDirection = JoystickDirection.DIRECTION_UP;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP);
                    } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle && mTempJoystickDirection != JoystickDirection.DIRECTION_UP_RIGHT) {
                        // 右上
                        mTempJoystickDirection = JoystickDirection.DIRECTION_UP_RIGHT;
                        mOnShakeListener.direction(JoystickDirection.DIRECTION_UP_RIGHT);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置回调模式
     *
     * @param mode 回调模式
     */
    public void setJoystickViewCallBackMode(JoystickViewCallBackMode mode) {
        mJoystickViewCallBackMode = mode;
    }

    /**
     * 添加摇杆摇动角度的监听
     *
     * @param listener 回调接口
     */
    public void setOnAngleChangeListener(OnAngleChangeListener listener) {
        if (null != null) {
            mOnAngleChangeListener = listener;
        }
    }

    /**
     * 添加摇杆摇动角度的监听
     *
     * @param listener 回调接口
     */
    public void setOnDistanceChangeListener(OnDistanceChangeListener listener) {
        if (null != listener) {
            mOnDistanceChangeListener = listener;
        }
    }

    /**
     * 添加摇动的监听
     *
     * @param joystickDirectionMode 监听的方向
     * @param listener              回调
     */
    public void setOnShakeListener(JoystickDirectionMode joystickDirectionMode, OnShakeListener listener) {
        mJoystickDirectionMode = joystickDirectionMode;

        if (null != listener) {
            mOnShakeListener = listener;
        }
    }

    /**
     * 摇动方向监听接口
     */
    public interface OnShakeListener {
        /**
         * 开始
         */
        void onStart();

        /**
         * 摇动方向
         *
         * @param joystickDirection 方向
         */
        void direction(JoystickDirection joystickDirection);

        /**
         * 结束
         */
        void onFinish();
    }

    /**
     * 摇动角度的监听接口
     */
    public interface OnAngleChangeListener {
        /**
         * 开始
         */

        void onStart();

        /**
         * 摇杆角度变化
         *
         * @param angle 角度[0,360)
         */
        void angle(double angle);

        /**
         * 结束
         */
        void onFinish();
    }

    /**
     * 摇动距离的监听接口
     */
    public interface OnDistanceChangeListener {
        /**
         * 开始
         */

        void onStart();

        /**
         * distance
         *
         * @param distance
         * @param distanceX
         * @param distanceY
         */
        void distance(int distance, int distanceX, int distanceY);

        /**
         * 结束
         */
        void onFinish();
    }
}


