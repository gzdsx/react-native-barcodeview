//
//  RNBarcodeView.m
//
//  Created by songdewei on 2023/4/1.
//

#import "RNBarcodeView.h"

@implementation RNBarcodeView

- (instancetype)init {
    if (self = [super init]) {
        self.session = [[AVCaptureSession alloc]init];
        self.sessionQueue = dispatch_queue_create("barCodeCameraQueue", DISPATCH_QUEUE_SERIAL);
        self.previewLayer = [[AVCaptureVideoPreviewLayer alloc] initWithSession:self.session];
        self.previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill;
        self.previewLayer.needsDisplayOnBoundsChange = YES;
        [self setAvalaibleBarCodeTypes];
        [self initializeCaptureSessionInput];
      
      SystemSoundID soundID = 0;
      NSURL *beepUrl = [[NSBundle mainBundle] URLForResource:@"beep" withExtension:@"wav"];
      NSLog(@"beepUrl=%@",beepUrl);
      AudioServicesCreateSystemSoundID((__bridge CFURLRef)(beepUrl), &soundID);
      self.beepSoundID = soundID;
    }

    return self;
}

- (void)layoutSubviews
{
    [super layoutSubviews];
    self.previewLayer.frame = self.bounds;
    [self setBackgroundColor:[UIColor blackColor]];
    [self.layer insertSublayer:self.previewLayer atIndex:0];
}

- (void)removeFromSuperview
{
    NSLog(@"removeFromSuperview");
    [super removeFromSuperview];
}

- (void)didMoveToSuperview {
    NSLog(@"didMoveToSuperview");
    [super didMoveToSuperview];
}

- (void)willMoveToSuperview:(UIView *)newSuperview {
    if (newSuperview == nil) {
        [self stopSession];
    } else {
        [self startSession];
    }

    [super willMoveToSuperview:newSuperview];
}

- (void)updateLayout {
}

- (void)initializeCaptureSessionInput {
    dispatch_async(self.sessionQueue, ^{
        [self.session beginConfiguration];

        NSError *error = nil;
        AVCaptureDevice *captureDevice = [AVCaptureDevice defaultDeviceWithMediaType:AVMediaTypeVideo];

        if (captureDevice == nil) {
            return;
        }

        AVCaptureDeviceInput *captureDeviceInput = [AVCaptureDeviceInput deviceInputWithDevice:captureDevice error:&error];

        if (error || captureDeviceInput == nil) {
            return;
        }

        [self.session removeInput:self.videoCaptureDeviceInput];

        if ([self.session canAddInput:captureDeviceInput]) {
            [self.session addInput:captureDeviceInput];
            self.videoCaptureDeviceInput = captureDeviceInput;
        }

        [self.session commitConfiguration];
    });
}

- (void)setupOrDisableBarcodeScanner {
    if (self.metadataOutput == nil) {
        AVCaptureMetadataOutput *metadataOutput = [[AVCaptureMetadataOutput alloc] init];
        self.metadataOutput = metadataOutput;

        if ([self.session canAddOutput:self.metadataOutput]) {
            [self.metadataOutput setMetadataObjectsDelegate:self queue:self.sessionQueue];
            [self.session addOutput:self.metadataOutput];
            [self.metadataOutput setMetadataObjectTypes:self.barCodeTypes];
        }
    }
}

- (void)startSession {
    dispatch_async(self.sessionQueue, ^{
        NSLog(@"startSession.........");

        if (self.session.isRunning) {
            return;
        }

        [self setupOrDisableBarcodeScanner];
        [self.session startRunning];
    });
}

- (void)stopSession {
#if TARGET_IPHONE_SIMULATOR
    return;

#endif
    dispatch_async(self.sessionQueue, ^{
        NSLog(@"stopSession.........");
        self.metadataOutput = nil;
        [self.session commitConfiguration];
        [self.session stopRunning];
    });
}

- (void)endSession {
#if TARGET_IPHONE_SIMULATOR
    return;

#endif
    dispatch_async(self.sessionQueue, ^{
        [self.previewLayer removeFromSuperlayer];
        [self.session commitConfiguration];
        [self.session stopRunning];

        for (AVCaptureInput *input in self.session.inputs) {
            [self.session removeInput:input];
        }

        for (AVCaptureOutput *output in self.session.outputs) {
            [self.session removeOutput:output];
        }
    });
}

- (void)captureOutput:(AVCaptureOutput *)captureOutput didOutputMetadataObjects:(NSArray *)metadataObjects fromConnection:(AVCaptureConnection *)connection {
    for (AVMetadataMachineReadableCodeObject *metadata in metadataObjects) {
        if ([self.barCodeTypes containsObject:metadata.type]) {
            [self stopSession];
            AudioServicesPlaySystemSound(self.beepSoundID);
            self.onBarcodeRead(@{
                @"type": metadata.type,
                @"value": metadata.stringValue,
                });
            return;
        }
    }
}

- (void)setAvalaibleBarCodeTypes {
    NSMutableArray *avalaibleCodeTypes = [[NSMutableArray alloc] init];

    [avalaibleCodeTypes addObject:AVMetadataObjectTypeUPCECode];
    self.barCodeTypes = @[
        AVMetadataObjectTypeUPCECode,
        AVMetadataObjectTypeCode39Code,
        AVMetadataObjectTypeCode39Mod43Code,
        AVMetadataObjectTypeEAN13Code,
        AVMetadataObjectTypeEAN8Code,
        AVMetadataObjectTypeCode93Code,
        AVMetadataObjectTypePDF417Code,
        AVMetadataObjectTypeQRCode,
        AVMetadataObjectTypeAztecCode,
        AVMetadataObjectTypeInterleaved2of5Code,
        AVMetadataObjectTypeITF14Code,
        AVMetadataObjectTypeDataMatrixCode
    ];
}

+ (BOOL)requiresMainQueueSetup {
    return NO;
}

@end
