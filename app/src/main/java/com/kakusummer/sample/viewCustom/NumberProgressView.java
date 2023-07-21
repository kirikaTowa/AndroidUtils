package com.kakusummer.sample.viewCustom;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
/**
 * Created by Adminis on 2017/5/1.
 */
public class NumberProgressView extends View{
    private Paint paint;//绘制进度条画笔
    private Paint textPaint;//绘制文字画笔
    private Paint dottePaint;//绘制灰色线画笔
    private int width;
    private int height;
    private int padding =5;
    private int value = 0;
    private boolean isStartDownload = false;
    public NumberProgressView(Context context) {
        this(context,null);
    }
    public NumberProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public NumberProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }
    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(3);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(18);

        dottePaint = new Paint();
        dottePaint.setAntiAlias(true);
        dottePaint.setStrokeWidth(2);
        dottePaint.setStyle(Paint.Style.FILL);
        dottePaint.setColor(Color.parseColor("#e5e5e5"));
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String str = value+"%";
        float strWidth = textPaint.measureText(value+"%")+padding;//绘制文字的宽度 +padding是为了防止在进度条加载完毕的时候文字绘制出现被切掉情况
        Rect rect = new Rect();
        textPaint.getTextBounds(str,0,str.length(),rect);
        canvas.drawLine(0,height/2,value*((width-strWidth)/100),height/2,paint);//绘制进度
        canvas.drawText(value+"%",value*((width-strWidth)/100)+padding,(height-rect.height())/2+2*padding,textPaint);//绘制进度文字 这个高度+2*padding是因为drawText是根据基线计算的,要准确的话要去求基线
        canvas.drawLine(value*((width-strWidth)/100)+strWidth+padding,height/2,width,height/2,dottePaint);//绘制灰色进度表示剩余多少
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(value<100&&isStartDownload){
                    value++;
                    postInvalidate();
                }
            }
        },100);
    }
    public void startDownLoad(){
        isStartDownload = !isStartDownload;
        invalidate();
    }
}



