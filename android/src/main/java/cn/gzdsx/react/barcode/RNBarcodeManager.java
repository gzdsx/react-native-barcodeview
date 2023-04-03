package cn.gzdsx.react.barcode;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class RNBarcodeManager extends ViewGroupManager<RNBarcodeView> {
    @NonNull
    @Override
    public String getName() {
        return "RNBarcodeView";
    }

    @NonNull
    @Override
    protected RNBarcodeView createViewInstance(@NonNull ThemedReactContext themedReactContext) {
        return new RNBarcodeView(themedReactContext);
    }

    @ReactProp(name = "isActive")
    public void setIsActive(RNBarcodeView view, boolean isActive) {

    }

    @Override
    public void onAfterUpdateTransaction(@NonNull RNBarcodeView view) {
        Log.d("AfterUpdateTransaction", "");
        super.onAfterUpdateTransaction(view);
    }

    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        super.getExportedCustomBubblingEventTypeConstants();
        MapBuilder.Builder<Object, Object> eventBuilder = MapBuilder.builder();
        eventBuilder.put("onBarcodeRead",
                MapBuilder.of(
                        "phasedRegistrationNames",
                        MapBuilder.of("bubbled", "onBarcodeRead")
                )
        );
        return eventBuilder.build();
    }

    @Override
    public void receiveCommand(@NonNull RNBarcodeView root, String commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
    }
}
