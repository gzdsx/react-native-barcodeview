package cn.gzdsx.react.barcode;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;

import java.util.EnumMap;
import java.util.EnumSet;

public class RNBarcodeAnalyzer implements ImageAnalysis.Analyzer {

    private MultiFormatReader mMultiFormatReader;
    private final RNBarcodeAnalyzeTaskDelegate mDelegate;

    public RNBarcodeAnalyzer(RNBarcodeAnalyzeTaskDelegate delegate) {
        initBarcodeReader();
        mDelegate = delegate;
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        Log.d("barcodeAnalyzer", "开始分析图片");
        RNBarcodeAnalyzeTask task = new RNBarcodeAnalyzeTask(mMultiFormatReader, image, mDelegate);
        task.execute();
    }

    private void initBarcodeReader() {
        mMultiFormatReader = new MultiFormatReader();
        EnumMap<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        EnumSet<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);

        decodeFormats.add(BarcodeFormat.QR_CODE);
        decodeFormats.add(BarcodeFormat.DATA_MATRIX);
        decodeFormats.add(BarcodeFormat.CODE_39);
        decodeFormats.add(BarcodeFormat.CODE_93);
        decodeFormats.add(BarcodeFormat.CODE_128);

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        mMultiFormatReader.setHints(hints);
    }
}
