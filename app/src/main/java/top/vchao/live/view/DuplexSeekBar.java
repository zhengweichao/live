package top.vchao.live.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.math.BigDecimal;

import top.vchao.live.R;

/**
 * Created by fpt on 2018/2/6.
 */

public class DuplexSeekBar extends View {
    private Drawable hasScrollBarBg;        //滑动条滑动后背景图
    private Drawable notScrollBarBg;        //滑动条未滑动背景图
    private Drawable mThumbLow;         //前滑块
    private Drawable mThumbHigh;        //后滑块
    private static final int[] STATE_NORMAL = {};
    private static final int[] STATE_PRESSED = {android.R.attr.state_pressed, android.R.attr.state_window_focused};
    private int mScrollBarHeight;    //滑动条高度
    private int mThumbWidth;        //滑动块直径
    private int mScrollBarWidth;     //控件实际宽度
    private int mDistance;      //总刻度是固定距离 两边各去掉半个滑块距离
    private double mOffsetLow = 0;     //前滑块中心坐标
    private double mOffsetHigh = 0;    //后滑块中心坐标

    private double defaultScreenLow = 36.5;    //默认前滑块位置百分比
    private double defaultScreenHigh = 37.2;  //默认后滑块位置百分比

    private static int mThumbMarginTop;   //滑动块顶部离view顶部的距离
    private static int mThumbMarginBottom;   //滑动块顶部离view顶部的距离
    private static int mTextViewMarginTop;   //当前滑块文字距离view顶部距离
    private static int unitLength;

    private OnSeekBarChangeListener mBarChangeListener;

    private static final int CLICK_ON_LOW = 1;        //手指在前滑块上滑动
    private static final int CLICK_ON_HIGH = 2;       //手指在后滑块上滑动
    private static final int CLICK_IN_LOW_AREA = 3;   //手指点击离前滑块近
    private static final int CLICK_IN_HIGH_AREA = 4;  //手指点击离后滑块近
    private static final int CLICK_OUT_AREA = 5;      //手指点击在view外
    private static final int CLICK_INVALID = 0;
    private int mFlag = CLICK_INVALID;   //手指按下的类型

    private Paint text_Paint;
    private double maxProgress = 40;
    private double minProgress = 35;
    private static boolean flag = true;

    public DuplexSeekBar(Context context) {
        super(context);
        initView(context);
    }

    public DuplexSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DuplexSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        unitLength = dp2px(context, 4);
        mThumbMarginTop = 6 * unitLength;
        mThumbMarginBottom = 6 * unitLength;
        mTextViewMarginTop = 4 * unitLength;
        notScrollBarBg = context.getResources().getDrawable(R.drawable.jdtbag);
        hasScrollBarBg = context.getResources().getDrawable(R.drawable.jdtbagzhongjian);
        mThumbLow = context.getResources().getDrawable(R.drawable.circle_blue);
        mThumbHigh = context.getResources().getDrawable(R.drawable.circle_blue);

        mThumbLow.setState(STATE_NORMAL);
        mThumbHigh.setState(STATE_NORMAL);

        //获取滑动条高度
        mScrollBarHeight = notScrollBarBg.getIntrinsicHeight();
        //获取滑动块直径
        mThumbWidth = mThumbLow.getIntrinsicWidth();

        //文字笔
        text_Paint = new Paint();
        text_Paint.setTextAlign(Paint.Align.CENTER);
        text_Paint.setAntiAlias(true);
        text_Paint.setColor(Color.parseColor("#1F1F1F"));
        text_Paint.setTextSize(26);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取view的总宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mDistance = width - mThumbWidth - 4 * unitLength;
        mScrollBarWidth = mDistance + mThumbWidth;

        mOffsetLow = formatDouble((defaultScreenLow - minProgress) / (maxProgress - minProgress) * (mDistance)) + mThumbWidth / 2;
        mOffsetHigh = formatDouble((defaultScreenHigh - minProgress) / (maxProgress - minProgress) * (mDistance)) + mThumbWidth / 2;

