package com.example.frankjunior.pinceldesom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.puredata.core.PdBase;

public class DrawingView extends View {

    private static final float TOUCH_TOLERANCE = 4;
    private static final int AMARELO = 0xFFFFC048;
    private static final int BRANCO = 0xFFFFFFFF;
    private static final int VERMELHO = 0xFFE82126;
    private static final int VERDE = 0xFF00B87F;
    private static final int AZUL = 0xFF3666DE;
    private static final String TRIGGER_X_AMARELO = "x_amarelo";
    private static final String TRIGGER_Y_AMARELO = "y_amarelo";
    private static final String TRIGGER_X_VERMELHO = "x_vermelho";
    private static final String TRIGGER_Y_VERMELHO = "y_vermelho";
    private static final String TRIGGER_X_VERDE = "x_verde";
    private static final String TRIGGER_Y_VERDE = "y_verde";
    private static final String TRIGGER_X_AZUL = "x_azul";
    private static final String TRIGGER_Y_AZUL = "y_azul";
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private Paint mBitmapPaint;
    private float mX, mY;
    private int color = AMARELO;

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(AMARELO);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BRANCO);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        canvas.drawPath(mPath, mPaint);
    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            switch (this.color) {
                case AMARELO:
                    PdBase.sendFloat(TRIGGER_X_AMARELO, mX);
                    PdBase.sendFloat(TRIGGER_Y_AMARELO, mY);
                    break;
                case VERMELHO:
                    PdBase.sendFloat(TRIGGER_X_VERMELHO, mX);
                    PdBase.sendFloat(TRIGGER_Y_VERMELHO, mY);
                    break;
                case VERDE:
                    PdBase.sendFloat(TRIGGER_X_VERDE, mX);
                    PdBase.sendFloat(TRIGGER_Y_VERDE, mY);
                    break;
                case AZUL:
                    PdBase.sendFloat(TRIGGER_X_AZUL, mX);
                    PdBase.sendFloat(TRIGGER_Y_AZUL, mY);
                    break;

                default:
                    break;
            }

        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public void colorChanged(int color) {
        this.color = color;
        mPaint.setColor(color);
        invalidate();
    }
}
