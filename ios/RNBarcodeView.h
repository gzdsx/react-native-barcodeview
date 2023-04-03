//
//  RNBarcodeView.h
//
//  Created by songdewei on 2023/4/1.
//

#import <AVFoundation/AVFoundation.h>
#import <React/RCTViewManager.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNBarcodeView : UIView<AVCaptureMetadataOutputObjectsDelegate>


@property (nonatomic, copy) RCTBubblingEventBlock onBarcodeRead;
@property (nonatomic, strong) AVCaptureSession *session;
@property (nonatomic, strong) dispatch_queue_t sessionQueue;
@property (nonatomic, strong, nullable) AVCaptureDeviceInput *videoCaptureDeviceInput;
@property (nonatomic, strong, nullable) AVCaptureMetadataOutput *metadataOutput;
@property (nonatomic, strong, nullable) AVCaptureVideoPreviewLayer *previewLayer;
@property (nonatomic, strong, nullable) NSArray *barCodeTypes;
@property SystemSoundID beepSoundID;

- (void)startSession;
- (void)stopSession;
- (void)endSession;
- (void)initializeCaptureSessionInput;

@end

NS_ASSUME_NONNULL_END
