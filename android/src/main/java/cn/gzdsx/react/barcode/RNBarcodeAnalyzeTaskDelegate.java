package cn.gzdsx.react.barcode;

import com.google.zxing.Result;

public interface RNBarcodeAnalyzeTaskDelegate {
    void onBarcodeRead(Result result);
    void onAnalyzeTaskCompleted();
}
