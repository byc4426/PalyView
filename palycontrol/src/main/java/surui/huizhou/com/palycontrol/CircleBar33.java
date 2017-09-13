package surui.huizhou.com.palycontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;


public class CircleBar33 extends View {
    private static final String TAG = "CircleBar";
    private RectF mVolMuteRectangle;//静音圆环的矩形范围
    private Paint mVolMutePaint;////静音灰色圆圈的画笔
    private int mVolMuteWidth;//静音圆环宽高
    private int volRank = 2;//音量级别，分为高3。中2。低1.
    private boolean isMute;//是否静音

    private Paint mVol;////音量画笔

    private RectF mVolDefaultRectangle;//音量圆环的矩形范围
    private Paint mVolDefaultPaint;////音量灰色圆圈的画笔
    private int mVolDefaultWidth;//音量圆环宽高
    private int volDefaultStrokeWidth = 100;//音量圆环的粗细
    private RectF mVolRectangle;//音量进度圆环的矩形范围
    private Paint mVolPaint;////音量进度圆圈的画笔
    private int[] doughnutColors = new int[]{0x7F01FA33, 0x7F66F931, 0x7FB6F703, 0x7FDEF701,
            Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW,
            Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW,
            Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW};
    private int mVolWidth;//音量进度圆环宽高
    private int volStrokeWidth = 70;//音量进度圆环的粗细
    private double vol = 80;//音量大小
    private double mVolPer = vol * 90 / 100;
    private static double valss;

    private RectF mButRectangle;//按钮圆环的矩形范围
    private Paint mButPaint;////绘制底部灰色圆圈的画笔
    private int butStyle;//按了按钮 1后退/2播放/3快进/静音4/正常0
    private Paint mPaint;
    private int butStrokeWidth = 100;//按钮圆环的粗细
    private int mButWidth;//按钮圆环宽高
    private Bitmap next;
    private Bitmap prev;
    private Bitmap pause;
    private Bitmap play;
    private Bitmap isPlayBit;
    private boolean isPlay;

    private Bitmap volBit;
    private Bitmap vol_min;
    private Bitmap vol_max;
    private Bitmap vol_mid;
    private Bitmap vol_mute;
    private Bitmap vol_arrow_n;
    private Bitmap vol_arrow_p;
    private Bitmap vol_arrow;
    private Bitmap indicator_n;
    private Bitmap indicator_p;
    private Bitmap indicator;

    private Paint mBackPaint;//
    private RectF mProgressRectangle;//播放进度的矩形范围
    private Paint mDefaultWheelPaint;////绘制播放进度背景圆环底部灰色圆圈的画笔
    private Paint mProgressPaint;//播放进度的画笔
    private int mWidth;//控件宽高
    private double mProgressPer;
    private double progress;//播放进度
    private double total = 120;//总进度
    private int circleStrokeWidth = 30;//圆环的粗细
    private Paint textPaint;//总时长文字的画笔
    private int mTextSize = circleStrokeWidth;//文字大小
    private int mTextColor = 0xffffffff;//默认文字颜色
    private int mProgressColor = 0xff3f86f5;//默认播放进度的圆环颜色
    private BarAnimation anim;//动画
    private int TIME = 500;//时间
    private int min;
    private boolean isvol;

    public void setOnPlayListener(OnPlayListener onPlayListener) {
        this.onPlayListener = onPlayListener;
    }

    private OnPlayListener onPlayListener;


