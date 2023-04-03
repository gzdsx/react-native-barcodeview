package cn.gzdsx.react.barcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.Result;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("ViewConstructor")
public class RNBarcodeView extends FrameLayout implements RNBarcodeAnalyzeTaskDelegate {
    public PreviewView mPreviewView;
    public ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    public ThemedReactContext mContext;
    public RNBarcodeAnalyzer barcodeAnalyzer;
    public ExecutorService barcodeExcutor;
    public Camera camera;
    ProcessCameraProvider cameraProvider;

    public RNBarcodeView(@NonNull ThemedReactContext context) {
        super(context);
        mContext = context;
        mPreviewView = new PreviewView(context);
        mPreviewView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPreviewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
        mPreviewView.setScaleType(PreviewView.ScaleType.FILL_CENTER);
        this.addView(mPreviewView);

        mPreviewView.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                Log.d("TAG", "onChildViewAdded");
                parent.measure(
                        MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
                parent.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });

        barcodeExcutor = Executors.newSingleThreadExecutor();
        barcodeAnalyzer = new RNBarcodeAnalyzer(this);
    }

    public void startSession() {
        String permission = Manifest.permission.CAMERA;
        int state = mContext.checkSelfPermission(permission);
        if (state == PackageManager.PERMISSION_GRANTED) {
            mPreviewView.post(this::startPreview);
        } else {
            Log.d("Permission", "无相机使用权限");
            Activity activity = mContext.getCurrentActivity();
            if (activity != null) {
                activity.requestPermissions(new String[]{permission}, 0);
            }
        }
    }

    public void stopSession() {
        barcodeExcutor.shutdown();
        cameraProvider.unbindAll();
    }

    public void startPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this.mContext);
        cameraProviderFuture.addListener(() -> {
            try {
                // Camera provider is now guaranteed to be available
                cameraProvider = cameraProviderFuture.get();

                Size size = new Size(mPreviewView.getWidth(), mPreviewView.getHeight());
                // Set up the view finder use case to display camera preview
                Preview preview = new Preview.Builder()
                        .setTargetResolution(size)
                        .build();
                // Choose the camera by requiring a lens facing
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
                imageAnalysis.setAnalyzer(barcodeExcutor, barcodeAnalyzer);

                cameraProvider.unbindAll();
                try {
                    // Attach use cases to the camera with the same lifecycle owner
                    Log.d("TAG", "开始预览");
                    AppCompatActivity activity = (AppCompatActivity) mContext.getCurrentActivity();
                    assert activity != null;
                    camera = cameraProvider.bindToLifecycle(
                            activity,
                            cameraSelector,
                            preview,
                            imageAnalysis);
                    // Connect the preview use case to the previewView
                    preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
                } catch (Exception e) {
                    Log.e("camera", "Use case binding failed", e);
                }
            } catch (InterruptedException | ExecutionException e) {
                // Currently no exceptions thrown. cameraProviderFuture.get()
                // shouldn't block since the listener is being called, so no need to
                // handle InterruptedException.
                Log.e("get cameraProvider fail", e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this.mContext));
    }

    @Override
    public void onAttachedToWindow() {
        Log.d("onAttachedToWindow", "");
        super.onAttachedToWindow();
        startSession();
    }

    @Override
    public void onDetachedFromWindow() {
        Log.d("onDetachedFromWindow", "");
        super.onDetachedFromWindow();
        stopSession();
    }

    @Override
    public void onBarcodeRead(Result result) {
        //Log.d("decodeResult:", result.getText());
        stopSession();
        MediaPlayer mediaPlayer = MediaPlayer.create(mContext, R.raw.beep);
        mediaPlayer.start();

        WritableMap data = Arguments.createMap();
        data.putString("type", String.valueOf(result.getBarcodeFormat()));
        data.putString("value", result.getText());
        sendEvent("onBarcodeRead", data);
    }

    @Override
    public void onAnalyzeTaskCompleted() {

    }

    private void sendEvent(String eventName, WritableMap data) {
        try {
            this.mContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                    getId(),
                    eventName,
                    data);

        } catch (RuntimeException e) {
            Log.e("ERROR", "java.lang.RuntimeException: Trying to invoke Javascript before CatalystInstance has been set!");
        }
    }
}