        setMeasuredDimension(width, mThumbWidth + mThumbMarginTop + mThumbMarginBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int top = mThumbMarginTop + mThumbWidth / 2 - mScrollBarHeight / 2;
        int bottom = top + mScrollBarHeight;

        //白色滑动条，两个滑块各两边部分
        notScrollBarBg.setBounds(mThumbWidth / 2 + 2 * unitLength, top, mDistance + mThumbWidth / 2 + 2 * unitLength, bottom);
        notScrollBarBg.draw(canvas);

        //红色滑动条，两个滑块中间部分
        hasScrollBarBg.setBounds((int) mOffsetLow + mThumbWidth / 2 + 2 * unitLength, top, (int) mOffsetHigh - mThumbWidth / 2 + 2 * unitLength, bottom);
        hasScrollBarBg.draw(canvas);

        //前滑块
        mThumbLow.setBounds((int) (mOffsetLow - mThumbWidth / 2) + 2 * unitLength, mThumbMarginTop, (int) (mOffsetLow + mThumbWidth / 2) + 2 * unitLength, mThumbWidth + mThumbMarginTop);
        mThumbLow.draw(canvas);
        //后滑块
        mThumbHigh.setBounds((int) (mOffsetHigh - mThumbWidth / 2) + 2 * unitLength, mThumbMarginTop, (int) (mOffsetHigh + mThumbWidth / 2) + 2 * unitLength, mThumbWidth + mThumbMarginTop);
        mThumbHigh.draw(canvas);

        //当前滑块刻度
        double progressLow = formatDouble((mOffsetLow - mThumbWidth / 2) / mDistance * (maxProgress - minProgress) + minProgress);
        double progressHigh = formatDouble((mOffsetHigh - mThumbWidth / 2) / mDistance * (maxProgress - minProgress) + minProgress);
        canvas.drawText(progressLow + "", (int) mOffsetLow + 20, mTextViewMarginTop, text_Paint);
        canvas.drawText(progressHigh + "", (int) mOffsetHigh + 20, mTextViewMarginTop, text_Paint);
        if (mBarChangeListener != null) {
            mBarChangeListener.onProgressChanged(this, progressLow, progressHigh);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (flag) {
            //按下
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                mFlag = getAreaFlag(e);
                if (mFlag == CLICK_ON_LOW) {
                    mThumbLow.setState(STATE_PRESSED);
                } else if (mFlag == CLICK_ON_HIGH) {
                    mThumbHigh.setState(STATE_PRESSED);
                } else if (mFlag == CLICK_IN_LOW_AREA) {
                    mThumbLow.setState(STATE_PRESSED);
                    mThumbHigh.setState(STATE_NORMAL);
                    //如果点击0-mThumbWidth/2坐标
                    if (e.getX() < 0 || e.getX() <= mThumbWidth / 2) {
                        mOffsetLow = mThumbWidth / 2;
                    } else if (e.getX() > mScrollBarWidth - mThumbWidth / 2) {
                        mOffsetLow = mThumbWidth / 2 + mDistance;
                    } else {
                        mOffsetLow = formatDouble(e.getX());

                    }
                } else if (mFlag == CLICK_IN_HIGH_AREA) {
                    mThumbHigh.setState(STATE_PRESSED);
                    mThumbLow.setState(STATE_NORMAL);
                    if (e.getX() >= mScrollBarWidth - mThumbWidth / 2) {
                        mOffsetHigh = mDistance + mThumbWidth / 2;
                    } else {
                        mOffsetHigh = formatDouble(e.getX());
                    }
                }
                //更新滑块
                invalidate();
                //移动move
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                if (mFlag == CLICK_ON_LOW) {
                    if (e.getX() < 0 || e.getX() <= mThumbWidth / 2) {
                        mOffsetLow = mThumbWidth / 2;
                    } else if (e.getX() >= mScrollBarWidth - mThumbWidth / 2) {
                        mOffsetLow = mThumbWidth / 2 + mDistance;
                        mOffsetHigh = mOffsetLow;
                    } else {
                        mOffsetLow = formatDouble(e.getX());
                        if (mOffsetHigh - mOffsetLow <= 0) {
                            mOffsetHigh = (mOffsetLow <= mDistance + mThumbWidth / 2) ? (mOffsetLow) : (mDistance + mThumbWidth / 2);
                        }
                    }
                } else if (mFlag == CLICK_ON_HIGH) {
                    if (e.getX() < mThumbWidth / 2) {
                        mOffsetHigh = mThumbWidth / 2;
                        mOffsetLow = mThumbWidth / 2;
                    } else if (e.getX() > mScrollBarWidth - mThumbWidth / 2) {
                        mOffsetHigh = mThumbWidth / 2 + mDistance;
                    } else {
                        mOffsetHigh = formatDouble(e.getX());
                        if (mOffsetHigh - mOffsetLow <= 0) {
                            mOffsetLow = (mOffsetHigh >= mThumbWidth / 2) ? (mOffsetHigh) : mThumbWidth / 2;
                        }
                    }
                }
                //更新滑块
                invalidate();
                //抬起
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                mThumbLow.setState(STATE_NORMAL);
                mThumbHigh.setState(STATE_NORMAL);
            }
        }
        return true;
    }


