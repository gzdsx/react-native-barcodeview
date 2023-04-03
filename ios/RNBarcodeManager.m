//
//  RNBarcodeManager.m
//  chatapp
//
//  Created by songdewei on 2023/4/1.
//

#import "RNBarcodeView.h"
#import "RNBarcodeManager.h"
#import <React/RCTViewManager.h>

@implementation RNBarcodeManager

RCT_EXPORT_MODULE(RNBarcodeView)
RCT_EXPORT_VIEW_PROPERTY(onBarcodeRead, RCTBubblingEventBlock)


- (UIView *)view {
    if (!self.barcodeView) {
        self.barcodeView = [[RNBarcodeView alloc] init];
    }

    return self.barcodeView;
}

+ (BOOL)requiresMainQueueSetup{
  return YES;
}

@end
