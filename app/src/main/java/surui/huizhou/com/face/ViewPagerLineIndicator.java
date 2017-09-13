package surui.huizhou.com.face;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ViewPagerLineIndicator extends LinearLayout {
    private Path mPath;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 默认的Tab数量
     */
    private static final int COUNT_DEFAULT_TAB = 4;
    /**
     * tab数量
     */
    private int mTabVisibleCount = COUNT_DEFAULT_TAB;
    /**
     * 线条默认颜色
     */
    private static final int DEFAULT_COLOR = 0xFFFFFFFF;
    /**
     * 指示器线条的颜色
     */
    private int mLineColor = DEFAULT_COLOR;
    /**
     * tab上的内容
     */
    private List<String> mTabTitles;
    /**
     * 与指示器绑定的 viewpager
     */
    private ViewPager mViewPager;
    /**
     * 指示器(矩形)
     */
    private Rect mRect;
    /**
     * 指示器的宽度
     */
    private int mLineWidth;
    private int mTriangleHeight;
    private int mTriangleWidths;
    private int mInitTranslationX;
    /**
     * 标题正常时的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    /**
     * 标题选中时的颜色
     */
    private static final int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFFFFFFFF;
    /**
     * 手指滑动时的偏移量
     */
    private float mTranslationX;

    /**
     * 占用了ViewPager 监听事件   提供接口给ViewPage 调用监听
     *
     * @author Administrator
     */
    public interface PageOnchangeListener {
        public void onPageSelected(int arg0);

        public void onPageScrolled(int arg0, float arg1, int arg2);

        public void onPageScrollStateChanged(int arg0);
    }

    public PageOnchangeListener mListener;

    public void setOnpageChangeListener(PageOnchangeListener listener) {
        this.mListener = listener;
    }


    public ViewPagerLineIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerLineIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获得自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerLineIndicator);
        mLineColor = a.getInt(R.styleable.ViewPagerLineIndicator_item_color, DEFAULT_COLOR);
        mTabVisibleCount = a.getInt(R.styleable.ViewPagerLineIndicator_item_count, COUNT_DEFAULT_TAB);

        if (mTabVisibleCount <= 0)
            mTabVisibleCount = COUNT_DEFAULT_TAB;

        a.recycle();

        // 初始化画笔，用来画矩形指示器
        mPaint = new Paint();
        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.FILL);
    }

    /**
     * 设置tab的标题内容 可选，可以自己在布局文件中写死
     *
     * @param datas
     */
    public void setTabItemTitles(List<String> datas) {
        // 如果传入的list有值，则移除布局文件中设置的 view
        if (datas != null && datas.size() > 0) {
            this.removeAllViews();
            this.mTabTitles = datas;

            for (String title : mTabTitles) {
                // 添加 view
                addView(generateTextView(title));
            }
            // 设置item的click事件
            setItemClickEvent();
        }
    }

    /**
     * 根据标题生成我们的TextView
     *
     * @param text
     * @return
     */
    private TextView generateTextView(String text) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / mTabVisibleCount;
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setLayoutParams(lp);
        return tv;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 设置每一个 tab 的宽、高度，以及点击事件
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.weight = 0;
            params.width = mLineWidth;
            view.setLayoutParams(params);
        }

        // 设置点击事件
        setItemClickEvent();
    }

    private void setItemClickEvent() {
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            final int position = i;
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(position);
                }
            });
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 当大小改变时调用
        super.onSizeChanged(w, h, oldw, oldh);
        mLineWidth = getWidth() / mTabVisibleCount;
        mTriangleWidths = mLineWidth / 4;
        mInitTranslationX = mLineWidth / 2 - mTriangleWidths / 2;
//		 initLine();//矩形指示器
        initTriangle();//三角形指示器
    }

    /**
     * 初始化矩形指示器
     */
    private void initLine() {
        mRect = new Rect(0, 0, mLineWidth, 10);
    }

    /**
     * 初始化三角形
     */
    private void initTriangle() {
        mTriangleHeight = mTriangleWidths / 2;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidths, 0);
        mPath.lineTo(mTriangleWidths / 2, -mTriangleHeight);
        mPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        // 偏移到 tab 的底部，因为指示器是在底部的
        // 画出矩形指示器
//		 canvas.translate(mTranslationX, getHeight() - 10);
//		 canvas.drawRect(mRect, mPaint);
        //画出三角形指示器
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 35);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

    }

    /**
     * 绑定 viewpager
     */
    public void setViewPager(ViewPager viewPager, int position) {
        mViewPager = viewPager;
        highLightTextView(position);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (mListener != null) {
                    mListener.onPageSelected(position);
                }
                System.out.println("onPageSelected:" + position);
                resetTextViewColor();// <span style="white-space:pre"> </span>//
                // 重置默认颜色
                highLightTextView(position);// <span style="white-space:pre">
                // </span>// 设置当前选中的item为高亮颜色
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
                System.out.println("onPageScrolled:" + position);
                // Pager 滚动时， tab 跟随滚动
                scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
                System.out.println("onPageScrollStateChanged:" + state);
            }
        });
    }

    private void scroll(int position, float offset) {
        // 计算偏移量
        mTranslationX = getWidth() / mTabVisibleCount * (position + offset);
        // 容器移动
        if (position >= (mTabVisibleCount - 2) && offset > 0 && getChildCount() > mTabVisibleCount) {

            if (mTabVisibleCount != 1) {
                this.scrollTo((position - (mTabVisibleCount - 2)) * mLineWidth + (int) (mLineWidth * offset), 0);
            } else {
                this.scrollTo(position * mLineWidth + (int) (mLineWidth * offset), 0);
            }
        }
        // 重画，刷新界面
        invalidate();
    }

    /**
     * 设置字体颜色
     */
    private void resetTextViewColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 设置高亮文本
     *
     * @param position
     */
    private void highLightTextView(int position) {
        View view = getChildAt(position);

        if (view instanceof TextView) {
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
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
}