    public CircleBar33(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleBar33(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        //播放进度圆的画笔
        mDefaultWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultWheelPaint.setColor(0xff595959);
        mDefaultWheelPaint.setStyle(Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth);//圆圈的线条粗细
        mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);//开启显示边缘为圆形

        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackPaint.setColor(0x1f000000);
        mBackPaint.setStyle(Style.STROKE);
        mBackPaint.setStrokeWidth(circleStrokeWidth * 2);//圆圈的线条粗细

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Style.STROKE);
        mProgressPaint.setStrokeWidth(circleStrokeWidth);//圆圈的线条粗细
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);//开启显示边缘为圆形
        //数值的画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setColor(mTextColor);
        textPaint.setStyle(Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(mTextSize);

        anim = new BarAnimation();
        anim.setDuration(TIME);

        next = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_next), 180);
        prev = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_prev), 180);
        play = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_play), 180);
        pause = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_pause), 180);
        isPlayBit = pause;
        vol_min = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_vol_min), 180);
        vol_max = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_vol_max), 180);
        vol_mid = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_vol_middle), 180);
        vol_mute = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_vol_mute), 180);
        volBit = vol_mid;
        vol_arrow_n = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_vol_arrow_normal), -110);
        vol_arrow_p = rotationBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_vol_arrow_pressed), -110);
        vol_arrow = vol_arrow_n;
        indicator_n = fitBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_indicator_normal), circleStrokeWidth + 15);
        indicator_p = fitBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_indicator_pressed), circleStrokeWidth + 15);
        indicator = indicator_n;
        //按钮圆环的画笔
        mButPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButPaint.setColor(0x1f000000);
        mButPaint.setStyle(Style.STROKE);
        mButPaint.setStrokeWidth(butStrokeWidth);//圆圈的线条粗细
        //按钮分割线
        mPaint = new Paint();
        mPaint.setColor(0xff595959);
        mPaint.setStyle(Style.FILL);
        mPaint.setAntiAlias(true);
//音量调节
        mVolDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVolDefaultPaint.setColor(0x7f000000);
        mVolDefaultPaint.setStyle(Style.STROKE);
        mVolDefaultPaint.setStrokeWidth(volDefaultStrokeWidth);//圆圈的线条粗细

        mVolPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVolPaint.setColor(0x7F60F928);
        mVolPaint.setStyle(Style.STROKE);
        mVolPaint.setStrokeWidth(volStrokeWidth);//圆圈的线条粗细
        if (doughnutColors.length > 1) {
            mVolPaint.setShader(new SweepGradient(0, 0, doughnutColors, null));
        } else {
            mVolPaint.setColor(0x7F60F928);
        }

        mVol = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVol.setColor(0x99F4EEEE);
        mVol.setStyle(Style.STROKE);
        mVol.setStrokeWidth(5);

//静音的
        mVolMutePaint = new Paint();
        mVolMutePaint.setColor(0x99707070);
        mVolMutePaint.setStyle(Style.FILL);
        mVolMutePaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-90, mWidth / 2, mWidth / 2);
        for (int i = 0; i < 30; i++) {
            canvas.drawLine(0, mVolMuteWidth + volStrokeWidth / 2 + volStrokeWidth / 2 / 30 * i, 0, mVolWidth, mVol);
            canvas.rotate(-3);
        }
        canvas.rotate(90);
        canvas.drawArc(mProgressRectangle, 0, 90, false, mBackPaint);//播放进度背景圆环
        canvas.drawArc(mProgressRectangle, 2, 86, false, mDefaultWheelPaint);//播放进度背景灰色圆环
        canvas.drawArc(mProgressRectangle, 2, (float) mProgressPer, false, mProgressPaint);//播放进度蓝色圆环
//        canvas.rotate(180);
        //先创建两个相同的圆形路径，并先画出两个路径原图
        Path circlePath = new Path();
        circlePath.addCircle(0, 0, min, Path.Direction.CW);
//        canvas.drawText("1:30:30", 0 - circleStrokeWidth * 4, mProgressRectangle.top + circleStrokeWidth / 2, textPaint);//总时长
        canvas.drawTextOnPath(total + "", circlePath, min + 50, circleStrokeWidth / 2 - 4, textPaint);
