package com.aigestudio.wheelpicker.view;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Region;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.view.R;

import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:nclong87@gmail.com"> Long Nguyen </a>
 */
public class ReactWheelCurvedPicker extends WheelCrossPicker {

    private final EventDispatcher mEventDispatcher;
    private final MediaPlayer mp;
    private List<Integer> mValueData;
    private float lineStrokeWidth = 10;
    private final HashMap<Integer, Integer> SPACE = new HashMap<>();
    private final HashMap<Integer, Integer> DEPTH = new HashMap<>();

    private final Camera camera = new Camera();
    private final Matrix matrixRotate = new Matrix(), matrixDepth = new Matrix();

    private int radius;
    private int degreeSingleDelta;
    private int degreeIndex, degreeUnitDelta;

    public ReactWheelCurvedPicker(ReactContext reactContext) {
        super(reactContext);
        reactContext.addLifecycleEventListener(new LifecycleEventListener() {
            @Override
            public void onHostResume() {
            }

            @Override
            public void onHostPause() {
                if (mp != null && mp.isPlaying()) {
                    mp.pause();
                }
            }

            @Override
            public void onHostDestroy() {
                if (mp != null) {
                    Log.d("onHostDestroy", "mp.release()");
                    mp.release();
                }
            }
        });
        mp = MediaPlayer.create(reactContext, R.raw.wheel);
        mp.setLooping(true);
        mEventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
        setOnWheelChangeListener(new OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {
            }

            @Override
            public void onWheelSelected(int index, String data) {
                if (mValueData != null && index < mValueData.size()) {
                    mEventDispatcher.dispatchEvent(
                            new ItemSelectedEvent(getId(), mValueData.get(index)));
                }
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (state == 1 && !mp.isPlaying()) {
                    mp.start();
                }
                if (state == 0 && mp.isPlaying()) {
                    mp.pause();
                }
            }
        });
    }

    @Override
    protected void computeWheelSizes() {
        super.computeWheelSizes();

        radius = mOrientation.computeRadius(itemCount, itemSpace, maxTextWidth, maxTextHeight);

        unit = (int) (180 * 1.0F / (itemCount + 1));

        wheelContentWidth = mOrientation.getCurvedWidth(radius, maxTextWidth, maxTextHeight);
        wheelContentHeight = mOrientation.getCurvedHeight(radius, maxTextWidth, maxTextHeight);

        unitDisplayMin = -90;
        unitDisplayMax = 90;

        unitDeltaMin = -unit * (data.size() - itemIndex - 1);
        unitDeltaMax = unit * itemIndex;
    }

    @Override
    protected void drawItems(Canvas canvas) {
        for (int i = -itemIndex; i < data.size() - itemIndex; i++) {
            int curUnit = unit * i;
            curUnit += (unitDeltaTotal + degreeSingleDelta);
            if (curUnit > unitDisplayMax || curUnit < unitDisplayMin) continue;
            int alpha = 255 - 255 * Math.abs(curUnit) / unitDisplayMax;
            if (alpha < 0) {
                // continue;
                alpha = 0;
            }

            int space = computeSpace(curUnit);
            if (space == 0) curUnit = 1;
            int depth = computeDepth(curUnit);

            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            float lineY = wheelCenterTextY + space - (mTextBound.height() / 2);
            float lineStopX = (i + itemIndex) % 2 == 0 ? 50 : 100;

            camera.save();
            mOrientation.rotateCamera(camera, curUnit);
            camera.getMatrix(matrixRotate);
            camera.restore();
            mOrientation.matrixToCenter(matrixRotate, space, wheelCenterX, wheelCenterY);
            camera.save();
            camera.translate(0, 0, depth);
            camera.getMatrix(matrixDepth);
            camera.restore();
            mOrientation.matrixToCenter(matrixDepth, space, wheelCenterX, wheelCenterY);
            matrixRotate.postConcat(matrixDepth);

            canvas.save();
            canvas.concat(matrixRotate);
            canvas.clipRect(rectCurItem, Region.Op.DIFFERENCE);
            mPaint.setColor(textColor);
            mPaint.setAlpha(alpha);
            mPaint.setStrokeWidth(lineStrokeWidth);
            canvas.drawLine(0, lineY, lineStopX, lineY, mPaint);
            mTextPaint.setColor(textColor);
            mTextPaint.setAlpha(alpha);
            // mOrientation.draw(canvas, mTextPaint, data.get(i + itemIndex), space, wheelCenterX, wheelCenterTextY);
            canvas.drawText(data.get(i + itemIndex), rectCurItem.right - 10, wheelCenterTextY + space, mTextPaint);
            canvas.restore();

            canvas.save();
            canvas.clipRect(rectCurItem);
            mPaint.setColor(curTextColor);
            mPaint.setStrokeWidth(lineStrokeWidth);
            canvas.drawLine(0, lineY, lineStopX, lineY, mPaint);
            mTextPaint.setColor(curTextColor);
            // mOrientation.draw(canvas, mTextPaint, data.get(i + itemIndex), space, wheelCenterX, wheelCenterTextY);
            canvas.drawText(data.get(i + itemIndex), rectCurItem.right - 10, wheelCenterTextY + space, mTextPaint);
            canvas.restore();
        }
    }

    private int computeSpace(int degree) {
        int space;
        if (SPACE.containsKey(degree)) {
            space = SPACE.get(degree);
        } else {
            space = (int) (Math.sin(Math.toRadians(degree)) * radius);
            SPACE.put(degree, space);
        }
        return space;
    }

    private int computeDepth(int degree) {
        int depth;
        if (DEPTH.containsKey(degree)) {
            depth = DEPTH.get(degree);
        } else {
            depth = (int) (radius - Math.cos(Math.toRadians(degree)) * radius);
            DEPTH.put(degree, depth);
        }
        return depth;
    }

    @Override
    protected void onTouchMove(MotionEvent event) {
        degreeUnitDelta = mOrientation.computeDegreeSingleDelta(diSingleMoveX, diSingleMoveY, radius);
        int curDis = mOrientation.obtainCurrentDis(diSingleMoveX, diSingleMoveY);
        if (Math.abs(curDis) >= radius) {
            if (curDis >= 0)
                degreeIndex++;
            else
                degreeIndex--;
            diSingleMoveX = 0;
            diSingleMoveY = 0;
            degreeUnitDelta = 0;
        }
        degreeSingleDelta = (degreeIndex * 80) + degreeUnitDelta;
        super.onTouchMove(event);
    }

    @Override
    protected void onTouchUp(MotionEvent event) {
        unitDeltaTotal += degreeSingleDelta;
        degreeSingleDelta = 0;
        degreeUnitDelta = 0;
        degreeIndex = 0;
        super.onTouchUp(event);
    }

    @Override
    public void clearCache() {
        SPACE.clear();
        DEPTH.clear();
        mOrientation.clearCache();
    }

    @Override
    protected void drawForeground(Canvas canvas) {
        super.drawForeground(canvas);

        /* Paint paint = new Paint();
        paint.setColor(textColor);
        int colorFrom = 0x00FFFFFF;//Color.BLACK;
        int colorTo = textColor;
        LinearGradient linearGradientShader = new LinearGradient(rectCurItem.left, rectCurItem.top, rectCurItem.right/2, rectCurItem.top, colorFrom, colorTo, Shader.TileMode.MIRROR);
        paint.setShader(linearGradientShader);
        canvas.drawLine(rectCurItem.left, rectCurItem.top, rectCurItem.right, rectCurItem.top, paint);
        canvas.drawLine(rectCurItem.left, rectCurItem.bottom, rectCurItem.right, rectCurItem.bottom, paint); */
    }

    @Override
    public void setItemIndex(int index) {
        super.setItemIndex(index);
        unitDeltaTotal = 0;
		mHandler.post(this);
    }

    public void setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = lineStrokeWidth;
    }

    public void setValueData(List<Integer> data) {
        mValueData = data;
    }

    public int getState() {
        return state;
    }
}

