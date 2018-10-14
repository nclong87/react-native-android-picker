package com.nclong87;

import com.aigestudio.wheelpicker.view.ReactWheelCurvedPickerManager;
import com.aigestudio.wheelpicker.view.ReactWheelStraightPickerManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:nclong87@gmail.com"> Long Nguyen </a>
 */
public class ReactNativeWheelPickerPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new ReactWheelStraightPickerManager(),
                new ReactWheelCurvedPickerManager()
        );
    }
}