//        canvas.rotate(-180);
        mButPaint.setColor(0x1f000000);
        canvas.drawArc(mButRectangle, 0, 30, false, mButPaint);//后退按钮圆环
        canvas.drawArc(mButRectangle, 30, 30, false, mButPaint);//播放按钮圆环
        canvas.drawArc(mButRectangle, 60, 30, false, mButPaint);//快进按钮圆环
        mVolMutePaint.setColor(0x99707070);
        canvas.drawArc(mVolMuteRectangle, 0, 90, true, mVolMutePaint);//静音圆环
        switch (butStyle) {
            case 1:
                mButPaint.setColor(0xff3f86f5);
                canvas.drawArc(mButRectangle, 0, 30, false, mButPaint);//后退按钮圆环
                break;
            case 2:
                mButPaint.setColor(0xff3f86f5);
                canvas.drawArc(mButRectangle, 30, 30, false, mButPaint);//播放按钮圆环
                break;
            case 3:
                mButPaint.setColor(0xff3f86f5);
                canvas.drawArc(mButRectangle, 60, 30, false, mButPaint);//快进按钮圆环
                break;
            case 4:
                mVolMutePaint.setColor(0xff3f86f5);
                canvas.drawArc(mVolMuteRectangle, 0, 90, true, mVolMutePaint);//静音圆环
                break;
        }
        canvas.drawArc(mVolDefaultRectangle, 0, 90, false, mVolDefaultPaint);//音量圆环
        if (!isMute) {//静音就不画（绿色）
            canvas.drawArc(mVolRectangle, 0, (float) mVolPer, false, mVolPaint);//音量圆环（绿色）
        }
        canvas.drawBitmap(volBit, mVolMuteWidth / 4 - volBit.getHeight() / 4, mVolMuteWidth / 4 - volBit.getHeight() / 4, new Paint());
        canvas.rotate(-15);
        canvas.drawBitmap(next, 0 - next.getHeight() / 2, mButWidth - butStrokeWidth + next.getHeight() / 4, new Paint());
        canvas.rotate(-15);
        canvas.drawLine(0, mButWidth, 0, mButWidth - butStrokeWidth, mPaint);
        canvas.rotate(-15);
        canvas.drawBitmap(isPlayBit, 0 - isPlayBit.getHeight() / 2, mButWidth - butStrokeWidth + isPlayBit.getHeight() / 4, new Paint());
        canvas.rotate(-15);
        canvas.drawLine(0, mButWidth, 0, mButWidth - butStrokeWidth, mPaint);
        canvas.rotate(-15);
        canvas.drawBitmap(prev, 0 - prev.getHeight() / 2, mButWidth - butStrokeWidth + prev.getHeight() / 4, new Paint());
        if (isvol) {
            canvas.rotate(-8);
            canvas.rotate((float) (progress / total * 86));
            canvas.drawBitmap(indicator, 0, min - indicator.getWidth() / 2, new Paint());
            canvas.rotate(8);
            canvas.rotate(-(float) (progress / total * 86));
            canvas.rotate((float) vol / 100 * 90);
            canvas.drawBitmap(vol_arrow, 0, (mVolWidth - volStrokeWidth / 2) - (volDefaultStrokeWidth - volStrokeWidth), new Paint());
        } else {
            canvas.rotate((float) vol / 100 * 90);
            canvas.drawBitmap(vol_arrow, 0, (mVolWidth - volStrokeWidth / 2) - (volDefaultStrokeWidth - volStrokeWidth), new Paint());
            canvas.rotate(-(float) vol / 100 * 90);
            canvas.rotate(-8);
            canvas.rotate((float) (progress / total * 86));
            canvas.drawTextOnPath(progress + "", circlePath, min + 150, -circleStrokeWidth / 2, textPaint);
            canvas.drawBitmap(indicator, 0, min - indicator.getWidth() / 2, new Paint());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        min = Math.min(width, height);
        mWidth = min + circleStrokeWidth;
        mButWidth = mWidth - circleStrokeWidth * 2 - 20;
        mVolDefaultWidth = mButWidth - butStrokeWidth;
        mVolWidth = mVolDefaultWidth - (volDefaultStrokeWidth - volStrokeWidth);
        mVolMuteWidth = mVolDefaultWidth - volDefaultStrokeWidth;
        setMeasuredDimension(mWidth, mWidth);
        //播放进度圆圈的矩形范围
        mProgressRectangle = new RectF(-min, -min, min, min);//播放进度圆圈的矩形范围
        //按钮圆环矩形范围
        mButRectangle = new RectF(-(mButWidth - butStrokeWidth / 2), -(mButWidth - butStrokeWidth / 2), (mButWidth - butStrokeWidth / 2), (mButWidth - butStrokeWidth / 2));//按钮圆环矩形范围
        //音量控制圆圈的矩形范围(背景)
        mVolDefaultRectangle = new RectF(-(mVolDefaultWidth - volDefaultStrokeWidth / 2), -(mVolDefaultWidth - volDefaultStrokeWidth / 2), (mVolDefaultWidth - volDefaultStrokeWidth / 2), (mVolDefaultWidth - volDefaultStrokeWidth / 2));//音量控制圆圈的矩形范围(背景)
        //音量控制圆圈的矩形范围(绿色)
        mVolRectangle = new RectF(-(mVolWidth - volStrokeWidth / 2), -(mVolWidth - volStrokeWidth / 2), (mVolWidth - volStrokeWidth / 2), (mVolWidth - volStrokeWidth / 2));//音量控制圆圈的矩形范围(绿色)
        //静音的矩形范围
        mVolMuteRectangle = new RectF(-mVolMuteWidth, -mVolMuteWidth, mVolMuteWidth, mVolMuteWidth);//静音的矩形范围
    }

    public void setProgress(double progress) {
        this.progress = progress;
        this.startAnimation(anim);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setVol(double vol) {
        this.vol = vol;
        this.startAnimation(anim);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double dis = getDistance(0, mWidth, event.getX(), event.getY());
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (dis > (mWidth - circleStrokeWidth - 5) && dis <= mWidth + 5) {//播放进度
                isvol = false;
                indicator = indicator_p;
                double j = getCos(mWidth - circleStrokeWidth / 2, mWidth - circleStrokeWidth / 2, event.getX(), event.getY());
                mProgressPer = j - 2;
                progress = mProgressPer / 86 * total;
            }
            if (dis > (mVolDefaultWidth - volDefaultStrokeWidth) && dis <= mVolDefaultWidth) {//音量调节
                isMute = false;//取消静音
                isvol = true;
                mVolPer = getCos2(mVolWidth, mVolWidth, event.getX(), event.getY());
                vol = mVolPer / 90 * 100;
                if (vol > 100) {
                    vol = 100;
                }
                if (vol < 0) {
                    vol = 0;
                }
                if (mVolPer <= 30) {
                    volRank = 1;
                    volBit = vol_min;
                } else if (mVolPer <= 60) {
                    volRank = 2;
                    volBit = vol_mid;
                } else {
                    volRank = 3;
                    volBit = vol_max;
                }
                vol_arrow = vol_arrow_p;
                if (onPlayListener != null) {
                    onPlayListener.volumeListener(Double.valueOf(String.format("%.1f", vol)));
                }
            }
            if (dis > (mButWidth - butStrokeWidth) && dis <= mButWidth) {
                if (!isContain(new Point(0, mWidth - mButWidth), new Point(0, mWidth - mButWidth + butStrokeWidth),
                        calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 30),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 30),
                        new Point((int) event.getX(), (int) event.getY()))) {//后退健
                    butStyle = 1;
                }
                if (!isContain(calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 30),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 30),
                        calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 60),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 60),
                        new Point((int) event.getX(), (int) event.getY()))) {//播放暂停健
                    butStyle = 2;
                }
                if (!isContain(calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 60),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 60),
                        calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 90),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 90),
                        new Point((int) event.getX(), (int) event.getY()))) {//快进健
                    butStyle = 3;
                }
            }
            if (dis < mVolMuteWidth) {//静音
                butStyle = 4;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            butStyle = 0;
            if (dis > (mWidth - circleStrokeWidth - 5) && dis <= mWidth + 5) {//播放进度
                isvol = false;
                indicator = indicator_n;
                double j = getCos(mWidth - circleStrokeWidth / 2, mWidth - circleStrokeWidth / 2, event.getX(), event.getY());
                mProgressPer = j - 2;
                progress = mProgressPer / 86 * total;
                if (onPlayListener != null) {
                    onPlayListener.progressListener(total, progress, Double.valueOf(String.format("%.2f", mProgressPer / 86 * 100)));
                }
            }
            if (dis > (mButWidth - butStrokeWidth) && dis <= mButWidth) {
                if (!isContain(new Point(0, mWidth - mButWidth), new Point(0, mWidth - mButWidth + butStrokeWidth),
                        calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 30),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 30),
                        new Point((int) event.getX(), (int) event.getY()))) {//后退健
                    if (onPlayListener != null) {
                        onPlayListener.backListener();
                    }
                }
                if (!isContain(calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 30),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 30),
                        calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 60),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 60),
                        new Point((int) event.getX(), (int) event.getY()))) {//播放暂停健
                    if (!isPlay) {
                        isPlayBit = play;
                    } else {
                        isPlayBit = pause;
                    }
                    isPlay = !isPlay;
                    if (onPlayListener != null) {
                        onPlayListener.pauseListener(isPlay);
                    }
                }
                if (!isContain(calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 60),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 60),
                        calcNewPoint(new Point(0, mWidth - mButWidth), new Point(0, mWidth), 90),
                        calcNewPoint(new Point(0, mWidth - mButWidth + butStrokeWidth), new Point(0, mWidth), 90),
                        new Point((int) event.getX(), (int) event.getY()))) {//快进健
                    if (onPlayListener != null) {
                        onPlayListener.speedListener();
                    }
                }
            }
            if (dis > (mVolDefaultWidth - volDefaultStrokeWidth) && dis <= mVolDefaultWidth) {//音量调节
                isvol = true;
                mVolPer = getCos2(mVolWidth, mVolWidth, event.getX(), event.getY());
                vol = mVolPer / 90 * 100;
                if (vol > 100) {
                    vol = 100;
                }
                if (vol < 0) {
                    vol = 0;
                }
                if (mVolPer <= 30) {
                    volRank = 1;
                    volBit = vol_min;
                } else if (mVolPer <= 60) {
                    volRank = 2;
                    volBit = vol_mid;
                } else {
                    volRank = 3;
                    volBit = vol_max;
                }
                vol_arrow = vol_arrow_n;
                if (onPlayListener != null) {
                    onPlayListener.volumeListener(Double.valueOf(String.format("%.1f", vol)));
                }
            }
            if (dis < mVolMuteWidth) {//静音
                if (!isMute) {
                    volBit = vol_mute;
                    valss = vol;
                    vol = 0;
                } else {
                    vol = valss;
                    switch (volRank) {
                        case 1:
                            volBit = vol_min;
                            break;
                        case 2:
                            volBit = vol_mid;
                            break;
                        case 3:
                            volBit = vol_max;
                            break;
                    }
                }
                isMute = !isMute;
                if (onPlayListener != null) {
                    onPlayListener.muteListener(isMute);
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (dis > (mWidth - circleStrokeWidth - 5) && dis <= mWidth + 5) {//播放进度
                isvol = false;
                double j = getCos(mWidth - circleStrokeWidth / 2, mWidth - circleStrokeWidth / 2, event.getX(), event.getY());
                mProgressPer = j - 2;
                progress = mProgressPer / 86 * total;
            }

            if (dis > (mVolDefaultWidth - volDefaultStrokeWidth) && dis <= mVolDefaultWidth) {//音量调节
                isvol = true;
                mVolPer = getCos2(mVolWidth, mVolWidth, event.getX(), event.getY());
                vol = mVolPer / 90 * 100;
                if (vol > 100) {
                    vol = 100;
                }
                if (vol < 0) {
                    vol = 0;
                }
                if (mVolPer <= 30) {
                    volRank = 1;
                    volBit = vol_min;
                } else if (mVolPer <= 60) {
                    volRank = 2;
                    volBit = vol_mid;
                } else {
                    volRank = 3;
                    volBit = vol_max;
                }
                if (onPlayListener != null) {
                    onPlayListener.volumeListener(Double.valueOf(String.format("%.1f", vol)));
                }
            }
        }
        postInvalidate();
        return true;
    }

    //两点间距离
    public double getDistance(float x1, float y1, float x2, float y2) {
        float a = x1 - x2;
        float b = y1 - y2;
        double dis = Math.sqrt((Math.pow(a, 2) + Math.pow(b, 2)));
        return dis;
    }

    private static Point calcNewPoint(Point p, Point pCenter, float angle) {
        // calc arc
        float l = (float) ((angle * Math.PI) / 180);
        //sin/cos value
        float cosv = (float) Math.cos(l);
        float sinv = (float) Math.sin(l);
        // calc new point
        float newX = (float) ((p.x - pCenter.x) * cosv - (p.y - pCenter.y) * sinv + pCenter.x);
        float newY = (float) ((p.x - pCenter.x) * sinv + (p.y - pCenter.y) * cosv + pCenter.y);
        return new Point((int) newX, (int) newY);
    }


    //求角度 2bc=(b²+c²-a²)/CosA