    //获取当前手指位置
    public int getAreaFlag(MotionEvent e) {
        int top = mThumbMarginTop;
        int bottom = mThumbWidth + mThumbMarginTop;
        if (e.getY() >= top && e.getY() <= bottom && e.getX() >= (mOffsetLow - mThumbWidth / 2) && e.getX() <= mOffsetLow + mThumbWidth / 2) {
            return CLICK_ON_LOW;
        } else if (e.getY() >= top && e.getY() <= bottom && e.getX() >= (mOffsetHigh - mThumbWidth / 2) && e.getX() <= (mOffsetHigh + mThumbWidth / 2)) {
            return CLICK_ON_HIGH;
        } else if (e.getY() >= top
                && e.getY() <= bottom
                && ((e.getX() >= 0 && e.getX() < (mOffsetLow - mThumbWidth / 2)) || ((e.getX() > (mOffsetLow + mThumbWidth / 2))
                && e.getX() <= ((double) mOffsetHigh + mOffsetLow) / 2))) {
            return CLICK_IN_LOW_AREA;
        } else if (e.getY() >= top && e.getY() <= bottom && (((e.getX() > ((double) mOffsetHigh + mOffsetLow) / 2) && e.getX() < (mOffsetHigh - mThumbWidth / 2)) || (e
                .getX() > (mOffsetHigh + mThumbWidth / 2) && e.getX() <= mScrollBarWidth))) {
            return CLICK_IN_HIGH_AREA;
        } else if (!(e.getX() >= 0 && e.getX() <= mScrollBarWidth && e.getY() >= top && e.getY() <= bottom)) {
            return CLICK_OUT_AREA;
        } else {
            return CLICK_INVALID;
        }
    }

    //设置前滑块的值
    public void setProgressLow(double progressLow) {
        this.defaultScreenLow = progressLow;
        mOffsetLow = formatDouble((defaultScreenLow - minProgress) / (maxProgress - minProgress) * (mDistance)) + mThumbWidth / 2;

        invalidate();
    }

    public void setThisEnable(boolean b) {
        flag = b;
    }

    //设置后滑块的值
    public void setProgressHigh(double progressHigh) {
        this.defaultScreenHigh = progressHigh;
        mOffsetHigh = formatDouble((defaultScreenHigh - minProgress) / (maxProgress - minProgress) * (mDistance)) + mThumbWidth / 2;

        invalidate();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
        this.mBarChangeListener = mListener;
    }

    //回调函数，在滑动时实时调用，改变输入框的值
    public interface OnSeekBarChangeListener {
        //滑动时
        void onProgressChanged(DuplexSeekBar seekBar, double progressLow, double progressHigh);
    }


    public static int dp2px(Context context, int value) {
        float v = context.getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    //设置滑动结果到小数点1位
    private static double formatDouble(double pDouble) {
        BigDecimal bd = new BigDecimal(pDouble);
        BigDecimal bd1 = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        pDouble = bd1.doubleValue();
        return pDouble;
    }

}
