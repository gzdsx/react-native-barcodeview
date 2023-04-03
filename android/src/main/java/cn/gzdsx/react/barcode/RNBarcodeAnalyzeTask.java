package cn.gzdsx.react.barcode;

import android.graphics.ImageFormat;
import android.os.AsyncTask;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.nio.ByteBuffer;

public class RNBarcodeAnalyzeTask extends AsyncTask<Void, Void, Result> {
    MultiFormatReader mMultiFormatReader;
    ImageProxy image;
    RNBarcodeAnalyzeTaskDelegate mDelegate;

    public RNBarcodeAnalyzeTask(MultiFormatReader multiFormatReader, ImageProxy imageProxy, RNBarcodeAnalyzeTaskDelegate delegate) {
        super();
        image = imageProxy;
        mDelegate = delegate;
        mMultiFormatReader = multiFormatReader;
    }

    @Override
    protected Result doInBackground(Void... params) {
        if (mDelegate == null) {
            return null;
        }
        //如果不是yuv_420_888格式直接不处理
        if (ImageFormat.YUV_420_888 != image.getFormat()) {
            Log.e("BarcodeAnalyzer", "expect YUV_420_888, now = ${image.format}");
            image.close();
            return null;
        }

        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);
        image.close();

        byte[] rotatedData = rotateImage(data, width, height);
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                rotatedData, // byte[] yuvData
                height, // int dataWidth
                width, // int dataHeight
                0, // int left
                0, // int top
                height, // int width
                width, // int height
                false // boolean reverseHorizontal
        );
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            return mMultiFormatReader.decode(bitmap);
        } catch (Throwable t) {
            String message = t.getMessage();
            if (message != null) {
                Log.d("AnalyzeBarCodeFailed", message);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (result != null) {
            mDelegate.onBarcodeRead(result);
        }
        mDelegate.onAnalyzeTaskCompleted();
    }

    private byte[] rotateImage(byte[] imageData, int width, int height) {
        byte[] rotated = new byte[imageData.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotated[x * height + height - y - 1] = imageData[x + y * width];
            }
        }
        return rotated;
    }
}
