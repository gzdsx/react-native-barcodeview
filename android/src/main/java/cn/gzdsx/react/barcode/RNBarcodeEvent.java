package cn.gzdsx.react.barcode;

import com.facebook.react.uimanager.events.Event;

public class RNBarcodeEvent extends Event {
    @Override
    public String getEventName() {
        return "RCTBarcodeEvent";
    }
}
