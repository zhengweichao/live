package top.vchao.live.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.text.DecimalFormat;

import top.vchao.live.R;

/**
 * @ Create_time: 2018/10/18 on 17:34.
 * @ description：
 * @ author: vchao
 */
public class MyCircleBar extends View {
    private static final int INIT_270_ANGLE = 270;//270度
    private static final int INIT_360_ANGLE = 360;//360度
    private static final int START_POINT_270 = 135;//圆环起始点角度
    private static final int START_POINT_360 = 90;//圆环起始点角度
    private static final int DEFAULT_CIRCLE_SIDE = 200;//控件而的默认边长
    private static final int DEFAULT_ANGLE_TYPE = 2;//默认类型-->360
    private static final int CHANGE_ANGLE_TYPE = 1;//默认类型-->270
    private static final int DEFAULT_RING_WIDTH = 10;//默认宽度
    private static final int DEFAULT_RING_UN_REACHED_COLOR = 0xfffcf1d2;//默认颜色
    private static final int DEFAULT_RING_REACHED_COLOR = 0xfff0ba27;//默认进度颜色
    private static final int DEFAULT_PROGRESS = 0;//默认进度是0
    private static final int DEFAULT_MAX_PROGRESS = 100;//默认最大进度值
    private static final boolean DEFAULT_GRADIENT_ON = true;//默认是关闭的
    private static final int DEFAULT_CIRCLE_START_COLOR = 0xfff0ba27;
    private static final int DEFAULT_CIRCLE_CENTER_COLOR = 0xffef8f1e;//黄色
    private static final int DEFAULT_CIRCLE_END_COLOR = 0xfffc8a03;//红色
    private static final int DEFAULT_HINT_TEXT_COLOR = 0xffefb81d;//黑色
    private static final int DEFAULT_SHOW_TEXT_COLOR = 0xffefb81d;//黑色
    private static final int DEFAULT_DRAW_SCALE_COLOR = 0xffffff00;//紫红色
    private static final boolean DEFAULT_DRAW_SCALE_ON = false;//默认不绘制刻

    private int circle_diameter;//圆环的直径
    private int circle_type;//圆环类型
    private int circle_width;//宽度
    private int unReachedColor;//颜色
    private int reachedColor;//颜色
    private int drawArcStartAngle;//绘制圆弧的起始点
    private int drawMaxValues;//绘制的最大值
    private Paint unReachedPaint,reachedPaint;//画笔
    private RectF drawArcRect;//绘制弧形的区域
    private int drawCircleRadius;//实际绘制半径
    private int drawOffset;//绘制的偏移量
    private int circlePointX;//中心X
    private int circlePointY;//中心Y
    private float nowProgress;//当前进度
    private int maxProgress;//最大进度值
    private boolean drawSingleColor;//是否绘制单一的颜色
    private int startColor;//起始颜色
    private int centerColor;//中间颜色
    private int endColor;//结束颜色
    private SweepGradient gradientColors;//渐变颜色
    private PaintFlagsDrawFilter mDrawFilter;//图形抗锯齿
    private BarAnimation circleAnimation;//进度动画
    private float unit;//角度值
    private Paint drawHintText;//绘制提示
    private Paint drawShowText;//绘制文字
    private RectF drawTextRect;//绘制文字的区域
    private int hintTextColor;//提示文字颜色
    private int showTextColor;//显示文字颜色
    private double values;//显示的值
    private int drawScaleHeightStartPoint;//绘制刻度的起点
    private Paint drawScalePaint;//绘制刻度
    private int drawScaleColor;//绘制刻度的颜色
    private boolean isShowScale=false;//是否显示刻度