//    cosc＝（a^2+b^2-c^2)/2ab
    public double getCos(double b, double c, float x1, float y1) {
        double a = getDistance(0, circleStrokeWidth / 2, x1, y1);
        double d = (Math.pow(b, 2) + (Math.pow(c, 2) - Math.pow(a, 2))) / (2 * b * c);
        return Math.acos((d)) * 180 / Math.PI;
    }

    public double getCos2(double b, double c, float x1, float y1) {
        double a = getDistance(0, mWidth - mVolWidth, x1, y1);
        double d = (Math.pow(b, 2) + (Math.pow(c, 2) - Math.pow(a, 2))) / (2 * b * c);
        return Math.acos((d)) * 180 / Math.PI;
    }

    //判断点是否在矩形内，只要判断该点的横坐标和纵坐标是否夹在矩形的左右边和上下边之间
    public boolean isContain(Point mp1, Point mp2, Point mp3, Point mp4, Point mp) {
        if (Multiply(mp, mp1, mp2) * Multiply(mp, mp4, mp3) <= 0
                && Multiply(mp, mp4, mp1) * Multiply(mp, mp3, mp2) <= 0)
            return true;
        return false;
    }

    // 计算叉乘 |P0P1| × |P0P2|
    private double Multiply(Point p1, Point p2, Point p0) {
        return ((p1.x - p0.x) * (p2.y - p0.y) - (p2.x - p0.x) * (p1.y - p0.y));
    }

    private Bitmap rotationBitmap(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    public class BarAnimation extends Animation {

        public BarAnimation() {

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mProgressPer = progress / total * 86 * interpolatedTime;
                mVolPer = vol / 100 * 90 * interpolatedTime;
            } else {
                mProgressPer = progress / total * 86;
                mVolPer = vol / 100 * 90;
            }
            postInvalidate();
        }
    }

    /**
     * fuction: 设置固定的宽度，高度随之变化，使图片不会变形
     *
     * @param target   需要转化bitmap参数
     * @param newWidth 设置新的宽度
     * @return
     */
    public static Bitmap fitBitmap(Bitmap target, int newWidth) {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        // float scaleHeight = ((float)newHeight) / height;
        int newHeight = (int) (scaleWidth * height);
        matrix.postScale(scaleWidth, scaleWidth);
        // Bitmap result = Bitmap.createBitmap(target,0,0,width,height,
        // matrix,true);
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
        if (target != null && !target.equals(bmp) && !target.isRecycled()) {
            target.recycle();
            target = null;
        }
        return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
        // true);
    }

    /**
     * 颜色渐变算法
     * 获取某个百分比下的渐变颜色值
     *
     * @param percent
     * @param colors
     * @return
     */
    public static int getCurrentColor(float percent, int[] colors) {
        float[][] f = new float[colors.length][3];
        for (int i = 0; i < colors.length; i++) {
            f[i][0] = (colors[i] & 0xff0000) >> 16;
            f[i][1] = (colors[i] & 0x00ff00) >> 8;
            f[i][2] = (colors[i] & 0x0000ff);
        }
        float[] result = new float[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < f.length; j++) {
                if (f.length == 1 || percent == j / (f.length - 1f)) {
                    result = f[j];
                } else {
                    if (percent > j / (f.length - 1f) && percent < (j + 1f) / (f.length - 1)) {
                        result[i] = f[j][i] - (f[j][i] - f[j + 1][i]) * (percent - j / (f.length - 1f)) * (f.length - 1f);
                    }
                }
            }
        }
        return Color.rgb((int) result[0], (int) result[1], (int) result[2]);
    }
}