package com.aigestudio.wheelpicker.view;

import android.graphics.Color;
import android.graphics.Paint;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author <a href="mailto:nclong87@gmail.com"> Long Nguyen </a>
 */
public class ReactWheelStraightPickerManager extends SimpleViewManager {

    private static final String REACT_CLASS = "WheelStraightPicker";

    private static final int DEFAULT_TEXT_SIZE = 25 * 2;
    private static final int DEFAULT_ITEM_SPACE = 14 * 2;

    @Override
    protected WheelCrossPicker createViewInstance(ThemedReactContext reactContext) {
        ReactWheelStraightPicker picker = new ReactWheelStraightPicker(reactContext);
        picker.setTextColor(Color.LTGRAY);
        picker.setCurrentTextColor(Color.WHITE);
        picker.setTextSize(DEFAULT_TEXT_SIZE);
        picker.setItemSpace(DEFAULT_ITEM_SPACE);
        // picker.setTextAlignment(1);
        return picker;
    }

    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                ItemSelectedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onValueChange")
        );
    }

    @ReactProp(name="data")
    public void setData(ReactWheelStraightPicker picker, ReadableArray items) {
        if (picker != null) {
            ArrayList<Integer> valueData = new ArrayList<>();
            ArrayList<String> labelData = new ArrayList<>();
            for (int i = 0; i < items.size(); i ++) {
                ReadableMap itemMap = items.getMap(i);
                valueData.add(itemMap.getInt("value"));
                labelData.add(itemMap.getString("label"));
            }
            picker.setValueData(valueData);
            picker.setData(labelData);
        }
    }

    @ReactProp(name="selectedIndex")
    public void setSelectedIndex(ReactWheelStraightPicker picker, int index) {
        if (picker != null && picker.getState() == AbstractWheelPicker.SCROLL_STATE_IDLE) {
            picker.setItemIndex(index);
            picker.invalidate();
        }
    }

    @ReactProp(name="textColor", customType = "Color")
    public void setTextColor(ReactWheelStraightPicker picker, Integer color) {
        if (picker != null) {
            picker.setCurrentTextColor(color);
            picker.setTextColor(color);
        }
    }

    @ReactProp(name="textSize")
    public void setTextSize(ReactWheelStraightPicker picker, int size) {
        if (picker != null) {
            picker.setTextSize((int) PixelUtil.toPixelFromDIP(size));
        }
    }

    @ReactProp(name="itemSpace")
    public void setItemSpace(ReactWheelStraightPicker picker, int space) {
        if (picker != null) {
            picker.setItemSpace((int) PixelUtil.toPixelFromDIP(space));
        }
    }

    @ReactProp(name="lineStrokeWidth")
    public void setLineStrokeWidth(ReactWheelStraightPicker picker, int lineStrokeWidth) {
        if (picker != null) {
            picker.setLineStrokeWidth(lineStrokeWidth);
        }
    }

    /* @ReactProp(name="textAlignment")
    public void setTextAlignment(ReactWheelStraightPicker picker, int textAlignment) {
        if (picker != null) {
            picker.setTextAlignment(textAlignment);
        }
    } */

    @Override
    public String getName() {
        return REACT_CLASS;
    }
}
