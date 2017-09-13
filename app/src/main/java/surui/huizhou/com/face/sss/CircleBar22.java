package surui.huizhou.com.face.sss;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import surui.huizhou.com.face.R;


public class CircleBar22 extends View {
    private static final String TAG = "CircleBar";

    private RectF mColorWheelRectangle;//圆圈的矩形范围
    private Paint mDefaultWheelPaint;////绘制底部灰色圆圈的画笔
    private Paint mColorWheelPaint;//绘制蓝色扇形的画笔
    private float mSweepAnglePer = 50;//扇形弧度百分比
    private int progress;//播放进度
    private float total = 200;//总进度
    private int circleStrokeWidth = 20;//圆环的粗细
    private Paint textPaint;//中间数值文字的画笔
    private int mTextSize = circleStrokeWidth;//文字大小
    private int mTextColor = getResources().getColor(R.color.blue);//默认文字颜色
    private int mWheelColor = getResources().getColor(R.color.blue);//默认圆环颜色
    private BarAnimation anim;//动画
    private int TIME = 1000;//时间
    private int min;

    public CircleBar22(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleBar22(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
//        mTextSize = dip2px(getContext(), circleStrokeWidth);
        //默认圆的画笔
        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(getResources().getColor(R.color.gray));
        mDefaultWheelPaint.setStyle(Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);//圆圈的线条粗细
        mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);//开启显示边缘为圆形

        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorWheelPaint.setColor(mWheelColor);
        mColorWheelPaint.setStyle(Style.STROKE);
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth);//圆圈的线条粗细
        mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);//开启显示边缘为圆形
        //数值的画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(mTextColor);
        textPaint.setStyle(Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(mTextSize);

        anim = new BarAnimation();
        anim.setDuration(TIME);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-90, (min + circleStrokeWidth / 2) / 2, (min + circleStrokeWidth / 2) / 2);
//        canvas.translate(-circleStrokeWidth, -circleStrokeWidth);
        canvas.drawArc(mColorWheelRectangle, 5, 80, false, mDefaultWheelPaint);//画外接的圆环
        canvas.drawArc(mColorWheelRectangle, 5, mSweepAnglePer, false, mColorWheelPaint);//画圆环
        canvas.drawText(total + "", 0 + circleStrokeWidth, mColorWheelRectangle.bottom, textPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        min = Math.min(width, height);
        setMeasuredDimension(min + circleStrokeWidth / 2, min + circleStrokeWidth / 2);
        mColorWheelRectangle = new RectF(-min, -min, min, min);//圆圈的矩形范围
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.startAnimation(anim);
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public void setPressed(boolean pressed) {
        Log.e("sss", "sfasfasdfas");
    }

    public class BarAnimation extends Animation {

        public BarAnimation() {

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mSweepAnglePer = progress / total * 80 * interpolatedTime;
            } else {
                mSweepAnglePer = progress / total * 80;
            }
            postInvalidate();
        }
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}