    public MyCircleBar(Context context) {
        this(context, null);
    }
    public MyCircleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyCircleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttributes(attrs);
        init();//初始化
        initPaint();//初始化画笔
    }

    private void init() {
        drawArcStartAngle = circle_type == DEFAULT_ANGLE_TYPE ? START_POINT_360 : START_POINT_270;
        drawMaxValues = circle_type == DEFAULT_ANGLE_TYPE ? INIT_360_ANGLE : INIT_270_ANGLE;
        drawArcRect = new RectF();//创建这个对象
        drawOffset = (circle_width>>1) + dip2px(2);
        drawScaleHeightStartPoint = circle_width + dip2px(2);
        circleAnimation = new BarAnimation();
        drawTextRect = new RectF();
    }


    private void initPaint() {
        unReachedPaint = new Paint();
        unReachedPaint.setAntiAlias(true);
        unReachedPaint.setDither(true);
        unReachedPaint.setStrokeWidth(circle_width);
        unReachedPaint.setColor(unReachedColor);
        unReachedPaint.setStyle(Paint.Style.STROKE);
        unReachedPaint.setStrokeCap(Paint.Cap.ROUND);


        reachedPaint = new Paint();
        reachedPaint.setAntiAlias(true);
        reachedPaint.setDither(true);
        reachedPaint.setStrokeWidth(circle_width);
        reachedPaint.setStyle(Paint.Style.STROKE);
        reachedPaint.setStrokeCap(Paint.Cap.ROUND);

        drawHintText = new Paint();
        drawHintText.setAntiAlias(true);
        drawHintText.setColor(hintTextColor);
        drawHintText.setTextAlign(Paint.Align.CENTER);
        drawHintText.setStyle(Paint.Style.FILL);

        drawShowText = new Paint();
        drawShowText.setAntiAlias(true);
        drawShowText.setColor(showTextColor);
        drawShowText.setTextAlign(Paint.Align.CENTER);
        drawShowText.setStyle(Paint.Style.FILL);

        drawScalePaint = new Paint();
        drawScalePaint.setAntiAlias(true);
        drawScalePaint.setDither(true);
        drawScalePaint.setColor(drawScaleColor);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArcRect.left=drawOffset;
        drawArcRect.top=drawOffset;
        drawArcRect.right = circle_diameter - drawOffset;
        drawArcRect.bottom = circle_diameter - drawOffset;
        drawCircleRadius = (circle_diameter>>1) - drawOffset;

        int chord_length = (int) Math.sqrt(Math.pow(circle_diameter, 2)
                + Math.pow(circle_diameter, 2));
        drawTextRect.left = (int) ((chord_length / 2 - drawCircleRadius) * Math.sin(45));
        drawTextRect.top = drawTextRect.left;
        drawTextRect.right = circle_diameter - drawTextRect.left;
        drawTextRect.bottom = circle_diameter - drawTextRect.top;
        int placeValues = (int)(drawTextRect.bottom - drawTextRect.top)/5;

        drawHintText.setTextSize(20);
        canvas.drawText("当前体温",circle_diameter/2,(float)(placeValues/2)*2 + drawTextRect.top,drawHintText);
        drawShowText.setTextSize(60);
        drawShowText.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(getStringValues(values/2)+"°C",circle_diameter/2,placeValues*4 + drawTextRect.top,drawShowText);

        circlePointX = circle_diameter>>1;
        circlePointY = circlePointX;


        switch (circle_type){
            case DEFAULT_ANGLE_TYPE:
                canvas.drawCircle(circlePointX,circlePointY,drawCircleRadius,unReachedPaint);
                break;
            case CHANGE_ANGLE_TYPE:
                canvas.drawArc(drawArcRect,drawArcStartAngle,drawMaxValues,false,unReachedPaint);
                break;
        }
        if (drawSingleColor){
            reachedPaint.setShader(gradientColors());
        }else {
            reachedPaint.setColor(reachedColor);
        }
        if (values <= maxProgress){
            canvas.drawArc(drawArcRect,drawArcStartAngle,
                    unit == 0 ?nowProgress/(float) maxProgress*drawMaxValues:unit,
                    false,reachedPaint);
        }else {
            canvas.drawArc(drawArcRect,drawArcStartAngle,drawMaxValues,
                    false,reachedPaint); }

        if (isShowScale){
            int scaleLength = (int) (drawTextRect.top - drawScaleHeightStartPoint)/2;
            float scaleValues = drawMaxValues*1.0f/100;
            canvas.save();
            int drawCounts;
            if (circle_type == DEFAULT_ANGLE_TYPE){
                canvas.rotate(-180,circlePointX,circlePointY);
                drawCounts = 100;
            }else {
                canvas.rotate(-135,circlePointX,circlePointY);
                drawCounts = 101;
            }
            canvas.translate(circlePointX,drawScaleHeightStartPoint);
            for (int i = 0;i < drawCounts;i++){
                if (i % 10 == 0){
                    canvas.drawLine(0,0,0,scaleLength,drawScalePaint);
                }else{
                    canvas.drawLine(0,0,0,scaleLength/2,drawScalePaint);
                }
                canvas.rotate(scaleValues,0,circlePointY - drawScaleHeightStartPoint);
            } canvas.restore();
        } canvas.setDrawFilter(mDrawFilter); }


    private String getStringValues(double values){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(values);
    }
    private SweepGradient gradientColors(){
        if (gradientColors == null){
            switch (circle_type){
                case DEFAULT_ANGLE_TYPE:
                    gradientColors = new SweepGradient(circlePointX,circlePointY,
                            new int[]{startColor,centerColor,endColor,centerColor,startColor},null);
                    break;
                case CHANGE_ANGLE_TYPE:
                    gradientColors = new SweepGradient(circlePointX,circlePointY,
                            new int[]{startColor,centerColor,endColor,startColor},null);
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.setRotate(drawArcStartAngle,circlePointX,circlePointY);
            gradientColors.setLocalMatrix(matrix);
        }
        return gradientColors;
    }

    public void updateProgress(float progress){
        this.nowProgress = progress;
        unit = nowProgress/(float) maxProgress*drawMaxValues;
        values = (double) nowProgress;
        invalidate();
    }

    public void showProgress(float progress,long time){
        this.nowProgress = progress;
        circleAnimation.setDuration(time);
        circleAnimation.setInterpolator(new LinearInterpolator());
        startAnimation(circleAnimation);
    }

    public void setMaxProgress(int maxProgress){
        this.maxProgress = maxProgress; }

    private void obtainAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MyCircleBar);
        circle_type = typedArray.getInt(R.styleable.MyCircleBar_circle_ring_angle_type,CHANGE_ANGLE_TYPE);
        circle_width = dip2px((int) typedArray.getDimension(R.styleable.MyCircleBar_circle_ring_width, DEFAULT_RING_WIDTH));
        unReachedColor = typedArray.getColor(R.styleable.MyCircleBar_circle_ring_un_reached, DEFAULT_RING_UN_REACHED_COLOR);
        reachedColor = typedArray.getColor(R.styleable.MyCircleBar_circle_ring_reached, DEFAULT_RING_REACHED_COLOR);
        nowProgress = typedArray.getInt(R.styleable.MyCircleBar_circle_show_progress,DEFAULT_PROGRESS);
        maxProgress = typedArray.getInt(R.styleable.MyCircleBar_circle_max_progress,DEFAULT_MAX_PROGRESS);
        drawSingleColor = typedArray.getBoolean(R.styleable.MyCircleBar_circle_gradient_on,DEFAULT_GRADIENT_ON);
        startColor = typedArray.getColor(R.styleable.MyCircleBar_circle_start_color,DEFAULT_CIRCLE_START_COLOR);
        centerColor = typedArray.getColor(R.styleable.MyCircleBar_circle_center_color,DEFAULT_CIRCLE_CENTER_COLOR);
        endColor = typedArray.getColor(R.styleable.MyCircleBar_circle_end_color,DEFAULT_CIRCLE_END_COLOR);
        hintTextColor = typedArray.getColor(R.styleable.MyCircleBar_circle_hint_text_color,DEFAULT_HINT_TEXT_COLOR);
        showTextColor = typedArray.getColor(R.styleable.MyCircleBar_circle_show_text_color,DEFAULT_SHOW_TEXT_COLOR);
        drawScaleColor = typedArray.getColor(R.styleable.MyCircleBar_circle_show_scale_color,DEFAULT_DRAW_SCALE_COLOR);
        //  isShowScale = typedArray.getBoolean(R.styleable.MyCircleBar_circle_show_scale_on,DEFAULT_DRAW_SCALE_ON);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        circle_diameter = Math.min(opinionSide(widthMode, widthSize), opinionSide(heightMode, heightSize));
        setMeasuredDimension(circle_diameter, circle_diameter);
    }

    private int opinionSide(int mode, int size) {
        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int defaultSize = DEFAULT_CIRCLE_SIDE;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, defaultSize); }
        } return result;
    }

    private int dip2px(int dipValues) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValues, getResources().getDisplayMetrics());
    }
    public class BarAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            unit = nowProgress/(float) maxProgress*drawMaxValues*interpolatedTime;
            values = nowProgress * interpolatedTime;
            postInvalidate();
        }
    }


}
