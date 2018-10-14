package com.aigestudio.wheelpicker.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Region;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.util.Log;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.view.R;

import java.util.List;

/**
 * @author <a href="mailto:nclong87@gmail.com"> Long Nguyen </a>
 */
public class ReactWheelStraightPicker extends WheelStraightPicker {

    private final EventDispatcher mEventDispatcher;
    private final MediaPlayer mp;
    private List<Integer> mValueData;
    private float lineStrokeWidth = 10;

    public ReactWheelStraightPicker(ReactContext reactContext) {
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
        // mp.start();
        mEventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
        // mp = MediaPlayer.create(reactContext.getCurrentActivity(), R.raw.wheel);
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
    protected void drawItems(Canvas canvas) {
        for (int i = -itemIndex; i < data.size() - itemIndex; i++) {
            int curDis = mOrientation.computeStraightUnit(unit, i, disTotalMoveX, disTotalMoveY,
                    diSingleMoveX, diSingleMoveY);
            if (curDis > unitDisplayMax || curDis < unitDisplayMin) {
                continue;
            }
            int alpha = 255 - 400 * Math.abs(curDis) / unitDisplayMax;
            if (alpha < 0) {
                continue;
            }

            float lineY = wheelCenterTextY + curDis - (mTextBound.height() / 2);
            float lineStopX = (i + itemIndex) % 2 == 0 ? 50 : 100;
            canvas.save();
            canvas.clipRect(rectCurItem, Region.Op.DIFFERENCE);
            mPaint.setColor(textColor);
            mPaint.setAlpha(alpha);
            mPaint.setStrokeWidth(lineStrokeWidth);
            canvas.drawLine(0, lineY, lineStopX, lineY, mPaint);
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            mTextPaint.setColor(textColor);
            // Log.d("itemIndex", ";i=" + i + ";val=" + data.get(i + itemIndex) + ";alpha=" + (255 - 400 * Math.abs(curDis) / unitDisplayMax));
            mTextPaint.setAlpha(alpha);
            // mOrientation.draw(canvas, mTextPaint, "-" + data.get(i + itemIndex), curDis, wheelCenterX, wheelCenterTextY);
            canvas.drawText(data.get(i + itemIndex), rectCurItem.right - 10, wheelCenterTextY + curDis, mTextPaint);

            canvas.restore();

            canvas.save();
            canvas.clipRect(rectCurItem);
            mPaint.setColor(curTextColor);
            mPaint.setStrokeWidth(lineStrokeWidth);
            canvas.drawLine(0, lineY, lineStopX, lineY, mPaint);
            mTextPaint.setColor(curTextColor);
            // mOrientation.draw(canvas, mTextPaint, "-" + data.get(i + itemIndex), curDis, wheelCenterX, wheelCenterTextY);
            canvas.drawText(data.get(i + itemIndex), rectCurItem.right - 10, wheelCenterTextY + curDis, mTextPaint);
            // canvas.drawLine(0, lineY, 50, lineY, mTextPaint);
            canvas.restore();

        }
    }

    @Override
    protected void drawForeground(Canvas canvas) {
        super.drawForeground(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        int colorFrom = 0x00FFFFFF;//Color.BLACK;
        int colorTo = Color.WHITE;
        LinearGradient linearGradientShader = new LinearGradient(rectCurItem.left, rectCurItem.top, rectCurItem.right/2, rectCurItem.top, colorFrom, colorTo, Shader.TileMode.MIRROR);
        paint.setShader(linearGradientShader);
        canvas.drawLine(rectCurItem.left, rectCurItem.top, rectCurItem.right, rectCurItem.top, paint);
        canvas.drawLine(rectCurItem.left, rectCurItem.bottom, rectCurItem.right, rectCurItem.bottom, paint);
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

    /* @Override
    public void setTextAlignment(int textAlignment) {
        // super.setTextAlignment(textAlignment);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
    } */

    public void setValueData(List<Integer> data) {
        mValueData = data;
    }

    public int getState() {
        return state;
    }
}


